// ─── PickBuffer.java (replaces PickingTexture.java) ──────
// Entity picking via color-encoded FrameBuffer render pass
package com.fraynix.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PickBuffer {
    private FrameBuffer fbo;
    private int width;
    private int height;

    public PickBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
    }

    public void enableWriting() {
        fbo.begin();
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void disableWriting() {
        fbo.end();
    }

    // Read pixel at (x, y) → entity ID encoded as red channel
    public int readPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return -1;
        // Flip Y (LibGDX origin is bottom-left, mouse is top-left)
        int flippedY = height - y;
        Pixmap pixmap = Pixmap.createFromFrameBuffer(x, flippedY, 1, 1);
        int pixel = pixmap.getPixel(0, 0);
        int r = (pixel >> 24) & 0xFF;
        pixmap.dispose();
        return r - 1; // entity ID = red - 1 (0 = no entity)
    }

    public void dispose() {
        if (fbo != null) fbo.dispose();
    }
}
