// ─── MouseControls.java ──────────────────────────────────
package com.fraynix.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.fraynix.core.GameObject;

public class MouseControls {
    private GameObject activeGameObject = null;
    private float debounceTime = 0.05f;
    private float debounce = 0;

    public void editGameObject(Vector2 screenCoords, Vector2 worldCoords) {
        // Active object follows mouse in grid-snapped positions
        if (activeGameObject != null) {
            float gridWidth = 32; // Settings.GRID_WIDTH
            float gridHeight = 32; // Settings.GRID_HEIGHT

            float x = ((int)Math.floor(worldCoords.x / gridWidth)) * gridWidth;
            float y = ((int)Math.floor(worldCoords.y / gridHeight)) * gridHeight;

            activeGameObject.transform.position.set(x, y);

            if (debounce > 0) {
                debounce -= Gdx.graphics.getDeltaTime();
            } else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                debounce = debounceTime;
                // Place the object (clone it into the scene)
            }
        }
    }

    public void setActiveGameObject(GameObject go) {
        this.activeGameObject = go;
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }
}
