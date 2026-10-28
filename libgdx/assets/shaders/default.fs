#version 330 core
in vec4 f_color;
in vec2 f_texCoord;
in float f_texId;
in float f_entityId;

// RenderBatch binds texture i to unit i + 1 and writes texId = i + 1 (0 = no texture).
// GLSL 330 only allows constant indices into sampler arrays, so each slot is spelled out.
uniform sampler2D uTextures[8];

out vec4 fragColor;

void main() {
    vec4 texColor = vec4(1.0);
    switch (int(f_texId + 0.5)) {
        case 1: texColor = texture(uTextures[1], f_texCoord); break;
        case 2: texColor = texture(uTextures[2], f_texCoord); break;
        case 3: texColor = texture(uTextures[3], f_texCoord); break;
        case 4: texColor = texture(uTextures[4], f_texCoord); break;
        case 5: texColor = texture(uTextures[5], f_texCoord); break;
        case 6: texColor = texture(uTextures[6], f_texCoord); break;
        case 7: texColor = texture(uTextures[7], f_texCoord); break;
    }
    fragColor = f_color * texColor;
}
