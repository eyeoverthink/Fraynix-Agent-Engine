// ─── PropertiesWindow.java ───────────────────────────────
package com.fraynix.editor;

import imgui.ImGui;
import imgui.type.ImString;
import imgui.flag.ImGuiInputTextFlags;

import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.Array;
import com.fraynix.core.Component;
import com.fraynix.core.GameObject;
import com.fraynix.components.SpriteRenderer;
import com.fraynix.scenes.Scene;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private ImString nameBuf = new ImString(256);

    public void imGui(Scene scene) {
        ImGui.begin("Properties");

        if (activeGameObject != null) {
            // Object name
            nameBuf.set(activeGameObject.getName());
            if (ImGui.inputText("Name", nameBuf, ImGuiInputTextFlags.EnterReturnsTrue)) {
                // Update name (would need setName on GameObject)
            }

            ImGui.separator();

            // Iterate components and draw their ImGui
            for (Component c : activeGameObject.getComponents()) {
                c.imgui();
            }

            // SpriteRenderer inspector
            SpriteRenderer spr = activeGameObject.getComponent(SpriteRenderer.class);
            if (spr != null) {
                if (ImGui.collapsingHeader("Sprite Renderer")) {
                    Vector4 color = spr.getColor();
                    float[] colorArr = {color.x, color.y, color.z, color.w};
                    if (ImGui.colorPicker4("Color", colorArr)) {
                        spr.setColor(new Vector4(colorArr[0], colorArr[1], colorArr[2], colorArr[3]));
                    }
                }
            }
        } else {
            ImGui.text("No object selected");
        }

        ImGui.end();
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }
}
