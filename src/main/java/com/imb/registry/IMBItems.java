package com.imb.registry;

import com.imb.IMBMod;
import com.imb.items.GraffitiConverterItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class IMBItems {
    public static final Item GRAFFITI_CONVERTER_ITEM = new GraffitiConverterItem(
            new Item.Settings().maxCount(1));

    private IMBItems() {
    }

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(IMBMod.MODID, "graffiti_converter_item"), GRAFFITI_CONVERTER_ITEM);
    }
}
