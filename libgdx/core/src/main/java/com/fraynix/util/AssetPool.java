// ─── AssetPool.java (texture/shader cache — mostly unchanged) ─
package com.fraynix.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.fraynix.renderer.Shader;
import com.fraynix.renderer.Texture;

public class AssetPool {
    private static ObjectMap<String, Shader> shaders = new ObjectMap<>();
    private static ObjectMap<String, Texture> textures = new ObjectMap<>();

    public static Shader getShader(String filepath) {
        if (shaders.containsKey(filepath)) {
            return shaders.get(filepath);
        }
        Shader shader = new Shader(filepath);
        shaders.put(filepath, shader);
        return shader;
    }

    public static Texture getTexture(String filepath) {
        if (textures.containsKey(filepath)) {
            return textures.get(filepath);
        }
        Texture texture = new Texture(filepath);
        textures.put(filepath, texture);
        return texture;
    }

    public static void dispose() {
        for (Shader s : shaders.values()) s.dispose();
        for (Texture t : textures.values()) t.dispose();
        shaders.clear();
        textures.clear();
    }
}
