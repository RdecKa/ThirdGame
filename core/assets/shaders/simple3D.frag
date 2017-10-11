
#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 globalAmbient;

uniform vec4 u_lightColor;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShininess;

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

void main()
{
    float lambert = dot(v_normal, v_s) / (length(v_normal) * length(v_s));
	float phong = dot(v_normal, v_h) / (length(v_normal) * length(v_h));

	vec4 diffuseColor = lambert * u_lightColor * u_materialDiffuse;
    vec4 specularColor = pow(phong, u_materialShininess) * u_lightColor * vec4(0.3, 0.3, 0.3, 1);
    //vec4 specularColor = vec4(0, 0, 0, 0);
    vec4 light1 = diffuseColor + specularColor;

	gl_FragColor = globalAmbient * u_materialDiffuse + light1;
}