// ─── SceneSerializer.java (replaces Gson with LibGDX Json) ──
package com.fraynix.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.fraynix.core.Component;
import com.fraynix.core.GameObject;
import com.fraynix.core.Transform;
import com.fraynix.components.SpriteRenderer;
import com.fraynix.renderer.Texture;
import com.fraynix.scenes.Scene;

public class SceneSerializer {

    // Save scene to JSON file (replaces Gson)
    public static void save(Array<GameObject> gameObjects, String filepath) {
        Json json = new Json();
        json.setUsePrototypes(false);
        json.setOutputType(Json.OutputType.json);

        Array<SerializedGameObject> serialized = new Array<>();
        int maxUid = 0;

        for (GameObject go : gameObjects) {
            SerializedGameObject sgo = new SerializedGameObject();
            sgo.name = go.getName();
            sgo.uid = go.getUid();
            sgo.zIndex = go.zIndex;
            sgo.transform = new SerializedTransform(
                go.transform.position.x, go.transform.position.y,
                go.transform.scale.x, go.transform.scale.y
            );
            maxUid = Math.max(maxUid, sgo.uid);

            // Serialize components
            for (Component c : go.getComponents()) {
                if (c instanceof SpriteRenderer) {
                    SpriteRenderer spr = (SpriteRenderer) c;
                    SerializedComponent sc = new SerializedComponent();
                    sc.type = "SpriteRenderer";
                    if (spr.getTexture() != null) {
                        sc.texturePath = spr.getTexture().getFilepath();
                    }
                    Vector4 col = spr.getColor();
                    sc.color = new float[]{col.x, col.y, col.z, col.w};
                    sgo.components.add(sc);
                }
                // Add other component types here
            }
            serialized.add(sgo);
        }

        // Reset ID counter
        GameObject.init(maxUid + 1);

        String jsonStr = json.prettyPrint(serialized);
        Gdx.files.local(filepath).writeString(jsonStr, false);
    }

    // Load scene from JSON file
    public static void load(String filepath, Scene scene) {
        FileHandle file = Gdx.files.local(filepath);
        if (!file.exists()) {
            System.out.println("Scene file not found: " + filepath + " — starting fresh");
            return;
        }

        Json json = new Json();
        Array<SerializedGameObject> serialized = json.fromJson(
            Array.class, SerializedGameObject.class, file
        );

        int maxUid = 0;
        for (SerializedGameObject sgo : serialized) {
            GameObject go = new GameObject(sgo.name);
            go.zIndex = sgo.zIndex;
            go.transform.position.set(sgo.transform.x, sgo.transform.y);
            go.transform.scale.set(sgo.transform.scaleX, sgo.transform.scaleY);
            maxUid = Math.max(maxUid, sgo.uid);

            for (SerializedComponent sc : sgo.components) {
                if (sc.type.equals("SpriteRenderer")) {
                    SpriteRenderer spr;
                    if (sc.texturePath != null && !sc.texturePath.isEmpty()) {
                        spr = new SpriteRenderer(AssetPool.getTexture(sc.texturePath));
                    } else {
                        spr = new SpriteRenderer();
                    }
                    if (sc.color != null && sc.color.length >= 4) {
                        spr.setColor(new Vector4(sc.color[0], sc.color[1], sc.color[2], sc.color[3]));
                    }
                    go.addComponent(spr);
                }
            }
            scene.addGameObject(go);
        }

        GameObject.init(maxUid + 1);
    }

    // ─── Serialization DTOs ───────────────────────────────
    public static class SerializedGameObject {
        public String name;
        public int uid;
        public int zIndex;
        public SerializedTransform transform;
        public Array<SerializedComponent> components = new Array<>();
    }

    public static class SerializedTransform {
        public float x, y, scaleX, scaleY;
        public SerializedTransform() {}
        public SerializedTransform(float x, float y, float sx, float sy) {
            this.x = x; this.y = y; this.scaleX = sx; this.scaleY = sy;
        }
    }

    public static class SerializedComponent {
        public String type;
        public String texturePath;
        public float[] color;
    }
}
