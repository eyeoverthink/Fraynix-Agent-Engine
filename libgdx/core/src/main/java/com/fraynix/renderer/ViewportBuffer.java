// ─── ViewportBuffer.java (replaces Framebuffer.java) ─────
// BEFORE: 30 lines of raw GL30 FBO + renderbuffer setup
// AFTER: 10 lines using LibGDX FrameBuffer
package com.fraynix.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class ViewportBuffer {
    private FrameBuffer fbo;
    private int width;
    private int height;

    public ViewportBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
    }

    public void bind() {
        fbo.begin();
        Gdx.gl20.glViewport(0, 0, width, height);
    }

    public void unbind() {
        fbo.end();
    }

    public int getTextureId() {
        return fbo.getColorBufferTexture().getTextureObjectHandle();
    }

    public com.badlogic.gdx.graphics.Texture getColorTexture() {
        return fbo.getColorBufferTexture();
    }

    public void dispose() {
        if (fbo != null) fbo.dispose();
    }
}
