package com.jantox.rvtools;

import static org.lwjgl.opengl.GL11.*;

public class Terrain {

	private int width, length;
	private float height[][];
	private Vector3f normals[][];

	public Terrain() {
		this.width = this.length = 128;

		height = new float[width][length];
		normals = new Vector3f[width][length];
	}

	public int getWidth() {
		return width;
	}

	public int getLength() {
		return length;
	}

	public float getHeight(int x, int z) {
		return height[x][z];
	}

	public double heightAt(double x, double z) {
		if (x < 0) {
			x = 0;
		}
		if (x > this.getWidth() - 1) {
			x = this.getWidth() - 1;
		}
		if (z < 0) {
			z = 0;
		}
		if (z > this.getLength() - 1) {
			z = this.getLength() - 1;
		}

		int leftX = (int) x;
		if (leftX == this.getWidth() - 1) {
			leftX--;
		}
		double fracX = x - leftX;

		int outZ = (int) z;
		if (outZ == this.getWidth() - 1) {
			outZ--;
		}
		double fracZ = z - outZ;

		float h11 = this.getHeight(leftX, outZ);
		float h12 = this.getHeight(leftX, outZ + 1);
		float h21 = this.getHeight(leftX + 1, outZ);
		float h22 = this.getHeight(leftX + 1, outZ + 1);

		return (1 - fracX) * ((1 - fracZ) * h11 + fracZ * h12) + fracX
				* ((1 - fracZ) * h21 + fracZ * h22);
	}

	public void setHeight(int x, int z, float h) {
		height[x][z] = h;
	}

	public Vector3f getNormal(int x, int z) {
		return normals[x][z];
	}

	public void render(Renderer renderer) {
		renderer.setColor(1, 1, 1);

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 1); // 1 will always be the map texture

		for (int z = 0; z < this.getLength() - 1; z++) {
			glBegin(GL_TRIANGLE_STRIP);
			for (int x = 0; x < this.getWidth(); x++) {
				glTexCoord2f(x / 128f, z / 128f);
				glVertex3f(x * 5, this.getHeight(x, z) * 5, z * 5);
				glTexCoord2f(x / 128f, (z + 1) / 128f);
				glVertex3f(x * 5, this.getHeight(x, z + 1) * 5, (z + 1) * 5);
			}
			glEnd();
		}

		glDisable(GL_TEXTURE_2D);

		renderer.setColor(1, 0, 0);
		for (int z = 0; z < this.getLength() - 1; z++) {
			glBegin(GL_LINE_STRIP);
			for (int x = 0; x < this.getWidth(); x++) {
				glVertex3f(x * 5, this.getHeight(x, z) * 5, z * 5);
				glVertex3f(x * 5, this.getHeight(x, z + 1) * 5, (z + 1) * 5);
				glVertex3f(x * 5, this.getHeight(x, z) * 5, z * 5);
			}
			glEnd();
		}
	}

	// region system methods

}
