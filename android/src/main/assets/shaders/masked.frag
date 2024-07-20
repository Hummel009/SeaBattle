#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform vec2 u_maskPosition;
uniform vec2 u_maskScale;

void main()
{
    vec2 normalizedTexCoords = (gl_FragCoord.xy - u_maskPosition + u_maskScale / 2.0) / u_maskScale;

    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec4 maskColor = texture2D(u_mask, normalizedTexCoords);

    texColor.a *= maskColor.a;

    gl_FragColor = v_color * texColor;
}