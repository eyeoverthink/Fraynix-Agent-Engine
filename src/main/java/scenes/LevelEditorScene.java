package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;


import physics2d.PhysicsSystem2D;
import physics2d.rigidbody.Rigidbody2D;
import renderer.DebugDraw;
import util.AssetPool;


// TODO : JARVIS :

// todo: Jarvis is my Handler - I need to incorporate camera functions and feedback of items picked up. Maybe the real time option to group?

public class LevelEditorScene extends Scene {

    GameObject levelEditorStuff = new GameObject("Level Editor", new Transform(new Vector2f()), 0);
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -10));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;
    private Spritesheet sprites;
    SpriteRenderer obj1Sprite;


    public LevelEditorScene() {

    }

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpritesheet("assets/tilemap_packed.png");



        // todo : this is our test Debug Draw

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/tilemap_packed.png",
                new Spritesheet(AssetPool.getTexture("assets/tilemap_packed.png"),
                        16, 16, 486, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");

        for (GameObject g : gameObjects){
            if (g.getComponent(SpriteRenderer.class) != null){
               SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
               if (spr.getTexture() != null){
                   spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
               }
            }
        }
    }


    @Override
    public void update(float dt) {
        levelEditorStuff.update(dt);
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
//        DebugDraw.addBox2D(obj1.position, new Vector2f(32, 32), 0.0f, new Vector3f(1, 0, 0));
//        DebugDraw.addBox2D(obj2.position, new Vector2f(32, 32), 0.0f, new Vector3f(0.2f, 0.8f, 0.1f));
//        physics.update(dt);

        this.renderer.render();
    }
    @Override
    public void render(){
        this.renderer.render();
    }

    @Override
    public void imgui() {
        // todo : test code for creating a window with text
        ImGui.begin("Level Editor");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i ++){
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight,
                    texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)){
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                // todo : attach our newly created game object to the mouse cursor for drag n drop
// todo :                 // Our Level Editor  Controls :
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);

            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if (i + 1 < sprites.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }

        }
        ImGui.end();
    }
}
