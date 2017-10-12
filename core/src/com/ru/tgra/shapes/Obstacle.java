package com.ru.tgra.shapes;

public class Obstacle {
	private int posX, posZ;
	private float radius;
	private Color color;
	private boolean growing;

	public Obstacle(int posX, int posZ, float radius, Color color) {
		this.posX = posX;
		this.posZ = posZ;
		this.radius = radius;
		this.color = color;
		this.growing = true;
	}

	public void draw(Shader3D shader) {
		shader.setMaterialDiffuse(this.color);
		shader.setMaterialSpecular(new Color(1, 1, 1, 1));
		shader.setShininess(10);
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.addTranslation(posX + 0.5f, 0.05f, posZ + 0.5f);
		ModelMatrix.main.addScale(this.radius, this.radius, this.radius);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere();
	}

	public void changeSize(float deltaTime) {
		if (this.growing)
			if (this.radius > 0.3f)
				this.growing = false;
			else
				this.radius += deltaTime;
		else
			if (this.radius < 0.1f)
				this.growing = true;
			else
				this.radius -= deltaTime;
	}
}
