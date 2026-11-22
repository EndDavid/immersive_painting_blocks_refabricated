package com.imb.registry;

import com.imb.IMBMod;
import com.imb.items.GraffitiConverterItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class IMBItems {
    public static final Item GRAFFITI_CONVERTER_ITEM = new GraffitiConverterItem(
            new Item.Settings().maxCount(1));

    private IMBItems() {
    }

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(IMBMod.MODID, "graffiti_converter_item"), GRAFFITI_CONVERTER_ITEM);
    }
}
