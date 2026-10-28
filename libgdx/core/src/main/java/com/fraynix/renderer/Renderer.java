// ─── Renderer.java (orchestration — mostly unchanged) ────
package com.fraynix.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.fraynix.components.SpriteRenderer;
import com.fraynix.core.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 10000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.zIndex) {
                Texture tex = sprite.getTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            batches.sort(RenderBatch::compareTo);
        }
    }

    public static void bindShader(Shader shader) { currentShader = shader; }
    public static Shader getBoundShader() { return currentShader; }

    public void render(OrthographicCamera camera) {
        if (currentShader == null) return;
        currentShader.bind();
        for (RenderBatch batch : batches) {
            batch.render(currentShader, camera.projection, camera.view);
        }
    }

    public void dispose() {
        for (RenderBatch batch : batches) {
            batch.dispose();
        }
    }
}
