package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

import java.util.Random;
import java.util.Vector;

public class Maze {
	private int mazeWidth, mazeDepth;
	private Cell[][] maze;
	private int unit;
	private Color wallColor;
	private Vector<Wall> innerWalls;
	private static Random rand = new Random();

	public Maze(int mazeWidth, int mazeDepth) {
		this.mazeWidth = mazeWidth;
		this.mazeDepth = mazeDepth;
		this.maze = new Cell[mazeDepth][mazeWidth];
		this.unit = 1;
		for (int z = 0; z < mazeDepth; z++) {
			for (int x = 0; x < mazeWidth; x++) {
				this.maze[z][x] = new Cell(x, z, this.unit, z == mazeDepth - 1, x == mazeWidth - 1);
			}
		}
		this.wallColor = new Color(0.5f, 0.5f, 0.5f, 1);
		this.innerWalls = new Vector<Wall>();
	}

	public void draw() {
		// Draw cells
		for (int z = 0; z < mazeDepth; z++) {
			ModelMatrix.main.loadIdentityMatrix();
			//ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.5f * this.unit, 0, (z + 0.5f) * this.unit);
			ModelMatrix.main.addScale(this.unit, 0.1f * this.unit, this.unit);
			for (int x = 0; x < mazeWidth; x++) {
				this.maze[z][x].draw();
				ModelMatrix.main.addTranslation(1, 0, 0);
			}
			//ModelMatrix.main.popMatrix();
		}

		// Draw outer walls
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.wallColor.getRed(), this.wallColor.getGreen(), this.wallColor.getBlue(), this.wallColor.getAlpha());

		for (int i = 0; i < 4; i++) {
			ModelMatrix.main.loadIdentityMatrix();
			//ModelMatrix.main.pushMatrix();
			switch(i) {
				case 0:
					// (0, 0, 0) - (0, 0, 1)
					ModelMatrix.main.addTranslation(0, 0.55f * this.unit, 0.5f * this.mazeDepth * this.unit);
					ModelMatrix.main.addScale(0.1f * this.unit, this.unit, this.mazeDepth * this.unit);
					break;
				case 1:
					// (1, 0, 0) - (1, 0, 1)
					ModelMatrix.main.addTranslation(this.mazeWidth * this.unit, 0.55f * this.unit, 0.5f * this.mazeDepth * this.unit);
					ModelMatrix.main.addScale(0.1f * this.unit, this.unit, this.mazeDepth * this.unit);
					break;
				case 2:
					// (0, 0, 0) - (1, 0, 0)
					ModelMatrix.main.addTranslation(0.5f * this.mazeWidth * this.unit, 0.55f * this.unit, 0);
					ModelMatrix.main.addScale(this.mazeWidth * this.unit, this.unit, 0.1f * this.unit);
					break;
				case 3:
					// (0, 0, 1) - (1, 0, 1)
					ModelMatrix.main.addTranslation(0.5f * this.mazeWidth * this.unit, 0.55f * this.unit, this.mazeDepth * this.unit);
					ModelMatrix.main.addScale(this.mazeWidth * this.unit, this.unit, 0.1f * this.unit);
					break;

			}
			ModelMatrix.main.setShaderMatrix();
			BoxGraphic.drawSolidCube();
			//ModelMatrix.main.popMatrix();
		}

		// Draw inner walls
		for (Wall wall : this.innerWalls) {
			wall.draw(this.unit);
		}
	}

	public void addRandomWall() {
		Wall newWall = null;
		while (newWall == null) {
			int z = rand.nextInt(this.mazeDepth);
			int x = rand.nextInt(this.mazeWidth);
			newWall = maze[z][x].addWall();
		}
		this.innerWalls.add(newWall);
	}
}

class Cell {
	// northWall parallel to X, eastWall parallel to Z
	private boolean northWall, eastWall;
	private Color floorColor;
	private static Random rand = new Random();
	private int posX, posZ;
	private int unit;

	public Cell(int x, int z, int unit, boolean northRow, boolean eastRow) {
		this.floorColor = new Color();
		this.northWall = northRow;
		this.eastWall = eastRow;
		this.posX = x;
		this.posZ = z;
		this.unit = unit;
	}

	public void draw() {
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.floorColor.getRed(), this.floorColor.getGreen(), this.floorColor.getBlue(), this.floorColor.getAlpha());
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
	}

	private Wall addNorthWall() {
		this.northWall = true;
		return new Wall(this.unit * 0.1f, this.unit, true,(this.posX + 0.5f) * this.unit, (this.posZ + 1) * this.unit);
	}
	private Wall addEastWall() {
		this.eastWall = true;
		return new Wall(this.unit * 0.1f, this.unit, false,(this.posX + 1) * this.unit, (this.posZ + 0.5f) * this.unit);
	}

	// Returns true if wall was added, false otherwise
	public Wall addWall() {
		if (this.northWall && this.eastWall) {
			return null;
		}
		Wall newWall;
		if (!this.northWall && !this.eastWall) {
			if (rand.nextFloat() > 0.5) {
				newWall = addNorthWall();
			} else {
				newWall = addEastWall();
			}
		} else if (!this.northWall) {
			newWall = addNorthWall();
		} else {
			newWall = addEastWall();
		}
		return newWall;
	}
}

class Wall {
	private float wallWidth, wallLength, wallHeight;
	private boolean parallelToX;
	private float centerX, centerZ; // Central point of the wall

	public Wall(float width, float length, boolean parallelToX, float centerX, float centerZ) {
		this.wallWidth = width;
		this.wallLength = length;
		this.wallHeight = 0.5f;
		this.parallelToX = parallelToX;
		this.centerX = centerX;
		this.centerZ = centerZ;
	}

	public void draw(int unit) {
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(this.centerX * unit, (this.wallHeight / 2 + 0.05f) * unit, this.centerZ * unit);
		if (this.parallelToX) {
			ModelMatrix.main.addScale(this.wallLength * unit, this.wallHeight * unit, this.wallWidth);
		} else {
			ModelMatrix.main.addScale(this.wallWidth * unit, this.wallHeight * unit, this.wallLength);
		}
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
	}
}