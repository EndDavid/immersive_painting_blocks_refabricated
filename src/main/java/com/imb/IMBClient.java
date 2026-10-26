package com.imb;

import com.imb.client.GraffitiBlockEntityRenderer;
import com.imb.registry.IMBBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class IMBClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(IMBBlockEntities.GRAFFITI_BLOCK_ENTITY, GraffitiBlockEntityRenderer::new);
    }
}

