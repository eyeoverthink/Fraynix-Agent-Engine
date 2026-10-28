// ─── DebugDraw.java (ShapeRenderer replaces raw GL lines) ─
package com.fraynix.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class DebugDraw {
    private static ShapeRenderer shapeRenderer;

    public static void init() {
        // macOS runs a core-profile context, where LibGDX's built-in ShapeRenderer
        // shader needs a GLSL 150 header. It is set only while that shader compiles,
        // so the engine's own #version 330 shaders are unaffected.
        boolean macCore = Gdx.gl30 != null && System.getProperty("os.name").toLowerCase().contains("mac");
        if (macCore) {
            ShaderProgram.prependVertexCode = "#version 150\n#define attribute in\n#define varying out\n";
            ShaderProgram.prependFragmentCode = "#version 150\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";
        }
        shapeRenderer = new ShapeRenderer();
        if (macCore) {
            ShaderProgram.prependVertexCode = "";
            ShaderProgram.prependFragmentCode = "";
        }
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
