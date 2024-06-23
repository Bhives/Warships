#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_maskTexture;
uniform vec2 u_circleCenter;
uniform float u_circleRadius;

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float dist = distance(gl_FragCoord.xy, u_circleCenter);
    if (dist < u_circleRadius) {
        vec2 maskTexCoords = v_texCoords;
        texColor = texture2D(u_maskTexture, maskTexCoords);
    }
    gl_FragColor = texColor;
}