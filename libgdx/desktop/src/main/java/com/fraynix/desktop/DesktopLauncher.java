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
        // That's it. LibGDX handles:
        //   - Window creation (GLFW on all platforms)
        //   - OpenGL context creation (compatible profile on macOS)
        //   - Native library loading per-OS
        //   - Input callbacks
        //   - Swap buffers / poll events
        new Lwjgl3Application(new FraynixApp(), config);
    }
}
