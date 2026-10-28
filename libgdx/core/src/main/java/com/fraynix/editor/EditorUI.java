// ─── EditorUI.java (replaces ImGuiLayer.java) ───────────
package com.fraynix.editor;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

import com.fraynix.scenes.Scene;

public class EditorUI {
    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;
    private GameViewWindow gameViewWindow;
    private PropertiesWindow propertiesWindow;
    private boolean initialized = false;

    // We need the GLFW window handle from LibGDX
    // LibGDX's Lwjgl3Application uses GLFW internally
    // We access it via GLFW.glfwGetCurrentContext()
    public EditorUI() {
        // ImGui init happens in first render() call because we need
        // the GLFW window to exist first (LibGDX creates it in create())
    }

    private void initImGui() {
        // Get the GLFW window handle created by LibGDX
        long windowHandle = GLFW.glfwGetCurrentContext();

        // Initialize ImGui GLFW binding
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGlfw.init(windowHandle, true);

        // Initialize ImGui GL3 binding
        imGuiGl3 = new ImGuiImplGl3();
        imGuiGl3.init("#version 330");

        // Create editor windows
        gameViewWindow = new GameViewWindow();
        propertiesWindow = new PropertiesWindow();

        // Setup ImGui style (dark theme, dockspace)
        ImGui.styleColorsDark();
        ImGui.getIO().setIniFilename("imgui.ini"); // persist layout

        initialized = true;
    }

    public void render(float dt, int framebufferTextureId, Scene currentScene) {
        if (!initialized) {
            initImGui();
        }

        // Start ImGui frame
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // Dockspace (full window)
        createDockspace();

        // Viewport window — renders the scene framebuffer texture
        gameViewWindow.imGui(framebufferTextureId);

        // Scene imgui (component inspectors)
        currentScene.imgui();

        // Properties inspector
        propertiesWindow.imGui(currentScene);

        // Render ImGui draw data
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void createDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse;
        windowFlags |= ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove;
        windowFlags |= ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY());
        ImGui.pushStyleVar(ImGui.style().getWindowRounding(), 0);
        ImGui.pushStyleVar(ImGui.style().getWindowBorderSize(), 0);
        ImGui.pushStyleVar(ImGui.style().getWindowPadding(), 0, 0);

        ImGui.begin("Dockspace", windowFlags);
        ImGui.popStyleVar(3);

        long dockspaceId = ImGui.getID("FraynixDockspace");
        ImGui.dockSpace(dockspaceId, 0, 0, ImGuiWindowFlags.NoDocking);
        ImGui.end();
    }

    public void resize(int width, int height) {
        // ImGui handles its own resize via GLFW callbacks
    }

    public void dispose() {
        if (imGuiGl3 != null) imGuiGl3.dispose();
        // imGuiGlfw cleanup handled by GLFW window destruction
    }
}
