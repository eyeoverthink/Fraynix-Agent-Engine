#version 330 core
layout (location=0) in vec2 a_position;
layout (location=1) in vec4 a_color;
layout (location=2) in vec2 a_texCoord;
layout (location=3) in float a_texId;
layout (location=4) in float a_entityId;

uniform mat4 uProjection;
uniform mat4 uView;
uniform int uTextures[8];

out vec4 f_color;
out vec2 f_texCoord;
out float f_texId;
out float f_entityId;

void main() {
    f_color = a_color;
    f_texCoord = a_texCoord;
    f_texId = a_texId;
    f_entityId = a_entityId;
    gl_Position = uProjection * uView * vec4(a_position, 0.0, 1.0);
}
