package com.jantox.rvtools;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Model {

	private int mid;

	private ArrayList<Face> faces;

	public Model(int id) {
		this.mid = id;
	}

	public Model(int id, ArrayList<Face> faces) {
		this.mid = id;
		this.faces = faces;
	}

	public void render(Renderer renderer, Vector3f pos, Vector3f rot, float angle) {
		glColor3f(1, 1, 1);
		//glDisable(GL_TEXTURE_2D);

		glTranslatef((float)Math.ceil(((int)pos.x) / 5) * 5, pos.y, (float)Math.ceil(((int)pos.z) / 5) * 5);
		glRotatef(angle, rot.x, rot.y, rot.z);
		glScalef(3, 3, 3);

		glCallList(mid);
	}

	public int getID() {
		return mid;
	}

}
