package com.jantox.rvtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class HeightmapPanel extends JPanel {

	private BufferedImage texture, overlay;
	private Graphics textureGraphics, overLayGraphics;
	
	private Vector3f mousePress;
	private Vector3f currentMpos;
	
	private int scale = 1;
	private int curTool = 0;
	private int clr = 0;
	
	public int mx = 0;
	public int my = 0;
	
	public HeightmapPanel() {
		this.setPreferredSize(new Dimension(128, 128));
		this.texture = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		this.textureGraphics = this.texture.getGraphics();
		
		this.overlay = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		this.overLayGraphics = this.overlay.getGraphics();
		
		mousePress = new Vector3f(0, 0, 0);
		currentMpos = new Vector3f(0, 0, 0);
	}
	
	public void setTool(int tool) {
		this.curTool = tool;
	}
	
	public void setMouseClick(Vector3f mpos) {
		mousePress = mpos;
	}
	
	public void setDrag(Vector3f mpos) {
		this.currentMpos = mpos;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
		this.setPreferredSize(new Dimension(128 * scale, 128 * scale));
	}
	
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
		this.textureGraphics = this.texture.getGraphics();
		this.overlay = new BufferedImage(128 * scale, 128 * scale, BufferedImage.TYPE_INT_ARGB);
		this.overLayGraphics = this.overlay.getGraphics();
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 1000, 1000);
		
		g.setColor(new Color(clr));
		
		g.drawImage(texture, 0, 0, 128 * scale, 128 * scale, null);

		if(curTool == 3) {
			this.overlay = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
			this.overLayGraphics = this.overlay.getGraphics();
			this.overLayGraphics.setColor(new Color(clr));
			if(currentMpos != null) {
				this.overLayGraphics.drawLine((int)mousePress.x, (int)mousePress.y, (int)currentMpos.x, (int)currentMpos.y);
			}
		}
		g.drawImage(overlay, 0, 0, 128 * scale, 128 * scale, null);
		
		g.setColor(Color.RED);
		g.drawString("Mouse X: " + mx + " Mouse Y: " + my, 10, 360);
		
		if(scale > 4) {
			g.setColor(Color.GRAY);
			for(int i = 0; i < scale*128; i+= scale) {
				g.drawLine(0, i, scale*128, i);
				g.drawLine(i, 0, i, scale*128);
			}
		}
	}
	
	public void setColor(int i) {
		this.clr = i;
	}

	public void color(int i, int x, int y) {
		if(x < texture.getWidth() && y < texture.getHeight() && x >= 0 && y >= 0) {
			texture.setRGB(x, y, new Color(i).getRGB());
			this.repaint();
		}
	}
	
	public void fill(int i, int x, int y) {
		int orig = texture.getRGB(x, y);
		if(x < texture.getWidth() && y < texture.getHeight() && x >= 0 && y >= 0) {
			this.spread(i, orig, x, y);
		}
		this.repaint();
	}
	
	public void spread(int nc, int oc, int x, int y) {
		if(oc == this.texture.getRGB(x, y)) {
			this.texture.setRGB(x, y, nc);
			if(x > 0 && y > 0 && y < texture.getWidth()-1 && x < texture.getHeight()-1) {
				if(this.texture.getRGB(x - 1, y) == oc && this.texture.getRGB(x - 1, y) != nc)
					spread(nc, oc, x - 1, y);
				if(this.texture.getRGB(x + 1, y) == oc && this.texture.getRGB(x + 1, y) != nc)
					spread(nc, oc, x + 1, y);
				if(this.texture.getRGB(x, y-1) == oc && this.texture.getRGB(x, y-1) != nc)
					spread(nc, oc, x, y-1);
				if(this.texture.getRGB(x, y+1) == oc && this.texture.getRGB(x, y+1) != nc)
					spread(nc, oc, x, y+1);
			}
		}
	}

	public Color getColorAt(int x, int y) {
		return new Color(texture.getRGB(x,y));
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public void apply() {
		this.textureGraphics.setColor(new Color(clr));
		this.textureGraphics.drawLine((int)mousePress.x, (int)mousePress.y, (int)currentMpos.x, (int)currentMpos.y);
	}
	
}
