package com.jantox.rvtools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

public class Texture {

	public static final int NEAREST = GL_NEAREST;
	public static final int LINEAR = GL_LINEAR;

	public int id;

	public int width;

	public int height;

	private Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public static Texture loadTexture(String name) {
		return loadTexture(name, NEAREST);
	}

	public static Texture loadTexture(String name, int type) {
		try {
			return loadTexture(
					ImageIO.read(new FileInputStream(new File(name))), type);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Texture loadTexture(BufferedImage bimg, int type) {
		int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0,
				bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth()
				* bimg.getHeight() * 4);

		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {
				int pixel = pixels[y * bimg.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, type);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, type);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(),
				bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return new Texture(textureID, bimg.getWidth(), bimg.getHeight());
	}

	public int getTextureID() {
		return id;
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public void update(BufferedImage bimg) {
		int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0,
				bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth()
				* bimg.getHeight() * 4);

		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {
				int pixel = pixels[y * bimg.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();

		glBindTexture(GL_TEXTURE_2D, this.getTextureID());

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}

}
