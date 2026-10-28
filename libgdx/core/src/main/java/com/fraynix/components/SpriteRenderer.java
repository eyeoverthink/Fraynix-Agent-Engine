// ─── SpriteRenderer.java (component — almost unchanged) ──
package com.fraynix.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.fraynix.core.Component;
import com.fraynix.core.GameObject;
import com.fraynix.renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4 color;
    private Texture texture;
    private Vector2[] texCoords;
    private boolean dirty = false;

    // Default texture coordinates for a full quad
    private static final Vector2[] DEFAULT_TEX_COORDS = {
        new Vector2(1, 1),  // bottom-right
        new Vector2(1, 0),  // top-right
        new Vector2(0, 0),  // top-left
        new Vector2(0, 1)   // bottom-left
    };

    public SpriteRenderer(Vector4 color) {
        this.color = color;
        this.texture = null;
        this.texCoords = DEFAULT_TEX_COORDS;
    }

    public SpriteRenderer(Texture texture) {
        this.texture = texture;
        this.color = new Vector4(1, 1, 1, 1);
        this.texCoords = DEFAULT_TEX_COORDS;
    }

    public SpriteRenderer() {
        this(new Vector4(1, 1, 1, 1));
    }

    public Texture getTexture() { return this.texture; }
    public void setTexture(Texture texture) { this.texture = texture; this.dirty = true; }

    public Vector4 getColor() { return this.color; }
    public void setColor(Vector4 color) { this.color = color; this.dirty = true; }

    public Vector2[] getTexCoords() { return this.texCoords; }
    public void setTexCoords(Vector2[] coords) { this.texCoords = coords; this.dirty = true; }

    public boolean isDirty() { return this.dirty; }
    public void setClean() { this.dirty = false; }

    @Override
    public void start() {
        if (this.texture != null) {
            // Ensure texture is loaded
        }
    }

    @Override
    public void imgui() {
        // ImGui color picker will be called from PropertiesWindow
    }
}
