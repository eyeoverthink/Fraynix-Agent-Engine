// ============================================================
// DesktopLauncher.java — THE FILE THAT FIXES macOS
// Path: desktop/src/com/fraynix/desktop/DesktopLauncher.java
// ============================================================
// This replaces the old Main.java + Window.java GLFW init.
// LibGDX's Lwjgl3Application auto-detects the OS and loads
// the correct LWJGL3 natives. No more "natives-windows" hardcode.
// ImGui natives for all platforms are in desktop/build.gradle.
// ============================================================

package com.fraynix.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fraynix.core.FraynixApp;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Fraynix-Agent-Engine");
        config.setWindowedMode(1920, 1080);
        config.setForegroundFPS(60);
        config.setVSync(true);
        config.setResizable(true);
        // macOS only offers OpenGL 3.2+ as a core profile, which the engine's
        // #version 330 shaders and ImGui need. Windows/Linux keep LibGDX's default.
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 2);
        }
        // That's it. LibGDX handles:
        //   - Window creation (GLFW on all platforms)
        //   - OpenGL context creation (3.2 core profile on macOS, see above)
        //   - Native library loading per-OS
        //   - Input callbacks
        //   - Swap buffers / poll events
        new Lwjgl3Application(new FraynixApp(), config);
    }
}
