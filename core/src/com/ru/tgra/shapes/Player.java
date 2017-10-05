package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Player {
	public Point3D position;
	public Vector3D direction;
	private Color color;

	public Player(Point3D startPosition, Vector3D startDirection) {
		this.position = startPosition;
		this.direction = startDirection;
		this.color = new Color(0, 0, 0, 1);
	}

	public void draw() {
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha());
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawSolidSphere();
	}
}
