# Immersive Painting Blocks Refabricated

A Fabric port of [foxynotail/immersive_painting_blocks](https://github.com/foxynotail/immersive_painting_blocks)
(originally NeoForge 1.21.1) to **Minecraft 1.18.2 / Fabric**, built on top of
[Immersive Paintings](https://github.com/Luke100000/ImmersivePaintings) 1.18.2.

The mod id stays `imb`, identical to upstream, so block/item ids are unchanged.

## What it does

* Right-click an Immersive Paintings **graffiti** entity with the **Graffiti Converter** to replace it
  with a static block that keeps the picture, its size, its facing and its exact wall alignment.
* Right-click a converted block with the converter to **stack thickness** (1..64 layers, 0.004 blocks apart).
* Glowing graffiti becomes a **light source** whose light level grows with thickness (0..15).
* Breaking the block gives back the original `immersive_paintings:graffiti` / `:glow_graffiti` item.

## Requirements

| | |
|---|---|
| Minecraft | 1.18.2 |
| Loader | Fabric Loader ≥ 0.14 |
| Java | 17 |
| Dependencies | Fabric API, Immersive Paintings 0.6.7+1.18.2 |

## Building

The build needs **JDK 21** for Gradle itself (Loom 1.15 requires it); the mod is still compiled with
`--release 17`.

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-21"
.\gradlew.bat build
```

The jar lands in `build/libs/`.

## Running in dev

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-21"
.\gradlew.bat runClient     # or runServer
```

Immersive Paintings is pulled from the Modrinth maven as a `modImplementation`, so it is present in the
dev runtime automatically.

## Notes on the port

* Registries, events and the creative tab use the plain Fabric APIs of 1.18.2 (no `DeferredRegister`).
* The block entity data is stored in NBT with the same keys as the original addon.
* The renderer resolves textures via Immersive Paintings' `ClientPaintingManager`; because a block is not
  an entity, the mod asks the server for the painting list on join (`ServerDataManager.playerRequestedImages`),
  otherwise the client would only ever see the blank canvas fallback.
* `/imb stats` from the original addon is **not** ported: it depends on 1.19+ sculk classes and on Create.

## License

GPL-3.0, same as both upstream projects.
