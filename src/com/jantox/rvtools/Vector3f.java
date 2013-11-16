package com.jantox.rvtools;

public class Vector3f {

	public float x, y, z;

	public Vector3f() {
		this.x = this.y = this.z = 0;
	}

	public Vector3f(float a, float b, float c) {
		this.x = a;
		this.y = b;
		this.z = c;
	}

	public void add(Vector3f b) {
		x += b.x;
		y += b.y;
		z += b.z;
	}

	public void subtract(Vector3f b) {
		x -= b.x;
		y -= b.y;
		z -= b.y;
	}

	public void multiply(float f) {
		x *= f;
		y *= f;
		z *= f;
	}

	public void divide(float f) {
		x /= f;
		y /= f;
		z /= f;
	}

	public Vector3f getAddition(Vector3f b) {
		return new Vector3f(x + b.x, y + b.y, z + b.z);
	}

	public Vector3f getSubtraction(Vector3f b) {
		return new Vector3f(x - b.x, y - b.y, z - b.z);
	}

	public Vector3f getMultiplication(float f) {
		return new Vector3f(x * f, y * f, z * f);
	}

	public Vector3f getDivision(float f) {
		return new Vector3f(x / f, y / f, z / f);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public void normalize() {
		this.divide(this.length());
	}

	public float dotProduct(Vector3f b) {
		return x * b.x + y * b.y + z * b.z;
	}

	public Vector3f copy() {
		return new Vector3f(x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector3f) {
			Vector3f b = (Vector3f) o;
			if (x == b.x && y == b.y && z == b.z) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public float distanceSquared(Vector3f b) {
		return (b.x - x) * (b.x - x) + (b.y - y) * (b.y - y) + (b.z - z)
				* (b.z - z);
	}

	public void move(float angle, float speed) {
		x += Math.cos(Math.toRadians(angle)) * speed;
		z += Math.sin(Math.toRadians(angle)) * speed;
	}

	public Vector3f crossProduct(Vector3f b) {
		Vector3f n = new Vector3f();

		n.x = y * b.z - z * b.y;
		n.y = z * b.x - x * b.z;
		n.z = x * b.y - y * b.x;

		return n;
	}

}
