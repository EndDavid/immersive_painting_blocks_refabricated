package com.imb.client;

import com.imb.blocks.GraffitiBlock;
import com.imb.blocks.GraffitiBlockEntity;
import com.imb.config.IMBConfigClient;
import immersive_paintings.resources.ClientPaintingManager;
import immersive_paintings.resources.Painting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

/**
 * Draws the converted graffiti as a stack of thin quads, one per thickness layer.
 *
 * This mirrors the NeoForge addon: the pictures are drawn at the anchor block centre plus the
 * alignment offset stored in the block entity, so a converted multi-block graffiti lines up with
 * where the entity used to be.
 */
public class GraffitiBlockEntityRenderer implements BlockEntityRenderer<GraffitiBlockEntity> {

    public GraffitiBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(GraffitiBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Identifier motive = blockEntity.getMotive();
        if (motive == null) {
            return;
        }

        Identifier texture = ClientPaintingManager
                .getPaintingTexture(motive, Painting.Type.FULL)
                .textureIdentifier;

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));

        Direction facing = blockEntity.getCachedState().get(GraffitiBlock.FACING);
        int width = blockEntity.getPaintingWidth();
        int height = blockEntity.getPaintingHeight();
        int thickness = blockEntity.getThickness();
        float yaw = blockEntity.getYaw();

        int packedLight = blockEntity.isGlowing() ? LightmapTextureManager.MAX_LIGHT_COORDINATE : light;

        matrices.push();

        // Centre of the anchor block, plus the alignment offset inherited from the entity.
        matrices.translate(0.5D + blockEntity.getShiftX(),
                0.5D + blockEntity.getShiftY(),
                0.5D + blockEntity.getShiftZ());

        // Rotate so that local +Z points away from the wall.
        switch (facing) {
            case SOUTH -> {
            }
            case WEST -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270.0F));
            case NORTH -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            case EAST -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
            case UP -> {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-yaw));
            }
            case DOWN -> {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(yaw));
            }
        }

        // Sit just in front of the supporting wall, exactly like Immersive Paintings' own
        // graffiti.obj (whose canvas plane is at z = 0.01 of a block).
        matrices.translate(0.0D, 0.0D, -0.5D + 0.01D);

        float minX = -width / 2.0F;
        float maxX = width / 2.0F;
        float minY = -height / 2.0F;
        float maxY = height / 2.0F;

        for (int i = 0; i < thickness; i++) {
            matrices.push();
            matrices.translate(0.0D, 0.0D, i * GraffitiBlockEntity.THICKNESS_GAP);

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            quad(consumer, matrix, minX, maxX, minY, maxY, packedLight, overlay);

            matrices.pop();
        }

        matrices.pop();
    }

    private static void quad(VertexConsumer consumer, Matrix4f matrix,
                             float minX, float maxX, float minY, float maxY,
                             int light, int overlay) {
        vertex(consumer, matrix, minX, minY, 0.0F, 1.0F, light, overlay);
        vertex(consumer, matrix, maxX, minY, 1.0F, 1.0F, light, overlay);
        vertex(consumer, matrix, maxX, maxY, 1.0F, 0.0F, light, overlay);
        vertex(consumer, matrix, minX, maxY, 0.0F, 0.0F, light, overlay);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y,
                               float u, float v, int light, int overlay) {
        consumer.vertex(matrix, x, y, 0.0F)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(overlay == 0 ? OverlayTexture.DEFAULT_UV : overlay)
                .light(light)
                .normal(0.0F, 0.0F, 1.0F)
                .next();
    }

    /**
     * Large paintings extend far beyond the one-block anchor, so culling has to be disabled for
     * them; otherwise the picture pops out when the anchor block leaves the frustum.
     */
    @Override
    public boolean rendersOutsideBoundingBox(GraffitiBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return IMBConfigClient.RENDER_DISTANCE;
    }
}
