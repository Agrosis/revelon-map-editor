package com.jantox.rvtools;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GUIUtils {

	public static void createBorder(JPanel panel, String title) {
		panel.setBorder(BorderFactory.createTitledBorder(title));
	}
	
}
