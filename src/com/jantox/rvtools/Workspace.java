package com.jantox.rvtools;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class Workspace extends JPanel {

	private ViewInstance liveView;
	
	private TextureEditor te;
	private MapEditor me;
	private HeightmapEditor he;
	
	public Workspace(ViewInstance vi) {
		this.liveView = vi;
		this.setPreferredSize(new Dimension(700, 520));
		
		te = new TextureEditor(vi);
		he = new HeightmapEditor(vi);
		me = new MapEditor();

		JTabbedPane tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(700, 498));
		tabs.addTab("Map Editor", me);
		tabs.addTab("Texture Editor", te);
		tabs.addTab("Heightmap Editor", he);
		
		JPanel tabPanel = new JPanel();
		tabPanel.add(tabs);
		
		this.add(tabPanel);
	}

	public void export() {
		try {
			this.te.export();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
