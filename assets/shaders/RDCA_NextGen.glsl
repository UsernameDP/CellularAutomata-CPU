#type compute
#version 430 core

layout(local_size_x = 1, local_size_y = 1, local_size_z = 1) in;
layout(rg32f, binding = 0) uniform image2D out_tex;

void main(){

    ivec2 pos = ivec2(gl_GlobalInvocationID.xy);


    vec2 in_val = imageLoad(out_tex, pos).rg;


    imageStore(out_tex, pos, vec4(pos.x, pos.y, 0.0, 0.0));
}




