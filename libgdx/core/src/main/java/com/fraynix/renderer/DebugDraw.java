// ─── DebugDraw.java (ShapeRenderer replaces raw GL lines) ─
package com.fraynix.renderer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class DebugDraw {
    private static ShapeRenderer shapeRenderer;

    public static void init() {
        shapeRenderer = new ShapeRenderer();
    }

    public static void begin(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }

    public static void drawLine(Vector2 from, Vector2 to, float r, float g, float b) {
        shapeRenderer.setColor(r, g, b, 1);
        shapeRenderer.line(from.x, from.y, to.x, to.y);
    }

    public static void drawBox(Vector2 center, float width, float height, float r, float g, float b) {
        shapeRenderer.setColor(r, g, b, 1);
        shapeRenderer.rect(center.x - width/2, center.y - height/2, width, height);
    }

    public static void drawCircle(Vector2 center, float radius, float r, float g, float b) {
        shapeRenderer.setColor(r, g, b, 1);
        shapeRenderer.circle(center.x, center.y, radius);
    }

    public static void end() {
        shapeRenderer.end();
    }

    public static void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
