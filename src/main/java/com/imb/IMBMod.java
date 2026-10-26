package com.imb;

import com.imb.events.IMBEvents;
import com.imb.registry.IMBBlockEntities;
import com.imb.registry.IMBBlocks;
import com.imb.registry.IMBItemGroups;
import com.imb.registry.IMBItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import immersive_paintings.ServerDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common (server + client) entrypoint.
 *
 * Port of https://github.com/foxynotail/immersive_painting_blocks (1.21.1 NeoForge)
 * to 1.18.2 Fabric, running on top of Immersive Paintings 1.18.2.
 */
public class IMBMod implements ModInitializer {
    public static final String MODID = "imb";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        IMBBlocks.register();
        IMBItems.register();
        IMBBlockEntities.register();
        IMBItemGroups.register();

        IMBEvents.register();

        // Immersive Paintings normally only sends its painting list when a player starts tracking
        // a painting entity. Our converted blocks are not entities, so make sure every joining
        // player receives the list up front. Without it the client cannot resolve the painting
        // texture and would render the blank canvas fallback.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerDataManager.playerRequestedImages(handler.player));

        LOGGER.info("Immersive Painting Blocks loaded");
    }
}

