package com.imb.registry;

import com.imb.IMBMod;
import com.imb.blocks.GraffitiBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class IMBBlocks {
    public static final Block GRAFFITI_BLOCK = new GraffitiBlock(FabricBlockSettings
            .of(Material.WOOL)
            .strength(0.1F)
            .sounds(BlockSoundGroup.WOOL)
            .nonOpaque()
            .luminance(state -> state.get(GraffitiBlock.LIGHT_LEVEL)));

    private IMBBlocks() {
    }

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(IMBMod.MODID, "graffiti_block"), GRAFFITI_BLOCK);
    }
}

