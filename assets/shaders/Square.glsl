#type vertex
#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aColor;

uniform vec2 uResolution;
out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = vec4((2.0 * aPos.x / uResolution.x) - 1.0, (2.0 * aPos.y / uResolution.y) - 1.0, 0, 1);
}

#type fragment
#version 330 core

in vec4 fColor;

out vec4 color;

//uniform float uTime;

void main(){
    //    float noise = fract(sin(dot(fColor.xy ,vec2(12.9898,78.233))) * (43758.5453) );
    color = fColor;
}