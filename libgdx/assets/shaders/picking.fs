#version 330 core
in float f_entityId;
out vec4 fragColor;

void main() {
    int r = int(f_entityId);
    int g = 0;
    int b = 0;
    fragColor = vec4(r / 255.0, g / 255.0, b / 255.0, 1.0);
}
