#type vertex
#version 430 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aChemicalValues;

uniform ivec2 uResolution;

out vec2 chemicalValues;

void main(){
    chemicalValues= aChemicalValues;
    gl_Position = vec4((2.0 * aPos.x / uResolution.x) - 1.0, (2.0 * aPos.y / uResolution.y) - 1.0, 0, 1);
}

#type fragment
#version 430 core

in vec2 chemicalValues;

out vec4 color;

uniform vec3 chemicalAColor;
uniform vec3 chemicalBColor;

void main(){
    vec3 rgbColor = chemicalValues.r * chemicalAColor + chemicalValues.g * chemicalBColor;

    color = vec4(rgbColor, 1.0);

}
