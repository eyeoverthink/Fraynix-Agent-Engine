#version 330 core
layout (location=0) in vec2 a_position;
layout (location=1) in float a_entityId;

uniform mat4 uProjection;
uniform mat4 uView;

out float f_entityId;

void main() {
    f_entityId = a_entityId;
    gl_Position = uProjection * uView * vec4(a_position, 0.0, 1.0);
}
