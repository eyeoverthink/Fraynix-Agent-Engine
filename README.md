# Fraynix-Agent-Engine

Fraynix-Agent-Engine is a Java-based 2D level editor/runtime built with LWJGL, OpenGL, ImGui, and JOML.
It focuses on fast sprite placement, grid snapping, scene persistence, and in-editor object inspection.

## What this project is
- A scene-driven editor (`LevelEditorScene`) plus runtime scene (`LevelScene`) with a shared rendering/input core.
- A component-based object model (`GameObject` + `Component`) where most behavior is composed, not inherited.
- A GPU-backed sprite batch renderer with an entity-picking pass for editor selection.

## Current status
- Primary workflow is Gradle (`build.gradle`, `gradlew.bat`).
- Maven metadata exists (`pom.xml`) but dependency versions differ; prefer one build tool per session.
- `level.txt` is the active serialized scene file and is loaded/saved automatically by `Scene`.

## Core architecture
### 1) App bootstrap and loop
- Entry: `src/main/java/Main.java`.
- Window + game loop: `src/main/java/jade/Window.java`.
- Scene switcher: `Window.changeScene(int)`
  - `0` -> `LevelEditorScene`
  - `1` -> `LevelScene`
- Scene lifecycle order: `load()` -> `init()` -> `start()`.

### 2) Scene + entity model
- Base class: `src/main/java/scenes/Scene.java`.
- Entities: `src/main/java/jade/GameObject.java`.
- Components: `src/main/java/components/Component.java` and subclasses.
- Renderer attachment happens during `Scene.start()` (important for visibility of loaded objects).

### 3) Rendering and picking
- Renderer orchestration: `src/main/java/renderer/Renderer.java`.
- Batching and per-vertex entity id: `src/main/java/renderer/RenderBatch.java`.
- Main framebuffer for viewport image: `src/main/java/renderer/Framebuffer.java`.
- Picking framebuffer for object selection: `src/main/java/renderer/PickingTexture.java`.
- Debug lines/circles/boxes: `src/main/java/renderer/DebugDraw.java`.

### 4) Editor UI
- ImGui integration and dockspace: `src/main/java/jade/ImGuiLayer.java`.
- Viewport window: `src/main/java/editor/GameViewWindow.java`.
- Inspector/properties panel: `src/main/java/editor/PropertiesWindow.java`.
- Mouse drag/drop object placement: `src/main/java/components/MouseControls.java`.

## Data and serialization
- File: `level.txt` in project root.
- Serialization model:
  - `Scene.saveExit()` writes all scene game objects as JSON.
  - `Scene.load()` rebuilds game objects/components.
  - `ComponentDeserializer` stores fully-qualified type names in JSON for polymorphic reconstruction.
- ID continuity:
  - `Scene.load()` scans max object/component ids and resets counters with `GameObject.init(...)` and `Component.init(...)`.

## Assets
- Root assets directory: `assets/`
- Shaders: `assets/shaders/`
- Fonts: `assets/fonts/`
- Sprite atlases/images: `assets/images/` and `assets/tilemap_packed.png`
- Asset cache entrypoint: `src/main/java/util/AssetPool.java`

## Local development setup
### Requirements
- Windows with Java JDK 25 LTS installed (matches `.java-version` and Gradle toolchain).
- OpenGL-capable GPU/drivers.
- Internet access for first Gradle dependency resolution.

## Build and run (PowerShell)
From project root (`Mario_ep18`):

```powershell
.\gradlew.bat run
.\gradlew.bat build
.\gradlew.bat test
```

Notes:
- `test` may be effectively empty right now because `src/main/test` has no tests yet.
- Runtime window title is now `Fraynix-Agent-Engine`.
- If you previously used older Gradle caches, run `clean` once after updating wrapper/toolchain.

## Typical editor workflow
1. Launch app with Gradle run.
2. Use sprite palette in the Level Editor ImGui window to create objects.
3. Move mouse in viewport; placement snaps to grid (`Settings.GRID_WIDTH`, `Settings.GRID_HEIGHT`).
4. Left-click to place.
5. Click existing objects in viewport to inspect in `Inspector`.
6. Close app to persist scene to `level.txt`.

## Input + coordinate mapping details
- Mouse is tracked in screen space then transformed into:
  - viewport-relative screen coordinates (`MouseListener.getScreenX/Y`)
  - orthographic world coordinates (`MouseListener.getOrthoX/Y`)
- Current transform math uses fixed internal viewport dimensions (`1940x1080`).
  If framebuffer sizing changes, update mapping logic accordingly.

## Project conventions (important)
- Use `AssetPool.getTexture/getShader/getSpritesheet` for shared resources.
- Prefer project-root-relative asset paths (`assets/...`).
- Keep behavior in components and data in `GameObject + Transform`.
- Avoid renaming component classes/packages without migration planning: serialized `type` values depend on canonical names.

## Troubleshooting
- App opens but sprites are missing:
  - verify working directory is project root (so `assets/...` resolves correctly).
- Picking selects wrong object or none:
  - check viewport bounds and picking framebuffer resolution assumptions.
- Build works with one tool but not the other:
  - stick to Gradle unless you intentionally align Maven dependencies first.
- Error `Unsupported class file major version ...`:
  - your Gradle runtime and JDK are mismatched; use the checked-in wrapper and confirm `java -version` matches `.java-version`.

## Roadmap ideas
- Add automated tests under `src/main/test`.
- Unify framebuffer constants (replace magic `1940x1080` with shared config).
- Add scene/version migration support for serialized JSON.
- Add camera controls and multi-selection UX improvements.

## Contributing
1. Create a branch.
2. Keep edits small and scene-safe (verify load/save cycle).
3. Build before PR:

```powershell
.\gradlew.bat build
```

4. Include notes when changing serialization format or component type names.

## License
No explicit license file is currently present in this repository.
Add a `LICENSE` file before public redistribution.

