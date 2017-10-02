package com.ru.tgra.shapes;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {
	OrtographicCamera ortCamera;
	PerspectiveCamera perspCamera;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		GameEnv.init();

		perspCamera = new PerspectiveCamera();
		perspCamera.Look3D(new Point3D(1.5f, 1.2f, 2.0f), new Point3D(0,0,0), new Vector3D(0,1,0));

		ortCamera = new OrtographicCamera();
		ortCamera.Look3D(new Point3D(1.5f, 1.2f, 2.0f), new Point3D(0,0,0), new Vector3D(0,1,0));
	}

	private void input(float deltaTime)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			ortCamera.slide(10 * deltaTime, 10 * deltaTime, 10 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			ortCamera.slide(-10 * deltaTime, -10 * deltaTime, -10 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			ortCamera.pitch(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			ortCamera.pitch(-50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			ortCamera.roll(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			ortCamera.roll(-50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			ortCamera.yaw(50 * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			ortCamera.yaw(-50 * deltaTime);
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

		//perspCamera.setPerspectiveProjection(90, 9.0f/16.0f, 1, 100);
		//perspCamera.setShaderMatrix();
		ortCamera.setOrtographicProjection(-10, 10, -10, 10, 1, 100);
		ortCamera.setShaderMatrix();

		Gdx.gl.glUniform4f(GameEnv.colorLoc, 0.9f, 0.3f, 0.1f, 1.0f);

		ModelMatrix.main.loadIdentityMatrix();
		//ModelMatrix.main.addTranslation(250, 250, 0);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addScale(1.0f, 1.0f, 1.0f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		//BoxGraphic.drawOutlineCube();
		//SphereGraphic.drawSolidSphere();
		//SphereGraphic.drawOutlineSphere();
		ModelMatrix.main.popMatrix();

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