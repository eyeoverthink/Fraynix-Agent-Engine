# AGENTS.md

## Project snapshot
- Fraynix-Agent-Engine is a Java/LWJGL + ImGui tile level editor; app entrypoint is `src/main/java/Main.java` -> `jade.Window.run()`.
- Runtime is scene-driven: `Window.changeScene(int)` instantiates `LevelEditorScene` (0) or `LevelScene` (1), then calls `load()`, `init()`, `start()`.
- No existing AI instruction files were found via requested glob search (`README.md`, `AGENTS.md`, `.github/copilot-instructions.md`, `.cursor/rules/**`, etc.).

## Architecture that matters
- `scenes.Scene` is the core boundary: owns `gameObjects`, `renderer`, `camera`, save/load lifecycle, and selection lookup (`getGameObject(id)`).
- Composition model: `jade.GameObject` holds `components.Component` instances; behavior lives in components (`SpriteRenderer`, `MouseControls`, `GridLines`, physics colliders).
- Rendering is batched by z-index (`renderer.Renderer` -> `renderer.RenderBatch`) and only renders `GameObject`s with `SpriteRenderer`.
- Editor UI pipeline is in `jade.ImGuiLayer`: Dockspace + scene UI + `editor.GameViewWindow` + `editor.PropertiesWindow` each frame.
- Picking uses a separate framebuffer (`renderer.PickingTexture`): entity id is written in `RenderBatch` (`uid + 1`) and read back in `PropertiesWindow.update`.

## Data flow and persistence
- Level state persists to `level.txt` via Gson in `Scene.saveExit()` and loads in `Scene.load()`.
- Components serialize polymorphically with canonical class names (`components.ComponentDeserializer`, field `type`), so class/package renames break old level files.
- After load, ID counters are repaired from max seen ids (`GameObject.init(maxGoId+1)`, `Component.init(maxCompId+1)`) to avoid collisions.
- `GameObject.copy()` also uses Gson round-trip cloning; remember to rebind textures from `AssetPool` (already done in method).

## Build/run workflows (discoverable from repo)
- Preferred workflow is Gradle wrapper (`gradlew.bat`) because `build.gradle` defines `application { mainClass = 'Main' }`.
- Maven files exist (`pom.xml`) but dependency versions differ from Gradle (e.g., LWJGL 3.2.3 vs 3.3.1), so avoid mixing outputs in one debug cycle.
- Typical commands from the project root:
  - `./gradlew.bat run`
  - `./gradlew.bat build`
  - `./gradlew.bat test` (currently no tests under `src/main/test`).

## Project-specific conventions and gotchas
- Asset paths are now expected to be project-root relative (`assets/...`), including shaders and fonts.
- Viewport math assumes fixed internal resolution `1940x1080` in `MouseListener.getScreenX/Y`; changing framebuffer size requires updating these transforms.
- Editor placement snaps to `Settings.GRID_WIDTH/HEIGHT` (currently `32`) inside `MouseControls.update`.
- Selection highlighting mutates sprite color in `PropertiesWindow.addActiveGameObject`; restore logic depends on `activeGameObjectsOgColor` ordering.
- `Scene.start()` must run after objects are loaded/added so renderer batches are populated.

## Key integration points
- External libs: LWJGL/OpenGL, ImGui Java binding, Gson, JOML (see `build.gradle`).
- Input ownership is shared: ImGui callbacks forward to `KeyListener`/`MouseListener` only when capture rules allow (`ImGuiLayer` checks `io.getWantCapture*()` and game viewport bounds).
- Resource caching is centralized in `util.AssetPool` using absolute file paths as keys; request textures/shaders through it instead of `new Texture()` / `new Shader()` directly for reusable assets.

