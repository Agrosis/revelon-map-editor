package com.jantox.rvtools;

public class Face {

	public Vector3f a, b, c, d;
	public Vector3f ta, tb, tc, td;

	int tex = 0;

	public Face(Vector3f[] facevtxes) {
		if (facevtxes.length == 3) {
			a = facevtxes[0];
			b = facevtxes[1];
			c = facevtxes[2];
			ta = tb = tc = new Vector3f();
			td = null;
			d = null;
		} else {
			a = facevtxes[0];
			b = facevtxes[1];
			c = facevtxes[2];
			d = facevtxes[3];
			ta = tb = tc = td = new Vector3f();
		}
	}

	public void setTexture(int t) {
		this.tex = t;
	}

	public Face(Vector3f a, Vector3f b, Vector3f c) {
		this.a = a;
		this.b = b;
		this.c = c;

		d = td = null;

		ta = tb = tc = new Vector3f();
	}

	public Face(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;

		ta = tb = tc = td = new Vector3f();
	}

	public boolean isQuad() {
		return d != null;
	}

	public void setTextureCoords(Vector3f[] texs) {
		ta = texs[0];
		tb = texs[1];
		tc = texs[2];
		if (this.isQuad()) {
			td = texs[3];
		}
	}

	public void render(Renderer renderer) {
		renderer.setColor(1, 0, 0);
		if (this.isQuad()) {
			renderer.drawQuad(a, b, c, d);
		} else {
			renderer.drawTriangle(a, b, c);
		}
	}

	public Vector3f getNormal() {
		Vector3f f = a.copy();
		f = f.getSubtraction(c);

		f.normalize();

		Vector3f g = b.copy();
		g = g.getSubtraction(c);

		g.normalize();

		Vector3f n = f.crossProduct(g);
		n.normalize();

		if (n.x > 0.9999 && n.x < 1) {
			n.x = 1;
		}
		if (n.y > 0.9999 && n.y < 1) {
			n.y = 1;
		}
		if (n.z > 0.9999 && n.y < 1) {
			n.z = 1;
		}

		if (n.x < 0.001 && n.x > 0) {
			n.x = 0;
		}
		if (n.y < 0.001 && n.y > 0) {
			n.y = 0;
		}
		if (n.z < 0.001 && n.z > 0) {
			n.z = 0;
		}

		return n;
	}

}
