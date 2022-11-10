package de.ollie.jrc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBException;

import de.ollie.jrc.DirectoryScanner;
import de.ollie.jrc.JRC;
import de.ollie.jrc.gui.ResourceManager.Localization;
import de.ollie.jrc.jrxml.FileReader;
import de.ollie.jrc.jrxml.FontLister;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.logger.Logger;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Generated // OLI: excluding GUI from unit testing.
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

	private JTextArea textAreaOutput;

	private Localization localization = Localization.DE;

	public JRCFrame(String dirName) {
		super("JRC");
		textAreaOutput = new JTextArea(40, 20);
		textAreaOutput.setFont(new Font("monospaced", Font.PLAIN, 11));
		String path = Path.of(dirName).toAbsolutePath().toString();
		localization = Localization.valueOf(System.getProperty("jrc.language", localization.name()).toUpperCase());
		addWindowListener(this);
		setMinimumSize(new Dimension(400, 200));
		setContentPane(createMainPanel(path));
		pack();
	}

	private JPanel createMainPanel(String path) {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.add(getResource("tab.header.text.check"), createCheckPanel(path));
		tabbedPane.add(getResource("tab.header.text.fontlister"), createFontListerPanel(path));
		tabbedPane.add(getResource("tab.header.text.usage"), createUsagePanel(path));
		tabbedPane.add(getResource("tab.header.text.xml"), createXMLPanel(path));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, new JScrollPane(textAreaOutput));
		p.add(splitPane);
		return p;
	}

	private String getResource(String resourceId) {
		return ResourceManager.INSTANCE.getString(localization, resourceId);
	}

	private JPanel createCheckPanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile =
				new FilenameSelector(
						path,
						fnscf,
						newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		JPanel p =
				createComponentPanel(
						"check",
						new ComponentData("check.filenameselectorfile.label", filenameSelectorFile));
		p
				.add(
						createButtonPanel(
								buttonStart,
								() -> textAreaOutput.setText(JRC.checkForFile(filenameSelectorFile.getPath(), false)),
								() -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")),
						BorderLayout.SOUTH);
		return p;
	}

	private JPanel createFontListerPanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorDirectory = new FilenameSelector(path, fnscf, null);
		JTextField textFieldExclude = new JTextField(40);
		JPanel p =
				createComponentPanel(
						"fontlister",
						new ComponentData("fontlister.filenameselectordirectory.label", filenameSelectorDirectory),
						new ComponentData("fontlister.textfieldexclude.label", textFieldExclude));
		p
				.add(
						createButtonPanel(
								buttonStart,
								() -> listFonts(filenameSelectorDirectory.getPath(), textFieldExclude.getText()),
								() -> new File(filenameSelectorDirectory.getPath()).isDirectory()),
						BorderLayout.SOUTH);
		return p;
	}

	private void listFonts(String directory, String exclude) {
		List<File> files = new DirectoryScanner().scan(new ArrayList<>(), directory, "*.jrxml");
		FontLister fontLister = new FontLister();
		List<String> fileNames = files.stream().map(File::getAbsolutePath).sorted().collect(Collectors.toList());
		StringBuilder sb = new StringBuilder();
		for (String fileName : fileNames) {
			try {
				JasperReport jasperReport = new FileReader(fileName).readFromFile();
				Set<String> fontNames = fontLister.getUsedFontNames(jasperReport);
				if ((exclude != null) && !exclude.isEmpty()) {
					fontNames.remove(exclude);
				}
				if (!fontNames.isEmpty()) {
					String s = fileName + ":\n";
					System.out.print(s);
					sb.append(s);
					fontNames.stream().sorted().forEach(fontName -> {
						String o = "- " + fontName + "\n";
						System.out.print(o);
						sb.append(o);
					});
				}
			} catch (IOException | JAXBException e) {
				LOGGER.error("something went wrong while reading file for font listing: " + fileName, e);
			}
		}
		textAreaOutput.setText(sb.toString());
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

	private JPanel createComponentPanel(String descriptionResourceIdPrefix, ComponentData... componentData) {
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
		JTextArea description = new JTextArea(getResource(descriptionResourceIdPrefix + ".description.text"), 4, 40);
		description.setEditable(false);
		description.setLineWrap(true);
		panel.add(new JScrollPane(description), BorderLayout.NORTH);
		panel.add(labels, BorderLayout.WEST);
		panel.add(components, BorderLayout.CENTER);
		p.add(panel, BorderLayout.NORTH);
		return p;
	}

	private JPanel createUsagePanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile =
				new FilenameSelector(
						path,
						fnscf,
						newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		FilenameSelector filenameSelectorReportsDirectory = new FilenameSelector(path, fnscf, null);
		JPanel p =
				createComponentPanel(
						"usage",
						new ComponentData(
								"usage.filenameselectorreportsdirectory.label",
								filenameSelectorReportsDirectory),
						new ComponentData("usage.filenameselectorfile.label", filenameSelectorFile));
		p.add(createButtonPanel(buttonStart, () -> {
			try {
				textAreaOutput
						.setText(
								JRC
										.usage(
												filenameSelectorFile.getPath(),
												filenameSelectorReportsDirectory.getPath(),
												false));
			} catch (Exception e) {
				LOGGER.error("something went wrong while reading files for usage check!", e);
			}
		}, () -> filenameSelectorFile.getPath().toLowerCase().endsWith(".jrxml")), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createXMLPanel(String path) {
		JButton buttonStart = new JButton(getResource("buttons.start.label"));
		FilenameSelector filenameSelectorFile =
				new FilenameSelector(
						path,
						fnscf,
						newPath -> buttonStart.setEnabled(newPath.toLowerCase().endsWith(".jrxml")));
		FilenameSelector filenameSelectorReportsDirectory = new FilenameSelector(path, fnscf, null);
		JPanel p =
				createComponentPanel(
						"xml",
						new ComponentData(
								"xml.filenameselectorreportsdirectory.label",
								filenameSelectorReportsDirectory),
						new ComponentData("xml.filenameselectorfile.label", filenameSelectorFile));
		p.add(createButtonPanel(buttonStart, () -> {
			try {
				textAreaOutput
						.setText(JRC.xml(filenameSelectorFile.getPath(), filenameSelectorReportsDirectory.getPath()));
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