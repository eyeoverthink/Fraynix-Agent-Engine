// ============================================================
// FraynixApp.java — ApplicationListener (replaces Main + Window)
// Path: core/src/com/fraynix/core/FraynixApp.java
// ============================================================
// Replaces:
//   - Main.java (entry point)
//   - Window.java (GLFW window + game loop)
// LibGDX's ApplicationListener provides the same lifecycle:
//   create() = init() + start()
//   render() = loop body (update + render + imgui)
//   resize() = framebuffer resize
//   dispose() = destroy()
// ============================================================

package com.fraynix.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fraynix.editor.EditorUI;
import com.fraynix.renderer.Renderer;
import com.fraynix.renderer.ViewportBuffer;
import com.fraynix.scenes.LevelEditorScene;
import com.fraynix.scenes.LevelScene;
import com.fraynix.scenes.Scene;
import com.fraynix.util.AssetPool;

public class FraynixApp extends ApplicationAdapter {

    public static final int EDITOR_SCENE = 0;
    public static final int LEVEL_SCENE = 1;

    private static FraynixApp instance;

    private Scene currentScene;
    private ViewportBuffer framebuffer;
    private EditorUI editorUI;

    private int windowWidth = 1920;
    private int windowHeight = 1080;

    // ImGui viewport dimensions (matches original 1940x1080 internal viewport)
    private int viewportWidth = 1920;
    private int viewportHeight = 1080;

    @Override
    public void create() {
        instance = this;
        this.framebuffer = new ViewportBuffer(viewportWidth, viewportHeight);
        this.editorUI = new EditorUI();
        // Sprite shader: Renderer.render() draws nothing until one is bound
        Renderer.bindShader(AssetPool.getShader("assets/shaders/default"));
        changeScene(EDITOR_SCENE);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Clear screen
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1.0f);

        // Render scene to framebuffer
        framebuffer.bind();
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        currentScene.update(dt);
        currentScene.render();
        framebuffer.unbind();

        // Render ImGui editor UI on top
        editorUI.render(dt, framebuffer.getTextureId(), currentScene);
    }

    @Override
    public void resize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        editorUI.resize(width, height);
    }

    @Override
    public void dispose() {
        if (currentScene != null) {
            currentScene.saveExit();
        }
        framebuffer.dispose();
        editorUI.dispose();
    }

    // ─── Scene switching ──────────────────────────────────
    public void changeScene(int scene) {
        switch (scene) {
            case EDITOR_SCENE:
                currentScene = new LevelEditorScene();
                break;
            case LEVEL_SCENE:
                currentScene = new LevelScene();
                break;
            default:
                currentScene = new LevelEditorScene();
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static FraynixApp get() {
        if (instance == null) instance = new FraynixApp();
        return instance;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public int getWindowWidth() { return windowWidth; }
    public int getWindowHeight() { return windowHeight; }
}
