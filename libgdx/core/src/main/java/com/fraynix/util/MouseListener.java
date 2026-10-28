// ─── MouseListener.java (uses LibGDX input) ──────────────
package com.fraynix.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MouseListener {
    private static double scrollX, scrollY;
    private static float x, y;
    private static float lastX, lastY;
    private static boolean mouseButtonPressed[] = new boolean[3];
    private static boolean isDragging;

    public static void editorUpdate(float dt, OrthographicCamera camera) {
        x = Gdx.input.getX();
        y = Gdx.input.getY();
    }

    public static float getX() { return x; }
    public static float getY() { return y; }

    public static float getOrthoX(OrthographicCamera camera) {
        Vector2 world = screenToWorld(camera, x, y);
        return world.x;
    }

    public static float getOrthoY(OrthographicCamera camera) {
        Vector2 world = screenToWorld(camera, x, y);
        return world.y;
    }

    public static Vector2 screenToWorld(OrthographicCamera camera, float screenX, float screenY) {
        Vector2 world = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        camera.unproject(new com.badlogic.gdx.math.Vector3(world.x, world.y, 0));
        return world;
    }

    public static boolean mouseButtonDown(int button) {
        return Gdx.input.isButtonPressed(button);
    }
}
