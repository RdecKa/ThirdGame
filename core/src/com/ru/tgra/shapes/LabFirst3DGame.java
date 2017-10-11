package com.ru.tgra.shapes;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	OrtographicCamera ortCamera;
	PerspectiveCamera perspCamera;
	Shader3D shader;
	Point3D lightPos1;
	Color lightCol1;

	Maze maze;
	int mazeWidth, mazeDepth;
	Point3D mapCenter;

	Vector3D moveLeft, moveForward, up, lookDown, moveFor;

	boolean firstPersonView;
	float fovProjection;

	Player player, thirdPerson;

	boolean win;
	public static int level, numWallsAtOnce;

	@Override
	public void create () {
		shader = new Shader3D();
		Gdx.input.setInputProcessor(this);
		GameEnv.init(shader);

		level = 1;
		initLevel(level);
	}

	private void initLevel(int level) {
		switch (level) {
			case 1: {
				mazeWidth = 3;
				mazeDepth = 4;
				numWallsAtOnce = 1;
				break;
			}
			case 2: {
				mazeWidth = 6;
				mazeDepth = 5;
				numWallsAtOnce = 2;
				break;
			}
			case 3: {
				mazeWidth = 10;
				mazeDepth = 8;
				numWallsAtOnce = 3;
				break;
			}
			default: {
				mazeWidth = 12;
				mazeDepth = 14;
				numWallsAtOnce = 4;
			}
		}

		maze = new Maze(mazeWidth, mazeDepth);

		player = new Player(new Point3D(0.5f, 0.8f, 0.5f), new Vector3D(1, 0, 1));

		win = false;

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

		lightPos1 = new Point3D(1, 1, 1);
		lightCol1 = new Color(1, 1, 1, 1);
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			initLevel(level);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
			lightPos1.x += deltaTime * 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
			lightPos1.x -= deltaTime * 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
			lightPos1.z -= deltaTime * 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
			lightPos1.z += deltaTime * 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
			lightPos1.y += deltaTime * 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			lightPos1.y -= deltaTime * 10;
		}

	}
	
	private void update(float deltaTime)
	{
		shader.setLightPosition(lightPos1);
		shader.setLightDiffuse(lightCol1);

		if (win) {
			initLevel(++level);
			win = false;
		}

		if (firstPersonView) {
			win = player.move(moveFor, maze);
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
		maze.incrementAngle(deltaTime * 50);
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (win) {
			return;
		}

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		perspCamera.setPerspectiveProjection(fovProjection, 1, 0.1f, 100);
		shader.setViewMatrix(perspCamera.getViewMatrix());
		shader.setProjectionMatrix(perspCamera.getProjectionMatrix());
		shader.setEyePosition(perspCamera.getEye());

		maze.draw(true, shader);

		//
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(lightPos1.x, lightPos1.y, lightPos1.z);
		ModelMatrix.main.addScale(0.1f, 0.1f, 0.1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		shader.setMaterialDiffuse(lightCol1);
		SphereGraphic.drawSolidSphere();
		//

		if (!firstPersonView)
			player.draw(shader);

		// Draw a map
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int mapWidth = mazeWidth * 10;
		int mapHeight = mazeDepth * 10;
		int margin = 10;
		Gdx.gl20.glViewport(screenWidth - mapWidth - margin, screenHeight - mapHeight - margin, mapWidth, mapHeight);

		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		ortCamera.setOrtographicProjection(-mazeWidth * 0.5f, mazeWidth * 0.5f, -mazeDepth * 0.5f, mazeDepth * 0.5f, 1, 10);
		shader.setViewMatrix(ortCamera.getViewMatrix());
		shader.setProjectionMatrix(ortCamera.getProjectionMatrix());
		shader.setEyePosition(ortCamera.getEye());

		maze.draw(false, shader);
		player.draw(shader);

		//
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(lightPos1.x, lightPos1.y, lightPos1.z);
		ModelMatrix.main.addScale(0.1f, 0.1f, 0.1f);		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		shader.setMaterialDiffuse(lightCol1);
		SphereGraphic.drawSolidSphere();
		//
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