#type compute
#version 430 core

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;
layout(rgb32f, binding = 0) uniform image2D colors;



void main(){

    ivec2 pos = ivec2(gl_GlobalInvocationID.xy);


    float in_val = imageLoad(out_tex, pos).r;


    imageStore(out_tex, pos, vec4(pos.x, 0.0, 0.0, 0.0));
}