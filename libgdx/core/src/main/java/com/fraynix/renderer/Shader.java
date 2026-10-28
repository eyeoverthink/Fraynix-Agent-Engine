// ─── Shader.java (wraps LibGDX ShaderProgram) ───────────
package com.fraynix.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class Shader {
    private ShaderProgram program;
    private String filepath;
    private boolean beingUsed = false;

    public Shader(String filepath) {
        this.filepath = filepath;
        String vertexSource = Gdx.files.internal(filepath + ".vs").readString();
        String fragmentSource = Gdx.files.internal(filepath + ".fs").readString();
        this.program = new ShaderProgram(vertexSource, fragmentSource);
        if (!program.isCompiled()) {
            throw new RuntimeException("Shader compile error: " + program.getLog());
        }
    }

    public void use() { beingUsed = true; }
    public void detach() { beingUsed = false; }
    public ShaderProgram getProgram() { return program; }

    public void uploadMat4f(String varName, Matrix4 mat4) {
        program.setUniformMatrix(varName, mat4);
    }

    public void uploadIntArray(String varName, int[] array) {
        program.setUniform1iv(varName, array, 0, array.length);
    }

    public void uploadTexture(String varName, int slot) {
        program.setUniformi(varName, slot);
    }

    public void bind() {
        program.bind();
    }

    public void dispose() {
        if (program != null) program.dispose();
    }
}
