// ─── RenderBatch.java — THE BIG MIGRATION ────────────────
// BEFORE: ~200 lines of raw GL15/GL20/GL30 VAO/VBO management
// AFTER: ~100 lines using LibGDX Mesh (VAO/VBO auto-managed)
// Vertex format PRESERVED: pos[2], color[4], texCoords[2], texId[1], entityId[1]
package com.fraynix.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.fraynix.components.SpriteRenderer;
import com.fraynix.core.FraynixApp;

public class RenderBatch implements Comparable<RenderBatch> {
    // Vertex layout (IDENTICAL to original)
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;
    private static final int ENTITY_ID_SIZE = 1;
    private static final int VERTEX_SIZE = 10; // 2+4+2+1+1
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * 4; // floats

    private static final int MAX_BATCH_SIZE = 10000;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private Array<Texture> textures;
    private int zIndex;

    // LibGDX Mesh replaces raw VAO/VBO
    private Mesh mesh;
    private short[] indices;

    public RenderBatch(int maxBatchSize, int zIndex) {
        this.zIndex = zIndex;
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new Array<>();

        // Create Mesh with SAME vertex attributes as original
        this.mesh = new Mesh(
            false,                              // not static (we update vertices)
            maxBatchSize * 4,                   // max vertices
            maxBatchSize * 6,                   // max indices
            new VertexAttribute(VertexAttributes.Usage.Position, POS_SIZE, "a_position"),
            new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_SIZE, "a_color"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, TEX_COORDS_SIZE, "a_texCoord"),
            new VertexAttribute(VertexAttributes.Usage.Generic, TEX_ID_SIZE, "a_texId"),
            new VertexAttribute(VertexAttributes.Usage.Generic, ENTITY_ID_SIZE, "a_entityId")
        );

        this.indices = generateIndices(maxBatchSize);
        this.mesh.setIndices(indices);
    }

    public void start() {
        // No-op in LibGDX! Mesh constructor already set up VAO/VBO.
        // (Replaces ~20 lines of glGenVertexArrays, glBindVertexArray, etc.)
    }

    public void addSprite(SpriteRenderer spr) {
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if (spr.getTexture() != null && !textures.contains(spr.getTexture(), true)) {
            textures.add(spr.getTexture());
        }

        loadVertexProperties(index);

        if (numSprites >= sprites.length) {
            this.hasRoom = false;
        }
    }

    public void render(Shader shader, Matrix4 projectionMatrix, Matrix4 viewMatrix) {
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.isDirty()) {
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }
        }

        // Upload vertex data (replaces glBufferSubData)
        if (rebufferData || numSprites > 0) {
            mesh.setVertices(vertices, 0, numSprites * 4 * VERTEX_SIZE);
        }

        // Bind shader and upload uniforms (same as original)
        ShaderProgram program = shader.getProgram();
        program.bind();
        program.setUniformMatrix("uProjection", projectionMatrix);
        program.setUniformMatrix("uView", viewMatrix);
        program.setUniform1iv("uTextures", texSlots, 0, texSlots.length);

        // Bind textures (same as original)
        for (int i = 0; i < textures.size; i++) {
            textures.get(i).bind(i + 1);
        }

        // Draw (replaces glDrawElements)
        mesh.render(program, GL20.GL_TRIANGLES, 0, numSprites * 6);

        // Unbind
        program.unbind();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;

        Vector4 color = sprite.getColor();
        Vector2[] texCoords = sprite.getTexCoords();
        int texId = 0;

        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size; i++) {
                if (textures.get(i).equals(sprite.getTexture())) {
                    texId = i + 1;
                    break;
                }
            }
        }

        // Quad vertices (same logic as original)
        float xAdd = 1.0f, yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) yAdd = 0.0f;
            else if (i == 2) xAdd = 0.0f;
            else if (i == 3) yAdd = 1.0f;

            // Position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
            // Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;
            // Texture coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;
            // Texture ID
            vertices[offset + 8] = texId;
            // Entity ID
            vertices[offset + 9] = sprite.gameObject.getUid() + 1;

            offset += VERTEX_SIZE;
        }
    }

    private short[] generateIndices(int maxBatchSize) {
        short[] elements = new short[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            int offsetArrayIndex = 6 * i;
            short offset = (short)(4 * i);
            // Triangle 1: 3, 2, 0
            elements[offsetArrayIndex] = (short)(offset + 3);
            elements[offsetArrayIndex + 1] = (short)(offset + 2);
            elements[offsetArrayIndex + 2] = offset;
            // Triangle 2: 0, 2, 1
            elements[offsetArrayIndex + 3] = offset;
            elements[offsetArrayIndex + 4] = (short)(offset + 2);
            elements[offsetArrayIndex + 5] = (short)(offset + 1);
        }
        return elements;
    }

    public boolean hasRoom() { return this.hasRoom; }
    public boolean hasTextureRoom() { return this.textures.size < 7; } // units 1-7, see default.fs
    public boolean hasTexture(Texture tex) { return this.textures.contains(tex, true); }
    public int zIndex() { return this.zIndex; }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex());
    }

    public void dispose() {
        if (mesh != null) mesh.dispose();
    }
}
