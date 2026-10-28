// ─── LevelEditorScene.java ──────────────────────────────
package com.fraynix.scenes;

import com.fraynix.core.GameObject;
import com.fraynix.editor.PropertiesWindow;
import com.fraynix.util.SceneSerializer;

public class LevelEditorScene extends Scene {

    @Override
    public void load() {
        if (levelLoaded) return;
        SceneSerializer.load("level.txt", this);
        levelLoaded = true;
    }

    @Override
    public void init() {
        this.camera.update();
    }

    @Override
    public void start() {
        this.isRunning = true;
        for (GameObject go : gameObjects) {
            go.start();
        }
    }

    @Override
    public void update(float dt) {
        for (GameObject go : gameObjects) {
            go.update(dt);
        }
        camera.update();
    }

    @Override
    public void render() {
        renderer.render(camera);
    }

    @Override
    public void imgui() {
        for (GameObject go : gameObjects) {
            go.imgui();
        }
    }
}
