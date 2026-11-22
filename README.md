# Immersive Painting Blocks: Refabricated — Fabric 1.20.1

A Fabric port of [foxynotail/immersive_painting_blocks](https://github.com/foxynotail/immersive_painting_blocks)
(originally NeoForge 1.21.1), built on top of
[Immersive Paintings](https://github.com/Luke100000/ImmersivePaintings) 1.20.1.

The mod id stays `imb`, exactly like the upstream addon, so block/item ids are unchanged
(`imb:graffiti_block`, `imb:graffiti_converter_item`, `imb:graffiti_block_entity`). The display
name of this Fabric port is **Immersive Painting Blocks: Refabricated**.

## Branch layout

This repository keeps one Git branch per loader/version combination:

| Branch | Target | Status |
|---|---|---|
| `main` / `fabric/1.18.2` | Minecraft 1.18.2, Fabric | existing port |
| `fabric/1.20.1` | Minecraft 1.20.1, Fabric | this branch |

`main` currently tracks the 1.18.2 line. The `fabric/1.18.2` branch is an explicit alias of it.
Future ports should follow the same `<loader>/<minecraft-version>` naming scheme.

## What it does

* Right-click an Immersive Paintings **graffiti** entity with the **Graffiti Converter** to replace it
  with a static block that keeps the picture, its size, its facing and its exact wall alignment.
* Right-click a converted block with the converter to **stack thickness** (1..64 layers, 0.004 blocks apart).
* Glowing graffiti becomes a **light source** whose light level grows with thickness (0..15).
* Breaking the block gives back the original `immersive_paintings:graffiti` / `:glow_graffiti` item.

## Requirements

| | |
|---|---|
| Minecraft | 1.20.1 |
| Loader | Fabric Loader ≥ 0.15 |
| Java | 17 |
| Dependencies | Fabric API 0.92.x, Immersive Paintings 0.6.13+1.20.1 |

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

Immersive Paintings is pulled from the Modrinth maven as a `modImplementation`, so it is present in
the dev runtime automatically.

## Notes on the port

* Registries, events and the creative tab use the plain Fabric APIs of 1.20.1 (no `DeferredRegister`).
* The block entity data is stored in NBT with the same keys as the original addon.
* The renderer resolves textures via Immersive Paintings' `ClientPaintingManager`; because a block is not
  an entity, the mod asks the server for the painting list on join (`ServerDataManager.playerRequestedImages`),
  otherwise the client would only ever see the blank canvas fallback.
* `/imb convert`, `/imb thickness` and `/imb stats` from the original addon are **not** ported yet;
  this matches the 1.18.2 Fabric port, which omitted them as well.
* 1.20.1 still uses the plural data-pack directories (`data/imb/loot_tables`, `data/imb/recipes`);
  the singular `loot_table`/`recipe` layout only arrived in 1.21.

## License

GPL-3.0, same as both upstream projects.
