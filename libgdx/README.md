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

## Still to do

- Port Rigidbody2D physics from Fraymus-Engine (swap JOML vectors for LibGDX vectors)
- Wire an `InputProcessor` into `MouseListener` / `KeyListener`
- Load and bind the sprite shader: nothing calls `Renderer.bindShader(...)` yet, so
  `Renderer.render` returns before drawing anything
- `assets/shaders/default.fs` declares `uniform int uTextures[8]`; `texture()` needs `sampler2D`
- macOS: the `#version 330` shaders and `imGuiGl3.init("#version 330")` need an OpenGL 3.2
  core context (`config.setOpenGLEmulation(GLEmulation.GL30, 3, 2)` in `DesktopLauncher`);
  LibGDX's built-in `ShapeRenderer` shader (used by `DebugDraw`) then needs a GLSL 150 prefix
- Not compiled yet: no Java toolchain was available while packaging
