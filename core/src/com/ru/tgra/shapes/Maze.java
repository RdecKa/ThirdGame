package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;

public class Maze {
	private int mazeWidth, mazeHeight;
	private Cell[][] maze;

	public Maze(int mazeWidth, int mazeHeight) {
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		this.maze = new Cell[mazeHeight][mazeWidth];
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				this.maze[y][x] = new Cell();
			}
		}
	}

	public void draw() {
		// Draw cells
		for (int z = 0; z < mazeHeight; z++) {
			ModelMatrix.main.loadIdentityMatrix();
			//ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0, 0, z);
			ModelMatrix.main.addScale(1, 0.1f, 1);
			for (int x = 0; x < mazeWidth; x++) {
				ModelMatrix.main.addTranslation(1, 0, 0);
				this.maze[z][x].draw();
			}
			//ModelMatrix.main.popMatrix();
		}
		// TODO: Draw walls
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
		Gdx.gl.glUniform4f(GameEnv.colorLoc, floorColor.getRed(), floorColor.getGreen(), floorColor.getBlue(), floorColor.getAlpha());
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
	}
}
