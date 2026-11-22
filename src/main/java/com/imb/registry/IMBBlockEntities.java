package com.imb.registry;

import com.imb.IMBMod;
import com.imb.blocks.GraffitiBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class IMBBlockEntities {
    public static final BlockEntityType<GraffitiBlockEntity> GRAFFITI_BLOCK_ENTITY = FabricBlockEntityTypeBuilder
            .create(GraffitiBlockEntity::new, IMBBlocks.GRAFFITI_BLOCK)
            .build();

    private IMBBlockEntities() {
    }

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(IMBMod.MODID, "graffiti_block_entity"), GRAFFITI_BLOCK_ENTITY);
    }
}
