package com.imb.blocks;

import com.imb.registry.IMBItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A block that renders an Immersive Paintings graffiti as a static, solid surface.
 *
 * The block itself is a single "anchor" block; the painting texture is drawn by
 * {@link com.imb.client.GraffitiBlockEntityRenderer} and may extend past the block bounds.
 */
public class GraffitiBlock extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty GLOWING = BooleanProperty.of("glowing");
    public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);

    /** Thickness of one drawn layer in blocks (0.004 * 16 = 0.064 pixel). */
    private static final float MIN_DEPTH = 0.0625F;

    public GraffitiBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(GLOWING, false)
                .with(LIGHT_LEVEL, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, GLOWING, LIGHT_LEVEL);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    private VoxelShape makeShape(BlockState state, BlockView world, BlockPos pos) {
        float depth = MIN_DEPTH;

        // Reading the block entity may fail for unloaded positions (structure/worldgen queries),
        // in that case fall back to a one pixel thick plate.
        if (world != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof GraffitiBlockEntity graffiti) {
                depth = Math.max(MIN_DEPTH, graffiti.getThickness() * GraffitiBlockEntity.THICKNESS_GAP);
            }
        }

        Direction facing = state.get(FACING);
        switch (facing) {
            case SOUTH:
                return Block.createCuboidShape(0, 0, 0, 16, 16, depth * 16);
            case NORTH:
                return Block.createCuboidShape(0, 0, (1 - depth) * 16, 16, 16, 16);
            case WEST:
                return Block.createCuboidShape((1 - depth) * 16, 0, 0, 16, 16, 16);
            case EAST:
            default:
                return Block.createCuboidShape(0, 0, 0, depth * 16, 16, 16);
            case UP:
                return Block.createCuboidShape(0, 0, 0, 16, depth * 16, 16);
            case DOWN:
                return Block.createCuboidShape(0, (1 - depth) * 16, 0, 16, 16, 16);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape(state, world, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape(state, world, pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GraffitiBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(IMBItems.GRAFFITI_CONVERTER_ITEM)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof GraffitiBlockEntity graffiti) {
                if (!world.isClient) {
                    graffiti.incrementThickness();
                }
                return ActionResult.success(world.isClient);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
