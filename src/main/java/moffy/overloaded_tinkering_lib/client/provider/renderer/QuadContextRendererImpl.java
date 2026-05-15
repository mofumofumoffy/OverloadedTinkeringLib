package moffy.overloaded_tinkering_lib.client.provider.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class QuadContextRendererImpl implements IQuadContextRenderer {
    public static final QuadContextRendererImpl RENDERER = new QuadContextRendererImpl();

    @Override
    public void renderQuad(BakedQuad quad, VertexConsumer consumer, float red, float green, float blue, float alpha, PoseStack poseStack, int combinedLight, int combinedOverlay) {
        consumer.putBulkData(
                poseStack.last(),
                quad,
                red, green, blue,
                combinedLight, combinedOverlay
        );
    }
}