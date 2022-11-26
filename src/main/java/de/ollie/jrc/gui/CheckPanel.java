package de.ollie.jrc.gui;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.ollie.jrc.FileNameProvider;
import de.ollie.jrc.JRC;
import de.ollie.jrc.gui.JRCFrame.ComponentData;
import de.ollie.jrc.gui.ResourceManager.Localization;

public class CheckPanel extends JPanel implements FocusListener {

	private JButton buttonStart;
	private CorrectFileSelectionChecker correctFileSelectionChecker;
	private FilenameSelector filenameSelectorFile;
	private FileNameProvider fileNameProvider;
	private Localization localization;
	private JTextField textFieldPattern = new JTextField(40);
	private JTextField textFieldExcludes = new JTextField(40);

	public CheckPanel(Localization localization, FileNameProvider fileNameProvider, JTextArea textAreaOutput,
			String path, FilenameSelectorComponentFactory fnscf,
			CorrectFileSelectionChecker correctFileSelectionChecker) {
		super(new BorderLayout());
		this.correctFileSelectionChecker = correctFileSelectionChecker;
		this.fileNameProvider = fileNameProvider;
		this.localization = localization;
		buttonStart = new JButton(getResource("buttons.start.label"));
		textFieldPattern.addActionListener(event -> setButtonStartEnabled());
		textFieldPattern.addFocusListener(this);
		filenameSelectorFile = new FilenameSelector(path, fnscf, newPath -> setButtonStartEnabled());
		add(
				JRCFrame
						.createComponentPanel(
								"check",
								localization,
								new ComponentData("check.filenameselectorfile.label", filenameSelectorFile),
								new ComponentData("check.textfieldpattern.label", textFieldPattern),
								new ComponentData("check.textfieldexcludes.label", textFieldExcludes)),
				BorderLayout.CENTER);
		add(
				JRCFrame
						.createButtonPanel(
								buttonStart,
								() -> textAreaOutput
										.setText(
												JRC
														.check(
																getFileNames(
																		filenameSelectorFile.getPath(),
																		textFieldPattern.getText()),
																true,
																textFieldExcludes.getText())),
								() -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")),
				BorderLayout.SOUTH);
	}

	private void setButtonStartEnabled() {
		String newPath = (filenameSelectorFile != null ? filenameSelectorFile.getPath() : null);
		buttonStart.setEnabled(correctFileSelectionChecker.isValid(newPath, getPattern(textFieldPattern)));
	}

	private String getResource(String resourceId) {
		return ResourceManager.INSTANCE.getString(localization, resourceId);
	}

	private List<String> getFileNames(String path, String pattern) {
		if (path.toLowerCase().endsWith(".jrxml")) {
			return List.of(path);
		}
		return fileNameProvider.getFileNamesFromCommandLineParameters(List.of(), path, pattern);
	}

	private String getPattern(JTextField textFieldPattern) {
		return ((textFieldPattern != null) && (textFieldPattern.getText() != null)
				&& !textFieldPattern.getText().isEmpty()) ? textFieldPattern.getText() : null;
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
