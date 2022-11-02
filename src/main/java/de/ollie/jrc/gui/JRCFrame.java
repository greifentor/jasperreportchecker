package de.ollie.jrc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.util.function.BooleanSupplier;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import de.ollie.jrc.JRC;
import de.ollie.jrc.gui.ResourceManager.Localization;
import de.ollie.jrc.logger.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class JRCFrame extends JFrame implements WindowListener {

	static final int HGAP = 3;
	static final int VGAP = 3;

	private static final Logger LOGGER = Logger.getLogger(JRCFrame.class.getSimpleName());

	private FilenameSelectorComponentFactory fnscf = new FilenameSelectorComponentFactory() {

		@Override
		public JButton createClearButton(FilenameSelector owner) {
			return new JButton(getResource("filenameselector.button.clear.text"));
		}

		@Override
		public JButton createSelectButton(FilenameSelector owner) {
			return new JButton(getResource("filenameselector.button.select.text"));
		}

		@Override
		public JTextField createTextField(FilenameSelector owner) {
			return new JTextField(80);
		}

	};

	public JRCFrame(String dirName) {
		super("JRC");
		String path = Path.of(dirName).toAbsolutePath().toString();
		addWindowListener(this);
		setMinimumSize(new Dimension(400, 200));
		setContentPane(createMainPanel(path));
		pack();
	}

	private JPanel createMainPanel(String path) {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.add(getResource("tab.header.text.check"), createCheckPanel(path));
		tabbedPane.add(getResource("tab.header.text.usage"), createUsagePanel(path));
		tabbedPane.add(getResource("tab.header.text.xml"), createXMLPanel(path));
		p.add(tabbedPane);
		return p;
	}

	private String getResource(String resourceId) {
		return ResourceManager.INSTANCE.getString(Localization.DE, resourceId);
	}

	private JPanel createCheckPanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile = new FilenameSelector(
				path,
				fnscf,
				newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		JPanel p = createComponentPanel(new ComponentData("check.filenameselectorfile.label", filenameSelectorFile));
		p
				.add(
						createButtonPanel(
								buttonStart,
								() -> JRC.checkForFile(filenameSelectorFile.getPath(), false),
								() -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")),
						BorderLayout.SOUTH);
		return p;
	}

	private JPanel createButtonPanel(JButton buttonStart, Runnable starter, BooleanSupplier enabledChecker) {
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
		buttonStart.addActionListener(e -> starter.run());
		buttonStart.setEnabled(enabledChecker.getAsBoolean());
		buttons.add(buttonStart);
		return buttons;
	}

	@AllArgsConstructor
	@Getter
	private static class ComponentData {

		String resourceId;
		Component component;
	}

	private JPanel createComponentPanel(ComponentData... componentData) {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		p.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		int rows = componentData.length;
		JPanel labels = new JPanel(new GridLayout(rows, 1, HGAP, VGAP));
		JPanel components = new JPanel(new GridLayout(rows, 1, HGAP, VGAP));
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		for (ComponentData cd : componentData) {
			labels.add(new JLabel(getResource(cd.getResourceId())));
			components.add(cd.getComponent());
		}
		panel.add(labels, BorderLayout.WEST);
		panel.add(components, BorderLayout.CENTER);
		p.add(panel, BorderLayout.NORTH);
		return p;
	}

	private JPanel createUsagePanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile = new FilenameSelector(
				path,
				fnscf,
				newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		FilenameSelector filenameSelectorReportsDirectory = new FilenameSelector(path, fnscf, null);
		JPanel p = createComponentPanel(
				new ComponentData("usage.filenameselectorreportsdirectory.label", filenameSelectorReportsDirectory),
				new ComponentData("usage.filenameselectorfile.label", filenameSelectorFile));
		p.add(createButtonPanel(buttonStart, () -> {
			try {
				JRC.usage(filenameSelectorFile.getPath(), filenameSelectorReportsDirectory.getPath(), false);
			} catch (Exception e) {
				LOGGER.error("something went wrong while reading files for usage check!", e);
			}
		}, () -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createXMLPanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile = new FilenameSelector(
				path,
				fnscf,
				newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		FilenameSelector filenameSelectorReportsDirectory = new FilenameSelector(path, fnscf, null);
		JPanel p = createComponentPanel(
				new ComponentData("xml.filenameselectorreportsdirectory.label", filenameSelectorReportsDirectory),
				new ComponentData("xml.filenameselectorfile.label", filenameSelectorFile));
		p.add(createButtonPanel(buttonStart, () -> {
			try {
				JRC.xml(filenameSelectorFile.getPath(), filenameSelectorReportsDirectory.getPath());
			} catch (Exception e) {
				LOGGER.error("something went wrong while reading files for xml generation!", e);
			}
		}, () -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")), BorderLayout.SOUTH);
		return p;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		LOGGER.info("window opened");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		LOGGER.info("window closing");
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		LOGGER.info("window closed");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		LOGGER.info("window iconified");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		LOGGER.info("window deiconified");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		LOGGER.info("window activate");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		LOGGER.info("window deactivate");
	}

}