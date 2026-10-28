// ─── GameViewWindow.java ─────────────────────────────────
package com.fraynix.editor;

import imgui.ImGui;
import imgui.type.ImBoolean;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

public class GameViewWindow {
    private static int lastTextureId = -1;

    public void imGui(int framebufferTextureId) {
        ImGui.begin("Viewport", new ImBoolean(true));

        // Get available content region
        float windowWidth = ImGui.getContentRegionAvailX();
        float windowHeight = ImGui.getContentRegionAvailY();

        // Render the framebuffer texture as an ImGui image
        // ImGui uses OpenGL texture IDs directly
        ImGui.image(framebufferTextureId, windowWidth, windowHeight, 0, 1, 1, 0);

        // Handle click-to-select via picking texture
        // (Original code read pixels from PickingTexture here)
        if (ImGui.isItemHovered() && ImGui.isMouseClicked(0)) {
            float mouseX = ImGui.getMousePosX() - ImGui.getWindowPosX();
            float mouseY = ImGui.getMousePosY() - ImGui.getWindowPosY();
            // TODO: Trigger pick buffer read at (mouseX, mouseY)
        }

        ImGui.end();
    }
}
