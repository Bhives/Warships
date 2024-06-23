#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_circleCenter;
uniform float u_circleRadius;
uniform vec4 u_maskColor;

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float dist = distance(gl_FragCoord.xy, u_circleCenter);
    if (dist < u_circleRadius) {
        texColor = u_maskColor;
    }
    gl_FragColor = texColor;
}