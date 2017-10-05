package com.ru.tgra.shapes;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	OrtographicCamera ortCamera;
	PerspectiveCamera perspCamera;
	Maze maze;
	Point3D playerPos;
	Vector3D playerDir, lookDown;

	Vector3D moveLeft, moveForward, up;

	int mouseX, mouseY;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		GameEnv.init();

		maze = new Maze(5, 8);
		playerPos = new Point3D(0.5f, 1, 0.5f);
		playerDir = new Vector3D(1, 0, 1);
		lookDown = new Vector3D(0, -0.5f, 0);

		moveLeft = new Vector3D(1, 0, 0);
		moveForward = new Vector3D(0, 0, 1);
		up = new Vector3D(0,1,0);

		perspCamera = new PerspectiveCamera();

		//ortCamera = new OrtographicCamera();
		//ortCamera.Look3D(new Point3D(1, 1, 1.5f), new Point3D(2, 2, 1.5f), new Vector3D(0,0,1));
	}

	private void input(float deltaTime)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerPos.add(moveLeft.returnScaled(deltaTime));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerPos.add(moveLeft.returnScaled(- deltaTime));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerPos.add(moveForward.returnScaled(deltaTime));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerPos.add(moveForward.returnScaled(- deltaTime));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			playerDir.rotateXZ(-100 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			playerDir.rotateXZ(100 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			lookDown.y += deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			lookDown.y -= deltaTime;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
			maze.addRandomWall();
		}
	}
	
	private void update(float deltaTime)
	{
		//System.out.println(playerPos);
		Point3D center = playerPos.returnAddedVector(playerDir).returnAddedVector(lookDown);
		moveForward = playerDir;
		moveLeft = up.cross(moveForward);
		maze.raiseWalls(deltaTime);
		perspCamera.Look3D(playerPos, center, up);
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		perspCamera.setPerspectiveProjection(60, 9.0f/16.0f, 0.1f, 100);
		perspCamera.setShaderMatrix();
		//ortCamera.setOrtographicProjection(-10, 10, -10, 10, -10, 100);
		//ortCamera.setShaderMatrix();

		Gdx.gl.glUniform4f(GameEnv.colorLoc, 0.9f, 0.3f, 0.1f, 1.0f);

		maze.draw();
	}

	@Override
	public void render () {

		float deltaTime = Gdx.graphics.getDeltaTime();
		
		input(deltaTime);
		//put the code inside the update and display methods, depending on the nature of the code
		update(deltaTime);
		display();

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}