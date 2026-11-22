package com.imb.registry;

import com.imb.IMBMod;
import com.imb.blocks.GraffitiBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class IMBBlocks {
    public static final Block GRAFFITI_BLOCK = new GraffitiBlock(FabricBlockSettings
            .create()
            .mapColor(MapColor.WHITE)
            .strength(0.1F)
            .sounds(BlockSoundGroup.WOOL)
            .nonOpaque()
            .luminance(state -> state.get(GraffitiBlock.LIGHT_LEVEL)));

    private IMBBlocks() {
    }

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(IMBMod.MODID, "graffiti_block"), GRAFFITI_BLOCK);
    }
}
