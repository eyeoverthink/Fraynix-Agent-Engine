// ─── Scene.java ─────────────────────────────────────────
package com.fraynix.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.fraynix.core.GameObject;
import com.fraynix.core.FraynixApp;
import com.fraynix.renderer.Renderer;
import com.fraynix.util.SceneSerializer;

public abstract class Scene {
    protected Renderer renderer;
    protected OrthographicCamera camera;
    protected Array<GameObject> gameObjects;
    protected boolean isRunning = false;
    protected boolean levelLoaded = false;

    public Scene() {
        this.renderer = new Renderer();
        this.gameObjects = new Array<>();
        int width = FraynixApp.get().getWindowWidth();
        int height = FraynixApp.get().getWindowHeight();
        this.camera = new OrthographicCamera(width, height);
        this.camera.translate(width / 2f, height / 2f); // origin bottom-left
        this.camera.update();
    }

    public abstract void load();
    public abstract void init();
    public abstract void start();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void imgui();

    public void saveExit() {
        SceneSerializer.save(this.gameObjects, "level.txt");
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
        }
        renderer.add(go);
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        gameObjects.add(go);
        return go;
    }

    public OrthographicCamera camera() { return this.camera; }
    public Array<GameObject> getGameObjects() { return this.gameObjects; }

    public void destroy() {
        for (GameObject go : gameObjects) {
            // Cleanup resources if needed
        }
        gameObjects.clear();
    }
}
