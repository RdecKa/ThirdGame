package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Maze {
	private int mazeWidth, mazeDepth;
	private Cell[][] maze;
	private int unit;
	private Color wallColor;

	public Maze(int mazeWidth, int mazeDepth) {
		this.mazeWidth = mazeWidth;
		this.mazeDepth = mazeDepth;
		this.maze = new Cell[mazeDepth][mazeWidth];
		for (int z = 0; z < mazeDepth; z++) {
			for (int x = 0; x < mazeWidth; x++) {
				this.maze[z][x] = new Cell();
			}
		}
		this.unit = 1;
		this.wallColor = new Color(0.5f, 0.5f, 0.5f, 1);
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

		// Draw walls
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.wallColor.getRed(), this.wallColor.getGreen(), this.wallColor.getBlue(), this.wallColor.getAlpha());

		for (int i = 0; i < 4; i++) {
			ModelMatrix.main.loadIdentityMatrix();
			ModelMatrix.main.pushMatrix();
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
			ModelMatrix.main.popMatrix();
		}
	}
}

class Cell {
	private Color floorColor;
	private boolean northWall, eastWall;

	public Cell() {
		this.floorColor = new Color();
		this.northWall = false;
		this.eastWall = false;
	}

	public void draw() {
		Gdx.gl.glUniform4f(GameEnv.colorLoc, this.floorColor.getRed(), this.floorColor.getGreen(), this.floorColor.getBlue(), this.floorColor.getAlpha());
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
	}
}
