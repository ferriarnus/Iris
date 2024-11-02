#version 150

in vec3 Position;
in vec4 gl_Color;
in vec2 vaUV0;
//in ivec2 vaUV1;
//in ivec2 vaUV2;
in vec3 vaNormal;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 IViewRotMat;
uniform int FogShape;

uniform vec3 Light0_Direction;
uniform vec3 Light1_Direction;

out float vertexDistance;
out vec4 vertexColor;
out vec4 shadedVertexColor;
out vec4 lightMapColor;
out vec4 overlayColor1;
out vec2 texCoord0;
out vec4 normal;


vec4 minecraft_mix_light(vec3 lightDir0, vec3 lightDir1, vec3 normal, vec4 color) {
    lightDir0 = normalize(lightDir0);
    lightDir1 = normalize(lightDir1);
    float light0 = max(0.0, dot(lightDir0, normal));
    float light1 = max(0.0, dot(lightDir1, normal));
    float lightAccum = min(1.0, (light0 + light1) * 0.6 + 0.4);
    return vec4(color.rgb * lightAccum, color.a);
}

float fog_distance(mat4 modelViewMatrix, vec3 pos, int shape) {
if (shape == 0) {
return length((modelViewMatrix * vec4(pos, 1.0)).xyz);
} else {
float distXZ = length((modelViewMatrix * vec4(pos.x, 0.0, pos.z, 1.0)).xyz);
float distY = length((modelViewMatrix * vec4(0.0, pos.y, 0.0, 1.0)).xyz);
return max(distXZ, distY);
}
}

void main() {
    //Like rendertype_entity_cutout except we calculate vertex colors for passed in and for non recoloring
    gl_Position = projectionMatrix * modelViewMatrix * vec4(Position, 1.0);
    //Calculate theactual tint to apply based on the passed in alpha value
    vec4 tint = vec4(mix(vec4(1,1,1,1).rgb, gl_Color.rgb, gl_Color.a), vec4(1,1,1,1).a);

    vertexDistance = fog_distance(modelViewMatrix, IViewRotMat * Position, FogShape);
    vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, vaNormal, vec4(1,1,1,1));
    shadedVertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, vaNormal, tint);
    lightMapColor = texelFetch(Sampler2, ivec2(16*vaUV2), 0);
    overlayColor1 = texelFetch(Sampler1, ivec2(16*vaUV1), 0);
    texCoord0 = vaUV0;
    normal = projectionMatrix * modelViewMatrix * vec4(vaNormal, 0.0);
}