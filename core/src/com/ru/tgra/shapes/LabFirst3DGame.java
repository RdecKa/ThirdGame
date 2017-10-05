package com.ru.tgra.shapes;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	OrtographicCamera ortCamera;
	PerspectiveCamera perspCamera;
	Maze maze;
	int mazeWidth, mazeDepth;
	Point3D playerPos;
	Vector3D playerDir, lookDown;

	Vector3D moveLeft, moveForward, up;

	boolean firstPersonView;

	int mouseX, mouseY;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		GameEnv.init();

		mazeWidth = 20;
		mazeDepth = 10;
		maze = new Maze(mazeWidth, mazeDepth);
		playerPos = new Point3D(0.5f, 1, 0.5f);
		playerDir = new Vector3D(1, 0, 1);
		lookDown = new Vector3D(0, -0.5f, 0);

		moveLeft = new Vector3D(1, 0, 0);
		moveForward = new Vector3D(0, 0, 1);
		up = new Vector3D(0,1,0);

		firstPersonView = true;

		perspCamera = new PerspectiveCamera();

		Point3D mapCameraEye = new Point3D((float)(mazeWidth/2.0), 10, (float)(mazeDepth/2.0));
		Point3D mapCameraCenter = mapCameraEye.clone().returnAddedVector(new Vector3D(0, -5, 0));
		System.out.println(mapCameraEye);
		ortCamera = new OrtographicCamera();
		ortCamera.Look3D(mapCameraEye, mapCameraCenter, new Vector3D(0,0,1));
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			firstPersonView = !firstPersonView;
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
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (firstPersonView) {
			perspCamera.setPerspectiveProjection(60, 9.0f/16.0f, 0.1f, 100);
			perspCamera.setShaderMatrix();
		} else {

		}

		maze.draw(true);

		// Draw a map
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int mapWidth = mazeWidth * 10;
		int mapHeight = mazeDepth * 10;
		int margin = 10;
		Gdx.gl20.glViewport(screenWidth - mapWidth - margin, screenHeight - mapHeight - margin, mapWidth, mapHeight);

		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		ortCamera.setOrtographicProjection(-mazeWidth * 0.5f, mazeWidth * 0.5f, -mazeDepth * 0.5f, mazeDepth * 0.5f, 1, 10);
		ortCamera.setShaderMatrix();
		maze.draw(false);
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