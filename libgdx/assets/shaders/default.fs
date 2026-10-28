#version 330 core
in vec4 f_color;
in vec2 f_texCoord;
in float f_texId;
in float f_entityId;

uniform int uTextures[8];

out vec4 fragColor;

void main() {
    vec4 texColor = vec4(1.0);
    if (f_texId > 0) {
        int texId = int(f_texId - 1);
        texColor = texture(uTextures[texId], f_texCoord);
    }
    fragColor = f_color * texColor;
}
