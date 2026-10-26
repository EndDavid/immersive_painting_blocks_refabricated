package com.imb.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** The tool that converts an Immersive Paintings graffiti entity into a block. */
public class GraffitiConverterItem extends Item {

    public GraffitiConverterItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Use on Immersive Paintings Graffiti to set in place as a block")
                .formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(new LiteralText("Use on Converted Graffiti Blocks to increase thickness")
                .formatted(Formatting.GRAY, Formatting.ITALIC));

        super.appendTooltip(stack, world, tooltip, context);
    }
}

