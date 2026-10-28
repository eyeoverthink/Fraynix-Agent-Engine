# Fraynix-Agent-Engine: LibGDX + ImGui port

This folder holds the LibGDX port of the LWJGL engine at the root of this repo.
It was assembled from the 10 files the Fraymus system saved in the Fraymus Transmuter
app, split into one class per file. The original project is untouched.

## Run

    cd libgdx
    ./gradlew desktop:run        # macOS / Linux
    gradlew.bat desktop:run      # Windows

Gradle 9.1 comes from the wrapper and needs JDK 17 or newer (this repo pins Java 25
in `.java-version`). The code compiles to Java 11 bytecode.

## Layout

- `core/`: entity model, renderer, ImGui editor and utilities (`com.fraynix.*`)
- `desktop/`: `DesktopLauncher` (LWJGL3) plus ImGui natives for Windows, macOS and Linux
- `assets/`: the original project's fonts, images and tilemaps, plus the ported
  `default` and `picking` `.vs`/`.fs` shaders

## Build fixes applied while packaging

- `desktop/build.gradle`: applies the `application` plugin (the `application {}` block
  needs it), runs from this folder and adds `-XstartOnFirstThread` on macOS
- `core/build.gradle`: adds `gdx-backend-lwjgl3` and `imgui-java-lwjgl3`, because
  `EditorUI` imports GLFW and ImGui's GLFW/GL3 backends
- `build.gradle`: drops the fixed Java 21 toolchain so the installed JDK is used

## Mac fixes

- `DesktopLauncher`: requests an OpenGL 3.2 core context on macOS (`setOpenGLEmulation(GL30, 3, 2)`), which the `#version 330` shaders and ImGui need
- `DebugDraw`: on macOS, compiles LibGDX's built-in `ShapeRenderer` shader with a GLSL 150 header (only while that shader is built)
- `FraynixApp`: loads and binds `assets/shaders/default` at startup, so `Renderer.render` draws sprites
- `assets/shaders/default.fs`: `uTextures` is now `sampler2D`, read with constant indices 1-7 to match the texture units `RenderBatch` binds; the unused declaration is gone from `default.vs`
- `RenderBatch`: a batch takes at most 7 textures, one per sampler slot

## Still to do

- Port Rigidbody2D physics from Fraymus-Engine (swap JOML vectors for LibGDX vectors)
- Wire an `InputProcessor` into `MouseListener` / `KeyListener`
- Not compiled or run yet: no Java toolchain was available while packaging
