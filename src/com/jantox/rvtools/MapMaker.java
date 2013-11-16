package com.jantox.rvtools;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class MapMaker extends JFrame implements ActionListener {
	
	private Workspace workspace;
	
	private ViewInstance liveView;
	private Canvas canvas;

    public MapMaker() {
        this.setTitle("Revelon Map Maker");
        this.setSize(new Dimension(1200, 620));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.buildGUI();
    }

    private void buildGUI() {
    	canvas = new Canvas();
    	canvas.setSize(new Dimension(ViewInstance.VIEW_WIDTH, ViewInstance.VIEW_HEIGHT));
    	canvas.setVisible(true);
    	
    	liveView = new ViewInstance(canvas);
    	workspace = new Workspace(liveView);
    	//GUIUtils.createBorder(workspace, "Workspace");
    	
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout());
    	
    	// 3D VIEW FUNCTIONS
    	JPanel viewPanel = new JPanel();
    	viewPanel.setPreferredSize(new Dimension(500, 520));
    	viewPanel.add(canvas);

    	// WORKSPACE FUNCTIONS
    	
    	mainPanel.add(viewPanel, BorderLayout.WEST);
    	mainPanel.add(workspace, BorderLayout.EAST);
    	
    	this.getContentPane().add(mainPanel);
    	this.pack();
    	
    	// JMENUBAR FUNCTIONS
        JMenuBar menuBar = new JMenuBar();

        JMenu file, edit, tools;

        file = new JMenu("File");
        JMenuItem open, save;
        
        open = new JMenuItem("Open Map");
        save = new JMenuItem("Save Map");
        save.addActionListener(this);
        save.setActionCommand("save");
        
        file.add(open);
        file.add(save);

        edit = new JMenu("Edit");

        tools = new JMenu("Tools");
        JMenuItem conhmap;

        conhmap = new JMenuItem("Convert PNG to RHM");
        tools.add(conhmap);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(tools);

        this.setJMenuBar(menuBar);
        
        Thread viewThread = new Thread(liveView);
        viewThread.start();
    }

    public static void main(String[] args) {
    	BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
    	Graphics g = bi.getGraphics();
    	g.setColor(new Color(0, 0, 0));
    	g.fillRect(0, 0, 128, 128);
    	
    	try {
			ImageIO.write(bi, "PNG", new FileOutputStream(new File("terrain.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
    	
        new MapMaker().setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("save")) {
			this.liveView.export();
			this.workspace.export();
		}
	}

}