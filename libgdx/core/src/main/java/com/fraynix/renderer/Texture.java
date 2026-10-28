// ─── Texture.java (wrapper) ──────────────────────────────
package com.fraynix.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class Texture {
    private com.badlogic.gdx.graphics.Texture gdxTexture;
    private String filepath;
    private int width, height;

    public Texture(String filepath) {
        this.filepath = filepath;
        this.gdxTexture = new com.badlogic.gdx.graphics.Texture(Gdx.files.internal(filepath));
        this.width = gdxTexture.getWidth();
        this.height = gdxTexture.getHeight();
    }

    // Create empty texture (for framebuffer attachments)
    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        this.gdxTexture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
    }

    public void bind() { gdxTexture.bind(0); }
    public void bind(int unit) { gdxTexture.bind(unit); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getId() { return gdxTexture.getTextureObjectHandle(); }
    public com.badlogic.gdx.graphics.Texture getGdxTexture() { return gdxTexture; }
    public String getFilepath() { return filepath; }

    public void dispose() {
        if (gdxTexture != null) gdxTexture.dispose();
    }
}
