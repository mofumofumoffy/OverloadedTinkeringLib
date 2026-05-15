package moffy.overloaded_tinkering_lib.client.provider.context;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.item.ItemStack;

public record RenderQuadContext(
        ItemStack itemStack,
        RenderType renderType,
        RenderContext renderContext,
        BakedQuad quad
) {
    public VertexConsumer getConsumer() {
        return renderContext.getBuffer(renderType);
    }
}
