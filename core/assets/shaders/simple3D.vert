
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

uniform vec4 u_lightPosition; // In global coordinates
//uniform vec3 u_useLight; // (diffuse, specular, ambient)

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;

	//v_color = (dot(normal, vec4(0,0,1,0)) / length(normal)) * u_color;

	// Global coordinates
	//  Ligthing

	v_normal = normal;
    if (u_lightPosition[3] == 1.0) {
    	v_s = u_lightPosition - position; // Direction to the light
    } else {
        v_s = vec4(u_lightPosition.x, u_lightPosition.y, u_lightPosition.z, 0);
    }
    vec4 v = u_eyePosition - position; // Direction to the camera

	v_h = v_s + v;

	position = u_viewMatrix * position;

	gl_Position = u_projectionMatrix * position;
}