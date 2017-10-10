package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import sun.security.provider.SHA;

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

	public void draw(Shader3D shader, Camera camera) {
		//Gdx.gl.glUniform4f(GameEnv.colorLoc, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha());
		shader.setColor(this.color);
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(position.x, position.y - 2 * this.radius, position.z);
		ModelMatrix.main.addScale(this.radius, this.radius, this.radius);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		SphereGraphic.drawSolidSphere();
	}

	public boolean move(Vector3D moveForOrig, Maze maze) {
		Vector3D moveFor = moveForOrig.clone();
		int currentX = (int) this.position.x;
		int currentZ = (int) this.position.z;

		if (currentX == maze.getMazeWidth() - 1 && currentZ == maze.getMazeDepth() - 1) {
			return true;
		}

		float wallWidth = maze.getWallWidth() / 2;

		// Check hitting walls directly
		if (moveFor.x >= 0 && this.position.x + this.radius > (currentX + 1 - wallWidth) && maze.hasEastWall(currentX, currentZ)) {
			moveFor.x = 0;
		} else if (moveFor.x <= 0 && this.position.x - this.radius < (currentX + wallWidth) && maze.hasEastWall(currentX - 1, currentZ)) {
			moveFor.x = 0;
		}
		if (moveFor.z >= 0 && this.position.z + this.radius > (currentZ + 1 - wallWidth) && maze.hasNorthWall(currentX, currentZ)) {
			moveFor.z = 0;
		} else if (moveFor.z <= 0 && this.position.z - this.radius < (currentZ + wallWidth) && maze.hasNorthWall(currentX, currentZ - 1)) {
			moveFor.z = 0;
		}

		// Check hitting walls from the side
		if (moveFor.x > 0 && this.position.x + this.radius * 0.95f > (currentX + 1) && this.position.z < (currentZ + wallWidth) && maze.hasNorthWall(currentX + 1, currentZ - 1) ||
			moveFor.x > 0 && this.position.x + this.radius * 0.95f > (currentX + 1) && this.position.z > (currentZ + 1 - wallWidth) && maze.hasNorthWall(currentX + 1, currentZ) ||
			moveFor.x < 0 && this.position.x - this.radius * 0.95f < currentX && this.position.z < (currentZ + wallWidth) && maze.hasNorthWall(currentX - 1, currentZ - 1) ||
			moveFor.x < 0 && this.position.x - this.radius * 0.95f < currentX && this.position.z > (currentZ + 1 - wallWidth) && maze.hasNorthWall(currentX - 1, currentZ) ||
			moveFor.x > 0 && maze.hasNorthWall(currentX + 1, currentZ - 1) && this.position.getXZDistanceTo(new Point3D(currentX + 1, 0, currentZ + wallWidth)) < this.radius * 0.95f ||
			moveFor.x > 0 && maze.hasNorthWall(currentX + 1, currentZ) && this.position.getXZDistanceTo(new Point3D(currentX + 1, 0, currentZ + 1 - wallWidth)) < this.radius * 0.95f ||
			moveFor.x < 0 && maze.hasNorthWall(currentX - 1, currentZ - 1) && this.position.getXZDistanceTo(new Point3D(currentX, 0, currentZ + wallWidth)) < this.radius * 0.95f ||
			moveFor.x < 0 && maze.hasNorthWall(currentX - 1, currentZ) && this.position.getXZDistanceTo(new Point3D(currentX, 0, currentZ + 1 - wallWidth)) < this.radius * 0.95f) {
			moveFor.x = 0;
		}

		if (moveFor.z > 0 && this.position.z + this.radius * 0.95f > (currentZ + 1) && this.position.x < (currentX + wallWidth) && maze.hasEastWall(currentX - 1, currentZ + 1) ||
			moveFor.z > 0 && this.position.z + this.radius * 0.95f > (currentZ + 1) && this.position.x > (currentX + 1 - wallWidth) && maze.hasEastWall(currentX, currentZ + 1) ||
			moveFor.z < 0 && this.position.z - this.radius * 0.95f < currentZ && this.position.x < (currentX + wallWidth) && maze.hasEastWall(currentX - 1, currentZ - 1) ||
			moveFor.z < 0 && this.position.z - this.radius * 0.95f < currentZ && this.position.x > (currentX + 1 - wallWidth) && maze.hasEastWall(currentX, currentZ - 1) ||
			moveFor.z > 0 && maze.hasEastWall(currentX -1, currentZ + 1) && this.position.getXZDistanceTo(new Point3D(currentX + wallWidth, 0, currentZ + 1)) < this.radius * 0.95f ||
			moveFor.z > 0 && maze.hasEastWall(currentX, currentZ + 1) && this.position.getXZDistanceTo(new Point3D(currentX + 1 - wallWidth, 0, currentZ + 1)) < this.radius * 0.95f ||
			moveFor.z < 0 && maze.hasEastWall(currentX - 1, currentZ - 1) && this.position.getXZDistanceTo(new Point3D(currentX + wallWidth, 0, currentZ)) < this.radius * 0.95f ||
			moveFor.z < 0 && maze.hasEastWall(currentX, currentZ - 1) && this.position.getXZDistanceTo(new Point3D(currentX + 1 - wallWidth, 0, currentZ)) < this.radius * 0.95f) {
			moveFor.z = 0;
		}

		this.position.add(moveFor);
		int newX = (int) this.position.x;
		int newZ = (int) this.position.z;
		if (currentX != newX || currentZ != newZ) {
			maze.addRandomWalls(LabFirst3DGame.numWallsAtOnce, newX, newZ);
		}

		return false;
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