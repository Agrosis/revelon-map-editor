package com.jantox.rvtools;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

	private Renderer() {
		
	}

	public void pushMatrix() {
		glPushMatrix();
	}

	public void popMatrix() {
		glPopMatrix();
	}

	public void setTexture(Texture t) {
		if (t != null) {
			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, t.getTextureID());
		}
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void clearTexture() {
		glDisable(GL_TEXTURE_2D);
		this.setColor(1, 1, 1, 1);
	}

	public void setColor(float r, float g, float b) {
		glColor3f(r, g, b);
	}

	public void setColor(float r, float g, float b, float a) {
		glColor4f(r, g, b, a);
	}

	public static Renderer instance() {
		return new Renderer();
	}

	public void drawTriangle(Vector3f a, Vector3f b, Vector3f c) {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_TRIANGLES);
		glVertex3f(a.x, a.y, a.z);
		glVertex3f(b.x, b.y, b.z);
		glVertex3f(c.x, c.y, c.z);
		glEnd();
	}

	public void drawQuad(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glVertex3f(a.x, a.y, a.z);
		glVertex3f(b.x, b.y, b.z);
		glVertex3f(c.x, c.y, c.z);
		glVertex3f(d.x, d.y, d.z);
		glEnd();
	}

	public void drawQuad(Vector3f a, Vector3f b, Vector3f c, Vector3f d, Vector3f ta, Vector3f tb, Vector3f tc, Vector3f td) {
		glEnable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glTexCoord2f(ta.x, ta.y);
		glVertex3f(a.x, a.y, a.z);
		glTexCoord2f(tb.x, tb.y);
		glVertex3f(b.x, b.y, b.z);
		glTexCoord2f(tc.x, tc.y);
		glVertex3f(c.x, c.y, c.z);
		glTexCoord2f(td.x, td.y);
		glVertex3f(d.x, d.y, d.z);
		glEnd();
	}
	
}
