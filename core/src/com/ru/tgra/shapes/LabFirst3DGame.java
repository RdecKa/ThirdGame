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
	Point3D mapCenter;

	Vector3D moveLeft, moveForward, up, lookDown, moveFor;

	boolean firstPersonView;
	float fovProjection;

	Player player, thirdPerson;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		GameEnv.init();

		mazeWidth = 10;
		mazeDepth = 7;
		maze = new Maze(mazeWidth, mazeDepth);

		player = new Player(new Point3D(0.5f, 0.8f, 0.5f), new Vector3D(1, 0, 1));

		lookDown = new Vector3D(0, -0.5f, 0);
		moveLeft = new Vector3D(1, 0, 0);
		moveForward = new Vector3D(0, 0, 1);
		up = new Vector3D(0,1,0);
		moveFor = new Vector3D(0, 0, 0);

		firstPersonView = true;

		perspCamera = new PerspectiveCamera();
		fovProjection = 130;

		Point3D mapCameraEye = new Point3D((float)(mazeWidth/2.0), 10, (float)(mazeDepth/2.0));
		mapCenter = mapCameraEye.clone().returnAddedVector(new Vector3D(0, -5, 0));
		Point3D mapCameraCenter = mapCenter;
		ortCamera = new OrtographicCamera();
		ortCamera.Look3D(mapCameraEye, mapCameraCenter, new Vector3D(0,0,1));

		thirdPerson = ThirdPerson.createThirdPerson(mapCameraCenter.returnAddedVector(new Vector3D(-10, 10, 0)), mapCameraCenter);
	}

	private void input(float deltaTime)
	{
		if (firstPersonView) {
			moveFor = new Vector3D(0, 0, 0);
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				moveFor.add(moveLeft);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				moveFor.add(moveLeft.returnScaled(-1));
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				moveFor.add(moveForward);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				moveFor.add(moveForward.returnScaled(-1));
			}
			moveFor.scale(deltaTime);
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player.direction.rotateXZ(-100 * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				player.direction.rotateXZ(100 * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				lookDown.y += deltaTime;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				lookDown.y -= deltaTime;
			}
		} else {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				thirdPerson.position.rotateAroundPoint(mapCenter, 100 * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				thirdPerson.position.rotateAroundPoint(mapCenter, -100 * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				fovProjection -= 100 * deltaTime;
				if (fovProjection < 10)
					fovProjection = 10;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				fovProjection += 100 * deltaTime;
				if (fovProjection > 130)
					fovProjection = 130;
			}
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
		if (firstPersonView) {
			player.move(moveFor, maze);
			fovProjection = 60;
			Point3D center = player.position.returnAddedVector(player.direction).returnAddedVector(lookDown);
			moveForward = player.direction;
			moveLeft = up.cross(moveForward);
			perspCamera.Look3D(player.position, center, up);
		} else {
			Point3D mapCenter = new Point3D(mazeWidth / 2, 1, mazeDepth / 2);
			perspCamera.Look3D(thirdPerson.position, mapCenter, new Vector3D(0, 1, 0));
		}
		maze.raiseWalls(deltaTime);
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		perspCamera.setPerspectiveProjection(fovProjection, 1, 0.1f, 100);
		perspCamera.setShaderMatrix();

		maze.draw(true);

		if (!firstPersonView)
			player.draw();

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
		player.draw();
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