package com.ru.tgra.shapes;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	OrtographicCamera ortCamera;
	PerspectiveCamera perspCamera;
	Maze maze;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		GameEnv.init();

		perspCamera = new PerspectiveCamera();
		perspCamera.Look3D(new Point3D(1, 1.5f, 1), new Point3D(2,1,2), new Vector3D(0,1,0));

		//ortCamera = new OrtographicCamera();
		//ortCamera.Look3D(new Point3D(1, 1, 1.5f), new Point3D(2, 2, 1.5f), new Vector3D(0,0,1));

		maze = new Maze(5, 10);
	}

	private void input(float deltaTime)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			perspCamera.slide(10 * deltaTime, 10 * deltaTime, 10 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			perspCamera.slide(-10 * deltaTime, -10 * deltaTime, -10 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			perspCamera.pitch(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			perspCamera.pitch(-50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			perspCamera.roll(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			perspCamera.roll(-50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			perspCamera.yaw(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			perspCamera.yaw(-50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
			perspCamera.slide(10 * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7)) {
			perspCamera.slide(-10 * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_5)) {
			perspCamera.slide(0, 10 * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
			perspCamera.slide(0, -10 * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
			perspCamera.slide(0, 0, 10 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_9)) {
			perspCamera.slide(0, 0, -10 * deltaTime);
		}

	}
	
	private void update(float deltaTime)
	{

		//do all updates to the game
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		perspCamera.setPerspectiveProjection(50, 9.0f/16.0f, 1, 100);
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