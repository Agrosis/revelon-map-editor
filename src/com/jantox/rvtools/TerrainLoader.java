package com.jantox.rvtools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TerrainLoader {

	public static Terrain loadTerrain(String filename) throws IOException {
		Terrain terrain = new Terrain();
		BufferedImage bi = ImageIO.read(new File(filename));

		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				float color = new Color(bi.getRGB(j, i)).getRed();
				color /= 255f;
				color -= 0.5f;

				color *= 16;

				terrain.setHeight(j, i, color);
			}
		}

		// terrain.computeNormals();

		return terrain;
	}

}