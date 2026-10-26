package com.imb.registry;

import com.imb.IMBMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class IMBItemGroups {
    public static final ItemGroup IMB_GROUP = FabricItemGroupBuilder
            .create(new Identifier(IMBMod.MODID, "imb_tab"))
            .icon(() -> new ItemStack(IMBItems.GRAFFITI_CONVERTER_ITEM))
            .appendItems(stacks -> stacks.add(new ItemStack(IMBItems.GRAFFITI_CONVERTER_ITEM)))
            .build();

    private IMBItemGroups() {
    }

    public static void register() {
        // The group is registered implicitly when the field above is initialized.
    }
}

