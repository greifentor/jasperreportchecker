package de.ollie.jrc.gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FileListCellRenderer implements ListCellRenderer<File> {

	@Override
	public Component getListCellRendererComponent(JList<? extends File> list, File value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = new JLabel(getName(value));
		label.setOpaque(true);
		if (isSelected) {
			label.setBackground(Color.blue);
			label.setForeground(Color.white);
		} else {
			label.setBackground(Color.white);
			label.setForeground(Color.black);
		}
		return label;
	}

	private String getName(File file) {
		return file.isDirectory() ? "[" + file.getName() + "]" : file.getName();
	}

}
