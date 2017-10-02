package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;

public class OrtographicCamera extends Camera {
	public void OrthographicProjection3D() {
		float[] pm = new float[16];

		pm[0] = 2.0f / (right - left); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -(right + left) / (right - left);
		pm[1] = 0.0f; pm[5] = 2.0f / (top - bottom); pm[9] = 0.0f; pm[13] = -(top + bottom) / (top - bottom);
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 2.0f / (near - far); pm[14] = (near + far) / (near - far);
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		GameEnv.matrixBuffer = BufferUtils.newFloatBuffer(16);
		GameEnv.matrixBuffer.put(pm);
		GameEnv.matrixBuffer.rewind();
		Gdx.gl.glUniformMatrix4fv(GameEnv.projectionMatrixLoc, 1, false, GameEnv.matrixBuffer);
	}

	public void setOrtographicProjection(float left, float right, float bottom, float top, float near, float far) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.near = near;
		this.far = far;
	}

	@Override
	protected void setProjection() {
		OrthographicProjection3D();
	}
}
