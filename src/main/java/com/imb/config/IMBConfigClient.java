package com.imb.config;

/**
 * Client side settings.
 *
 * The NeoForge original used a {@code ModConfigSpec} for a single value. Fabric 1.18.2 has no
 * built-in config API, so the render distance is a plain constant for now (can be replaced by a
 * JSON/cloth-config based implementation later).
 */
public final class IMBConfigClient {
    /**
     * How far away (in blocks) converted graffiti blocks keep rendering. Vanilla block entity
     * renderers default to 64; paintings can be large, so use the entity tracking maximum instead.
     */
    public static final int RENDER_DISTANCE = 256;

    private IMBConfigClient() {
    }
}

