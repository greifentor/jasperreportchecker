package de.ollie.jrc.gui;

import javax.swing.JButton;
import javax.swing.JTextField;

public interface FilenameSelectorComponentFactory {

	public JButton createClearButton(FilenameSelector owner);

	public JButton createSelectButton(FilenameSelector owner);

	public JTextField createTextField(FilenameSelector owner);

}
