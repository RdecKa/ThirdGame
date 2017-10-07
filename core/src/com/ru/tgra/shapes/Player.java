package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Player {
	public Point3D position;
	public Vector3D direction;
	private Color color;
	private float radius;

	public Player(Point3D startPosition, Vector3D startDirection) {
		this.position = startPosition;
		this.direction = startDirection;
		this.color = new Color(0, 0, 0, 1);
		this.radius = 0.4f;
	}

	public void draw() {
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha());
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y - 2 * this.radius, position.z);
		ModelMatrix.main.addScale(this.radius, this.radius, this.radius);
		ModelMatrix.main.setShaderMatrix();
		SphereGraphic.drawSolidSphere();
	}

	public void move(Vector3D moveFor, Maze maze) {
		int currentX = (int) this.position.x;
		int currentZ = (int) this.position.z;
		this.position.add(moveFor);
		int newX = (int) this.position.x;
		int newZ = (int) this.position.z;
		if (currentX != newX || currentZ != newZ) {
			maze.addRandomWalls(2);
		}
	}
}

class ThirdPerson extends Player {
	public ThirdPerson(Point3D startPosition, Vector3D startDirection) {
		super(startPosition, startDirection);
	}

	public static ThirdPerson createThirdPerson(Point3D startPosition, Point3D center) {
		Vector3D startDirection = Vector3D.difference(center, startPosition);
		return new ThirdPerson(startPosition, startDirection);
	}
}