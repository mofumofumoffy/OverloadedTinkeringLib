package moffy.overloaded_tinkering_lib.client.provider.context.armor;

import com.mojang.blaze3d.vertex.VertexConsumer;
import moffy.overloaded_tinkering_lib.client.provider.context.RenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public record RenderGenericContext(
        RenderContext renderContext,
        ResourceLocation atlasLocation,
        RenderType renderType,
        BufferGetter bufferGetter,
        boolean onGui
) {
    public interface BufferGetter {
        VertexConsumer get(RenderType renderType);
    }
}
