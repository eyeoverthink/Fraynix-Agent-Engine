// ─── KeyListener.java (uses LibGDX input) ────────────────
package com.fraynix.util;

import com.badlogic.gdx.Gdx;

public class KeyListener {
    private static boolean keyPressed[] = new boolean[350];

    public static void editorUpdate(float dt) {
        // Check for key presses
        for (int i = 0; i < 350; i++) {
            keyPressed[i] = Gdx.input.isKeyPressed(i);
        }
    }

    public static boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }
}
