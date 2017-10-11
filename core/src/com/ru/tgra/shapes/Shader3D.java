package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

public class Shader3D {
	public static int renderingProgramID;
	public static int vertexShaderID;
	public static int fragmentShaderID;

	public static int positionLoc;
	public static int normalLoc;

	public static int modelMatrixLoc;
	public static int viewMatrixLoc;
	public static int projectionMatrixLoc;

	public static int eyePosLoc;
	//public static int useLightLoc;
	public static int globalAmbient;

	//public static int colorLoc;
	public static int lightPosLoc;
	public static int lightColLoc;
	public static int matDiffLoc;
	public static int matSpecLoc;
	public static int matShinLoc;

	public Shader3D() {
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		System.out.println("ERROR: " + Gdx.gl.glGetError());
		System.out.println("INFOLOG vetrtex:\n" + Gdx.gl.glGetShaderInfoLog(vertexShaderID));
		System.out.println("INFOLOG fragment:\n" + Gdx.gl.glGetShaderInfoLog(fragmentShaderID));

		renderingProgramID = Gdx.gl.glCreateProgram();

		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");
		//useLightLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_useLight");
		globalAmbient 			= Gdx.gl.glGetUniformLocation(renderingProgramID, "globalAmbient");

		//colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");
		lightPosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");
		lightColLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightColor");
		matDiffLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		matSpecLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		matShinLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");

		Gdx.gl.glUseProgram(renderingProgramID);
	}

	/*public void setColor(Color c) {
		Gdx.gl.glUniform4f(colorLoc, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}*/

	public void setLightPosition(Point3D p) { Gdx.gl.glUniform4f(lightPosLoc, p.x, p.y, p.z, 1.0f); }
	public void setLightDirection(Vector3D v)  {
		Gdx.gl.glUniform4f(lightPosLoc, v.x, v.y, v.z, 0.0f);
	}
	public void setEyePosition(Point3D p) {
		Gdx.gl.glUniform4f(eyePosLoc, p.x, p.y, p.z, 1.0f);
	}

	/*public void setLightsUsed(boolean diff, boolean spec, boolean amb) {
		float diffF = diff ? 1 : 0;
		float specF = spec ? 1 : 0;
		float ambF = amb ? 1 : 0;
		System.out.println(diffF + " " +  specF + " " + ambF);
		Gdx.gl.glUniform3f(useLightLoc, diffF, specF, ambF);
	}*/

	public void setGlobalAmbient(Color c) {
		Gdx.gl.glUniform4f(globalAmbient, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public void setLightColor(Color c) {
		Gdx.gl.glUniform4f(lightColLoc, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public void setMaterialDiffuse(Color c) {
		Gdx.gl.glUniform4f(matDiffLoc, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public void setMaterialSpecular(Color c) {
		Gdx.gl.glUniform4f(matSpecLoc, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public void setShininess(float shine) {
		Gdx.gl.glUniform1f(matShinLoc, shine);
	}

	public int getVertexPointer() {
		return positionLoc;
	}

	public int getNormalPointer() {
		return normalLoc;
	}

	public void setModelMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
	}

	public void setViewMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
	}

	public void setProjectionMatrix(FloatBuffer matrix) {
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
	}
}
