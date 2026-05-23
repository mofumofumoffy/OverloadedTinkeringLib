package moffy.overloaded_tinkering_lib.client.render;

import moffy.overloaded_tinkering_lib.client.provider.renderer.QuadContextRendererImpl;
import moffy.overloaded_tinkering_lib.client.provider.context.ItemRenderContext;
import moffy.overloaded_tinkering_lib.client.provider.context.RenderContext;
import moffy.overloaded_tinkering_lib.client.provider.context.RenderQuadContext;
import moffy.overloaded_tinkering_lib.client.shader.RenderTasks;
import moffy.overloaded_tinkering_lib.client.provider.ShaderProvider;
import moffy.overloaded_tinkering_lib.client.shader.ShaderToolQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class ToolRenders {
    public static List<RenderTasks.RenderTask> prepareRenderTasks(
            RenderType renderType,
            List<BakedQuad> pQuads,
            ItemRenderContext itemRenderContext,
            @Nullable ToolStack tool,
            List<ShaderProvider.Tool> seenList
    ) {
        List<RenderTasks.RenderTask> renderTasks = new ArrayList<>();
        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        ItemStack pItemStack = itemRenderContext.itemStack();

        List<RenderQuadContext> renderContexts = new ArrayList<>();

        // prepare contexts
        for (BakedQuad bakedQuad : pQuads) {
            int overridedColor = -1;

            if (!pItemStack.isEmpty() && bakedQuad.isTinted()) {
                overridedColor = itemColors.getColor(pItemStack, bakedQuad.getTintIndex());
            }

            // calculate quad color
            RenderContext renderContext = getRenderContext(itemRenderContext, overridedColor);
            RenderQuadContext context = new RenderQuadContext(
                    itemRenderContext.itemStack(),
                    renderType,
                    renderContext,
                    bakedQuad
            );

            renderContexts.add(context);
        }

        // add render batch task
        List<ToolRenderBatch> renderBatches = processRenderBatches(itemRenderContext, renderContexts, tool, seenList);

        for (ToolRenderBatch renderBatch : renderBatches) {
            if (renderBatch.shaderProvider == null) {
                renderTasks.add(new RenderTasks.NakedBatchTask(renderBatch, itemRenderContext, renderType));
            } else {
                renderTasks.add(new RenderTasks.RenderBatchTask(renderBatch, itemRenderContext));
            }
        }

        return renderTasks;
    }

    private static @NotNull RenderContext getRenderContext(ItemRenderContext itemRenderContext, int overridedColor) {
        float r = overridedColor != -1 ? ((float) ((overridedColor >> 16) & 255) / 255.0F) : 1.0f;
        float g = overridedColor != -1 ? ((float) ((overridedColor >> 8) & 255) / 255.0F) : 1.0f;
        float b = overridedColor != -1 ? ((float) (overridedColor & 255) / 255.0F) : 1.0f;

        RenderContext renderContext = new RenderContext(
                itemRenderContext.bufferSource(),
                r, g, b, 1.0f,
                itemRenderContext.poseStack(),
                itemRenderContext.combinedLight(),
                itemRenderContext.combinedOverlay()
        );
        return renderContext;
    }

    public static List<ToolRenderBatch> processRenderBatches(ItemRenderContext itemRenderContext, List<RenderQuadContext> contexts, @Nullable ToolStack toolStack, List<ShaderProvider.Tool> seenList) {

        EnumMap<RenderPhase, ShaderRenderTasksMap> phaseTasksMap = new EnumMap<>(RenderPhase.class);

        for (RenderQuadContext context : contexts) {
            BakedQuad quad = context.quad();

            ShaderProvider.Tool shaderProvider;
            if (quad instanceof ShaderToolQuad shaderToolQuad) {
                shaderProvider = shaderToolQuad.getShaderProvider();
            } else {
                shaderProvider = null;
            }

            List<RenderTasks.RenderTask> renderTasks = new ArrayList<>(
                    getMaterialQuadRenderTasks(context, itemRenderContext, seenList)
            );

            if (toolStack != null && context.itemStack().getItem() instanceof IModifiable) {
                renderTasks.addAll(getModifierQuadRenderTasks(context, toolStack, seenList));
            }

            for (RenderTasks.RenderTask renderTask : renderTasks) {
                RenderPhase phase = renderTask.getPhase();

                // phaseTasksMap[phase][shaderProvider].add(renderTask)
                phaseTasksMap
                        .computeIfAbsent(phase, renderPhase -> new ShaderRenderTasksMap())
                        .computeIfAbsent(shaderProvider, sp -> new ArrayList<>())
                        .add(renderTask);
            }
        }

        List<ToolRenderBatch> batches = new ArrayList<>();

        for (RenderPhase renderPhase : phaseTasksMap.keySet()) {
            ShaderRenderTasksMap shaderRenderTasksMap = phaseTasksMap.get(renderPhase);

            shaderRenderTasksMap.forEach((shaderProvider, renderTasks) -> {
                ToolRenderBatch renderBatch = new ToolRenderBatch(
                        shaderProvider,
                        renderPhase,
                        renderTasks
                );

                batches.add(renderBatch);
            });
        }

        return batches;
    }

    public static List<RenderTasks.RenderTask> getMaterialQuadRenderTasks(RenderQuadContext context, ItemRenderContext itemRenderContext, List<ShaderProvider.Tool> seenList) {
        List<RenderTasks.RenderTask> renderTasks = new ArrayList<>();
        BakedQuad bakedQuad = context.quad();

        if (!(bakedQuad instanceof ShaderToolQuad.Material shaderToolQuad)) {
            return List.of(new RenderTasks.NakedRenderTask(
                    RenderPhase.OVERLAY_NORMAL,
                    context,
                    QuadContextRendererImpl.RENDERER
            ));
        }

        ShaderProvider.Tool provider = shaderToolQuad.getShaderProvider();

        if (provider == null) {
            return List.of(new RenderTasks.NakedRenderTask(
                    RenderPhase.OVERLAY_NORMAL,
                    context,
                    QuadContextRendererImpl.RENDERER
            ));
        }

        if (!seenList.contains(provider)) {
            renderTasks.add(new RenderTasks.InstantRenderTask(
                    RenderPhase.UNDERLAY,
                    () -> {
                        provider.preRenderMaterial(itemRenderContext, shaderToolQuad.getMaterialId());
                        provider.renderUnderlay(context, QuadContextRendererImpl.RENDERER);
                    }
            ));
            seenList.add(provider);
        }

        //overlay
        renderTasks.add(new RenderTasks.InstantRenderTask(
                RenderPhase.OVERLAY_MATERIAL,
                () -> {
                    provider.preRenderMaterial(itemRenderContext, shaderToolQuad.getMaterialId());
                    provider.renderOverlay(context, QuadContextRendererImpl.RENDERER);
                }
        ));

        return renderTasks;
    }

    public static List<RenderTasks.RenderTask> getModifierQuadRenderTasks(RenderQuadContext context, ToolStack tool, List<ShaderProvider.Tool> seenList) {
        List<RenderTasks.RenderTask> renderTasks = new ArrayList<>();
        BakedQuad bakedQuad = context.quad();

        if (!(bakedQuad instanceof ShaderToolQuad.Modifier shaderToolQuad)) {
            return List.of();
        }

        ShaderProvider.Tool provider = shaderToolQuad.getShaderProvider();

        if (provider == null) {
            //normal modifier
            return List.of(new RenderTasks.NakedRenderTask(
                    RenderPhase.OVERLAY_NORMAL,
                    context,
                    QuadContextRendererImpl.RENDERER
            ));
        }

        if (!seenList.contains(provider)) {
            renderTasks.add(new RenderTasks.InstantRenderTask(
                    RenderPhase.UNDERLAY,
                    () -> {
                        provider.preRenderModifier(tool, shaderToolQuad.getModifierId());
                        provider.renderUnderlay(context, QuadContextRendererImpl.RENDERER);
                    }
            ));
            seenList.add(provider);
        }

        //overlay
        renderTasks.add(new RenderTasks.InstantRenderTask(
                RenderPhase.OVERLAY_MODIFIER,
                () -> {
                    provider.preRenderModifier(tool, shaderToolQuad.getModifierId());
                    provider.renderOverlay(context, QuadContextRendererImpl.RENDERER);
                }
        ));

        return renderTasks;
    }

    public enum RenderPhase {
        UNDERLAY(0),
        OVERLAY_MATERIAL(2),
        OVERLAY_NORMAL(3),
        OVERLAY_MODIFIER(4);

        private final int index;

        RenderPhase(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static final class ShaderRenderTasksMap extends HashMap<ShaderProvider.Tool, List<RenderTasks.RenderTask>> {
    }

    public record ToolRenderBatch(
            @Nullable ShaderProvider.Tool shaderProvider,
            RenderPhase renderPhase,
            List<RenderTasks.RenderTask> renderTasks
    ) {

    }
}