package com.jantox.rvtools;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class ViewInstance implements Runnable {
	
	public static final int VIEW_WIDTH = 500;
	public static final int VIEW_HEIGHT = 500;
	
	private Canvas canvas;

	// testing
	private Texture t;
	private BufferedImage bi;
	
	private Camera camera;
	private Terrain terrain;
	
	private ArrayList<Decoration> decs;
	
	public ViewInstance(Canvas canvas) {
		this.canvas = canvas;
		
		decs = new ArrayList<Decoration>();
	}
	
	public void updateTexture(BufferedImage bi) {
		this.bi = bi;
	}
	
	public void initGL() {
		glViewport(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(68, (float) VIEW_WIDTH / (float) VIEW_HEIGHT, 0.01f, 2000.0f);
        glMatrixMode(GL_MODELVIEW);

        glClearDepth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	}
	
	public void update() {
		camera.update();
		if(bi != null) {
			synchronized(t) {
				t.update(bi);
			}
		}
	}
	
	float y = 0;
	
	public void render() {
		Renderer renderer = Renderer.instance();
		renderer.clear();
		
		renderer.pushMatrix();

		camera.rotate();
		camera.translate();
		
		renderer.pushMatrix();
		terrain.render(renderer);
		
		for(int i = 0; i < decs.size(); i++) {
			renderer.pushMatrix();
			decs.get(i).render(renderer);
			renderer.popMatrix();
		}
		
		camera.render(renderer);
		renderer.popMatrix();
		
		renderer.popMatrix();
	}

	@Override
	public void run() {
		try {
			Display.setParent(canvas);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		this.initGL();

		this.t = Texture.loadTexture("map1.png");
		try {
			this.terrain = TerrainLoader.loadTerrain("terrain.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.camera = new Camera(this, this.terrain);
		
		while(true) {
			this.update();
			this.render();
			
			Display.update();
			Display.sync(60);
		}
	}
	
	public void spawn(Decoration d) {
		decs.add(d);
	}

	public void export() {
		
	}

	public void updateHeightmap(BufferedImage hmap) {
		for (int i = 0; i < hmap.getWidth(); i++) {
			for (int j = 0; j < hmap.getHeight(); j++) {
				float color = new Color(hmap.getRGB(j, i)).getRed();
				color /= 255f;
				color -= 0.5f;

				color *= 16;

				terrain.setHeight(j, i, color);
			}
		}
	}
	
}
