package com.jantox.rvtools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class HeightmapEditor extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

	public static final int TOOL_PENCIL = 0;
	public static final int TOOL_FILLER = 1;
	public static final int TOOL_CHOOSER = 2;
	public static final int TOOL_LINE = 3;
	
	private final JFileChooser fileChooser;
	
	private JScrollPane heightmapPane;
	private HeightmapPanel hp;
	private ViewInstance view;
	
	private JButton openTexture;
	private JButton fgColor, bgColor;
	private ButtonGroup bgroup;
	private JButton zoomIn, zoomOut;
	
	private Color fg, bg;
	
	private int lastTool = 0;
	private int curTool = 0;
	private int scale = 1;
	
	JRadioButton pencil, fill, chooser, line;

	public HeightmapEditor(ViewInstance view) {
		this.view = view;
		this.setSize(700, 600);
		
		this.setLayout(new BorderLayout());
		
		this.fg = new Color(0, 0, 0);
		this.bg = new Color(0, 0, 0);
		
		this.hp = new HeightmapPanel();
		
		this.openTexture = new JButton("Open Texture");
		this.openTexture.addActionListener(this);
		
		this.fgColor = new JButton("Set Foreground");
		this.fgColor.addActionListener(this);
		
		this.bgColor = new JButton("Set Background");
		this.bgColor.addActionListener(this);
		
		this.zoomIn = new JButton("Zoom In");
		this.zoomIn.addActionListener(this);
		this.zoomOut = new JButton("Zoom Out");
		this.zoomOut.addActionListener(this);
		
		bgroup = new ButtonGroup();
		
		pencil = new JRadioButton("Pencil");
		pencil.setSelected(true);
		pencil.setActionCommand("pencil");
		pencil.addActionListener(this);
		fill = new JRadioButton("Fill");
		fill.setActionCommand("fill");
		fill.addActionListener(this);
		chooser = new JRadioButton("Color Chooser");
		chooser.setActionCommand("chooser");
		chooser.addActionListener(this);
		line = new JRadioButton("Line");
		line.setActionCommand("line");
		line.addActionListener(this);
		
		bgroup.add(pencil);
		bgroup.add(fill);
		bgroup.add(chooser);
		bgroup.add(line);
		
		JPanel toolsPanel = new JPanel();
		toolsPanel.setPreferredSize(new Dimension(249, 100));
		GUIUtils.createBorder(toolsPanel, "Toolbox");
		toolsPanel.add(pencil);
		toolsPanel.add(fill);
		toolsPanel.add(chooser);
		toolsPanel.add(line);
		
		
		JPanel actionPanel = new JPanel();
		actionPanel.add(openTexture);
		actionPanel.add(zoomIn);
		actionPanel.add(zoomOut);
		
		heightmapPane = new JScrollPane(hp);
		heightmapPane.setPreferredSize(new Dimension(400, 400));
		
		JPanel lefhpanel = new JPanel();
		lefhpanel.add(heightmapPane);
		
		this.add(lefhpanel, BorderLayout.WEST);
		
		JPanel righhpanel = new JPanel();
		righhpanel.add(actionPanel);
		righhpanel.add(toolsPanel);
		
		JPanel colorPanel = new JPanel();
		GUIUtils.createBorder(colorPanel, "Color Chooser");
		colorPanel.add(fgColor);
		colorPanel.add(bgColor);
		righhpanel.add(colorPanel);
		
		this.add(righhpanel, BorderLayout.CENTER);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
	}
	
	@Override 
	public void addNotify() {
		super.addNotify();
		
		this.hp.addMouseListener(this);
		this.hp.addMouseMotionListener(this);
		this.requestFocus();
		this.setFocusable(true);
		this.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(openTexture)) {
			int res = fileChooser.showOpenDialog(this);
			if(res == 0) {
				File texture = fileChooser.getSelectedFile();
				try {
					BufferedImage imgTex = ImageIO.read(texture);
					hp.setTexture(imgTex);
					hp.repaint();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if(ae.getSource().equals(fgColor)) {
			fg = JColorChooser.showDialog(this, "Choose A Foreground Color", fg);
			hp.setColor(fg.getRGB());
		} else if(ae.getSource().equals(bgColor)) {
			bg = JColorChooser.showDialog(this, "Choose A Background Color", bg);
		} else if(ae.getSource().equals(zoomIn)) {
			scale *= 2;
			if(scale > 32) {
				scale = 32;
			}
			hp.setScale(scale);
			hp.repaint();
		} else if(ae.getSource().equals(zoomOut)) {
			scale /= 2;
			if(scale < 1) {
				scale = 1;
			}
			hp.setScale(scale);
			hp.repaint();
		} else {
			lastTool = curTool;
			if(ae.getSource().equals(pencil)) {
				curTool = 0;
			} else if(ae.getSource().equals(fill)) {
				curTool = 1;
			} else if(ae.getSource().equals(chooser)) {
				curTool = 2;
			} else if(ae.getSource().equals(line)) {
				curTool = 3;
			}
			hp.setTool(curTool);
		}
		
		this.heightmapPane.revalidate();
		this.heightmapPane.revalidate();
		
		this.heightmapPane.getHorizontalScrollBar().setUnitIncrement(scale);
		this.heightmapPane.getVerticalScrollBar().setUnitIncrement(scale);
	}
	
	private boolean drag = false;
	private int px, py;

	@Override
	public void mouseDragged(MouseEvent me) {
		this.drag = true;
		
		hp.mx = me.getX();
		hp.my = me.getY();
		
		int x = me.getX() / scale;
		int y = me.getY() / scale;
		
		hp.setDrag(new Vector3f(x, y, 0));
		
		Color c = null;
		if(SwingUtilities.isRightMouseButton(me)) {
			c = bg;
		} else if(SwingUtilities.isLeftMouseButton(me)) {
			c = fg;
		}
		
		if(curTool == TOOL_PENCIL) {
			hp.color(c.getRGB(), x, y);
		} else if(curTool == TOOL_FILLER) {
			hp.fill(c.getRGB(), x, y);
		} else if(curTool == TOOL_CHOOSER) {
			if(SwingUtilities.isRightMouseButton(me)) {
				bg = hp.getColorAt(x, y);
			} else if(SwingUtilities.isLeftMouseButton(me)) {
				fg = hp.getColorAt(x, y);
			}
			hp.setColor(fg.getRGB());
			this.setTool(lastTool);
		}
		
		this.repaint();
		view.updateHeightmap(hp.getTexture());
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		hp.mx = me.getX();
		hp.my = me.getY();
		hp.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		this.requestFocus();
		int x = me.getX() / scale;
		int y = me.getY() / scale;
		
		hp.setMouseClick(new Vector3f(x,y,0));
		
		Color c = null;
		
		if(SwingUtilities.isRightMouseButton(me)) {
			c = bg;
		} else if(SwingUtilities.isLeftMouseButton(me)) {
			c = fg;
		}
		
		if(curTool == TOOL_PENCIL) {
			hp.color(c.getRGB(), x, y);
		} else if(curTool == TOOL_FILLER) {
			hp.fill(c.getRGB(), x, y);
		} else if(curTool == TOOL_CHOOSER) {
			if(SwingUtilities.isRightMouseButton(me)) {
				bg = hp.getColorAt(x, y);
			} else if(SwingUtilities.isLeftMouseButton(me)) {
				fg = hp.getColorAt(x, y);
			}
			hp.setColor(fg.getRGB());
			this.setTool(lastTool);
		}
		view.updateHeightmap(hp.getTexture());
	}

	public void setTool(int tool) {
		hp.setTool(tool);
		if(tool == 0) {
			pencil.setSelected(true);
		} else if(tool == 1) {
			fill.setSelected(true);
		} else if(tool == 2) {
			chooser.setSelected(true);
		} else if(tool == 3) {
			line.setSelected(true);
		}
		curTool = tool;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.drag = false;

		if(curTool == 3) {
			hp.apply();
		}
		
		hp.setDrag(null);
		

		view.updateHeightmap(hp.getTexture());
	}

	public void export() throws FileNotFoundException, IOException {
		ImageIO.write(hp.getTexture(), "PNG", new FileOutputStream(new File("map_texture.png")));
	}
	
	private boolean ctrl = false;

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyChar() == 'c') {
			lastTool = curTool;
			curTool = 2;
		} else if(ke.getKeyChar() == 'l') {
			lastTool = curTool;
			curTool = 3;
		} else if(ke.getKeyChar() == 'f') {
			lastTool = curTool;
			curTool = 1;
		} else if(ke.getKeyChar() == 'd') {
			lastTool = curTool;
			curTool = 0;
		} 
		
		if(ke.getKeyCode() == KeyEvent.VK_EQUALS) {
			scale *= 2;
			if(scale > 32) {
				scale = 32;
			}
			hp.setScale(scale);
			hp.repaint();
			this.heightmapPane.revalidate();
		} else if(ke.getKeyCode() == KeyEvent.VK_MINUS) {
			scale /= 2;
			if(scale < 1) {
				scale = 1;
			}
			hp.setScale(scale);
			hp.repaint();
			this.heightmapPane.revalidate();
		}
		
		this.heightmapPane.getHorizontalScrollBar().setUnitIncrement(scale);
		this.heightmapPane.getVerticalScrollBar().setUnitIncrement(scale);
		
		this.setTool(curTool);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
	
}
