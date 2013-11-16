package com.jantox.rvtools;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

// Camera is a third person camera that focuses in on the Player
public class Camera {

	public static final float CAMERA_DISTANCE = 15f;
	public static final float PLAYER_SPEED = 2f;

	private Vector3f campos;

	private float fc_yaw, fc_pitch;
	private Vector3f focus;

	private ViewInstance vi;
	private Terrain t;
	private Model held;

	public Camera(ViewInstance vi, Terrain t) {
		this.vi = vi;
		this.t = t;

		this.campos = new Vector3f();
		this.fc_yaw = 0;
		this.fc_pitch = 0;

		this.focus = new Vector3f();

		held = ModelLoader.loadRTM("models/house.rtm");
	}

	public void update() {
		focus.y = (float) t.heightAt(focus.x / 5, focus.z / 5) * 5;

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				vi.spawn(new Decoration(new Model(held.getID()), focus.copy(), (float) Math.ceil(((int) fc_yaw + 45 + 45/2f) / 45) * 45));
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			fc_yaw -= 2.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			fc_yaw += 2.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			fc_pitch--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			fc_pitch++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			focus.move(-(fc_yaw + 180) - 90, PLAYER_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			focus.move(-(fc_yaw + 180) - 180, PLAYER_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			focus.move(-(fc_yaw + 180) - 270, PLAYER_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			focus.move(-(fc_yaw + 180), PLAYER_SPEED);
		}

		this.fixCamera();

		Vector3f dir = this.getDirectionVector();
		dir.multiply(100);

		campos = focus.getAddition(dir);
		// campos.y = focus.y + 50;
		// calculate new pitch and yaw here
	}

	public void translate() {
		glTranslatef(-campos.x, -campos.y, -campos.z);
	}

	public void rotate() {
		glRotatef(-fc_pitch, 1.0f, 0f, 0f);
		glRotatef(-(fc_yaw + 180), 0f, 1.0f, 0f);
	}

	public void render(Renderer renderer) {
		this.held.render(renderer, focus.copy(), new Vector3f(0, 1, 0), (float) Math.ceil(((int) fc_yaw + 45 + 45/2f) / 45) * 45);
	}

	public Vector3f getDirectionVector() {
		double pitchRadians = Math.toRadians(fc_pitch);
		double yawRadians = Math.toRadians(fc_yaw);

		double sinPitch = Math.sin(pitchRadians);
		double cosPitch = Math.cos(pitchRadians);
		double sinYaw = Math.sin(yawRadians);
		double cosYaw = Math.cos(yawRadians);

		return new Vector3f((float) -cosPitch * (float) sinYaw,
				(float) -sinPitch, (float) -cosPitch * (float) cosYaw);
	}

	public void fixCamera() {
		if (fc_yaw >= 360) {
			fc_yaw -= 360;
		}
		if (fc_yaw < 0) {
			fc_yaw += 360;
		}

		if (fc_pitch < -75) {
			fc_pitch = -75;
		}
		if (fc_pitch > -10) {
			fc_pitch = -10;
		}
	}

	public Vector3f getPosition() {
		return campos.copy();
	}

}