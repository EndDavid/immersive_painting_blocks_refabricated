package com.imb.events;

import com.imb.blocks.GraffitiBlock;
import com.imb.blocks.GraffitiBlockEntity;
import com.imb.registry.IMBBlocks;
import com.imb.registry.IMBItems;
import immersive_paintings.entity.ImmersiveGlowGraffitiEntity;
import immersive_paintings.entity.ImmersiveGraffitiEntity;
import immersive_paintings.entity.ImmersivePaintingEntity;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class IMBEvents {

    private IMBEvents() {
    }

    public static void register() {
        UseEntityCallback.EVENT.register(IMBEvents::onUseEntity);
    }

    /**
     * Right-clicking a graffiti entity with the converter replaces it with a graffiti block,
     * keeping the motive, size, facing and the exact alignment offsets.
     */
    private static ActionResult onUseEntity(PlayerEntity player, World world, Hand hand,
                                            Entity target, @Nullable EntityHitResult hitResult) {
        if (world.isClient || hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }
        if (!player.getStackInHand(hand).isOf(IMBItems.GRAFFITI_CONVERTER_ITEM)) {
            return ActionResult.PASS;
        }
        if (!(target instanceof ImmersiveGraffitiEntity || target instanceof ImmersiveGlowGraffitiEntity)) {
            return ActionResult.PASS;
        }

        ImmersivePaintingEntity painting = (ImmersivePaintingEntity) target;

        int width = painting.getWidthPixels() / 16;
        int height = painting.getHeightPixels() / 16;
        Direction facing = painting.getHorizontalFacing();
        Identifier motive = painting.getMotive();
        BlockPos anchor = painting.getAttachmentPos();
        float yaw = painting.getRotation();

        boolean glowing = target instanceof ImmersiveGlowGraffitiEntity
                || "glow_graffiti".equals(
                net.minecraft.util.registry.Registry.ENTITY_TYPE.getId(target.getType()).getPath());

        // Offset from the anchor block centre to the entity's visual centre.
        double shiftX = painting.getX() - (anchor.getX() + 0.5D);
        double shiftY = painting.getY() - (anchor.getY() + 0.5D);
        double shiftZ = painting.getZ() - (anchor.getZ() + 0.5D);

        // Keep the picture flush with the supporting wall.
        switch (facing) {
            case NORTH, SOUTH -> shiftZ = 0;
            case EAST, WEST -> shiftX = 0;
            case UP, DOWN -> shiftY = 0;
        }

        if (!canPlaceAt(world, anchor)) {
            return ActionResult.PASS;
        }

        painting.discard();
        generateGraffitiBlock(world, anchor, facing, width, height, motive, glowing, yaw,
                (float) shiftX, (float) shiftY, (float) shiftZ);

        return ActionResult.SUCCESS;
    }

    private static boolean canPlaceAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isReplaceable()
                || world.getBlockState(pos).isOf(IMBBlocks.GRAFFITI_BLOCK);
    }

    private static void generateGraffitiBlock(World world, BlockPos pos, Direction facing,
                                              int width, int height, @Nullable Identifier motive,
                                              boolean glowing, float yaw,
                                              float shiftX, float shiftY, float shiftZ) {
        BlockState state = IMBBlocks.GRAFFITI_BLOCK.getDefaultState()
                .with(GraffitiBlock.FACING, facing)
                .with(GraffitiBlock.GLOWING, glowing);

        world.setBlockState(pos, state, 3);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GraffitiBlockEntity graffiti) {
            graffiti.setGraffitiData(motive, width, height, glowing, yaw, shiftX, shiftY, shiftZ);
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(pos);
            }
        }
    }
}

