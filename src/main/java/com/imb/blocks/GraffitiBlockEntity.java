package com.imb.blocks;

import com.imb.registry.IMBBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Stores everything the renderer needs: the Immersive Paintings motive, the original painting
 * size, the number of stacked layers (thickness) and the alignment offsets derived from the
 * converted entity's visual centre.
 */
public class GraffitiBlockEntity extends BlockEntity {
    /** Distance between two stacked layers, in blocks. Mirrors the original addon. */
    public static final float THICKNESS_GAP = 0.004F;
    public static final int MAX_THICKNESS = 64;

    @Nullable
    private Identifier motive;
    private int paintingWidth;
    private int paintingHeight;
    private int thickness = 1;
    private boolean glowing;
    private float yaw;
    private float shiftX;
    private float shiftY;
    private float shiftZ;
    private int lightLevel;

    public GraffitiBlockEntity(BlockPos pos, BlockState state) {
        super(IMBBlockEntities.GRAFFITI_BLOCK_ENTITY, pos, state);
    }

    public void setGraffitiData(@Nullable Identifier motive, int width, int height, boolean glowing,
                                float yaw, float shiftX, float shiftY, float shiftZ) {
        this.motive = motive;
        this.paintingWidth = width;
        this.paintingHeight = height;
        this.glowing = glowing;
        this.yaw = yaw;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.shiftZ = shiftZ;
        this.thickness = Math.max(1, this.thickness);
        markDirty();
        sync();
    }

    public void incrementThickness() {
        if (this.thickness >= MAX_THICKNESS) {
            return;
        }
        setThickness(this.thickness + 1);
    }

    public void setThickness(int thickness) {
        this.thickness = MathHelper.clamp(thickness, 1, MAX_THICKNESS);

        if (this.glowing && this.world != null) {
            int level = MathHelper.ceil((this.thickness / (float) MAX_THICKNESS) * 16) - 1;
            level = MathHelper.clamp(level, 0, 15);
            this.lightLevel = level;

            BlockState state = this.getCachedState();
            if (state.get(GraffitiBlock.LIGHT_LEVEL) != level) {
                // setBlockState also triggers the light engine update, so the block emits light.
                this.world.setBlockState(this.pos, state.with(GraffitiBlock.LIGHT_LEVEL, level), 3);
            }
        }

        markDirty();
        sync();
    }

    private void sync() {
        if (this.world != null && !this.world.isClient) {
            this.world.markDirty(this.pos);
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    @Nullable
    public Identifier getMotive() {
        return motive;
    }

    public int getPaintingWidth() {
        return paintingWidth;
    }

    public int getPaintingHeight() {
        return paintingHeight;
    }

    public int getThickness() {
        return thickness;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public float getYaw() {
        return yaw;
    }

    public float getShiftX() {
        return shiftX;
    }

    public float getShiftY() {
        return shiftY;
    }

    public float getShiftZ() {
        return shiftZ;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.motive = tag.contains("Motive") ? Identifier.tryParse(tag.getString("Motive")) : null;
        this.paintingWidth = tag.getInt("PaintingWidth");
        this.paintingHeight = tag.getInt("PaintingHeight");
        this.thickness = Math.max(1, tag.getInt("Thickness"));
        this.glowing = tag.getBoolean("IsGlowing");
        this.yaw = tag.getFloat("Yaw");
        this.shiftX = tag.getFloat("ShiftX");
        this.shiftY = tag.getFloat("ShiftY");
        this.shiftZ = tag.getFloat("ShiftZ");
        this.lightLevel = tag.getInt("LightLevel");
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        if (this.motive != null) {
            tag.putString("Motive", this.motive.toString());
        }
        tag.putInt("PaintingWidth", this.paintingWidth);
        tag.putInt("PaintingHeight", this.paintingHeight);
        tag.putInt("Thickness", this.thickness);
        tag.putBoolean("IsGlowing", this.glowing);
        tag.putFloat("Yaw", this.yaw);
        tag.putFloat("ShiftX", this.shiftX);
        tag.putFloat("ShiftY", this.shiftY);
        tag.putFloat("ShiftZ", this.shiftZ);
        tag.putInt("LightLevel", this.lightLevel);
    }

    /** Sends the block entity data to everyone tracking the chunk. */
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        writeNbt(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
