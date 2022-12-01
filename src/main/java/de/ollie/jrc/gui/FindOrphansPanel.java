package de.ollie.jrc.gui;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.ollie.jrc.commands.FindOrphansCommand;
import de.ollie.jrc.gui.JRCFrame.ComponentData;
import de.ollie.jrc.gui.ResourceManager.Localization;

public class FindOrphansPanel extends JPanel implements FocusListener {

	private JButton buttonStart;
	private FilenameSelector filenameSelectorFile;
	private Localization localization;

	public FindOrphansPanel(Localization localization, JTextArea textAreaOutput, String path,
			FilenameSelectorComponentFactory fnscf) {
		super(new BorderLayout());
		this.localization = localization;
		buttonStart = new JButton(getResource("buttons.start.label"));
		filenameSelectorFile = new FilenameSelector(path, fnscf, newPath -> setButtonStartEnabled());
		add(
				JRCFrame
						.createComponentPanel(
								"findorphans",
								localization,
								new ComponentData("findorphans.filenameselectorfile.label", filenameSelectorFile)),
				BorderLayout.CENTER);
		add(
				JRCFrame
						.createButtonPanel(
								buttonStart,
								() -> textAreaOutput.setText(new FindOrphansCommand(path).getReport()),
								() -> new File(filenameSelectorFile.getPath()).isDirectory()),
				BorderLayout.SOUTH);
	}

	private void setButtonStartEnabled() {
		String newPath = (filenameSelectorFile != null ? filenameSelectorFile.getPath() : null);
		buttonStart.setEnabled((newPath != null) && new File(newPath).isDirectory());
	}

	private String getResource(String resourceId) {
		return ResourceManager.INSTANCE.getString(localization, resourceId);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// NOP
	}

	@Override
	public void focusLost(FocusEvent e) {
		setButtonStartEnabled();
	}

}
