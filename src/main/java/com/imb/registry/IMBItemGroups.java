package com.imb.registry;

import com.imb.IMBMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class IMBItemGroups {
    public static final ItemGroup IMB_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.imb.imb_tab"))
            .icon(() -> new ItemStack(IMBItems.GRAFFITI_CONVERTER_ITEM))
            .entries((context, entries) -> entries.add(new ItemStack(IMBItems.GRAFFITI_CONVERTER_ITEM)))
            .build();

    private IMBItemGroups() {
    }

    public static void register() {
        Registry.register(Registries.ITEM_GROUP,
                new Identifier(IMBMod.MODID, "imb_tab"), IMB_GROUP);
    }
}
