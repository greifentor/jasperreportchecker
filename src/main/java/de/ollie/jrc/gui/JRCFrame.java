package de.ollie.jrc.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.ollie.jrc.JRC;
import de.ollie.jrc.logger.Logger;

public class JRCFrame extends JFrame implements ListSelectionListener, WindowListener {

	private static final Logger LOGGER = Logger.getLogger(JRCFrame.class.getSimpleName());

	private JList<File> files;

	public JRCFrame(String dirName) {
		super("JRC");
		this.addWindowListener(this);
		setContentPane(createMainPanel(dirName));
		pack();
	}

	private JPanel createMainPanel(String dirName) {
		JPanel p = new JPanel(new BorderLayout(3, 3));
		files = createList(dirName);
		p.add(new JScrollPane(files), BorderLayout.CENTER);
		return p;
	}

	private JList<File> createList(String dirName) {
		JList<File> list = new JList<>();
		list.setListData(getFiles(dirName));
		list.setCellRenderer(new FileListCellRenderer());
		list.addListSelectionListener(this);
		return list;
	}

	private File[] getFiles(String dirName) {
		File d = new File(dirName);
		List<File> l = new ArrayList<>();
		try {
			dirName = new File(dirName).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateTitle(dirName);
		if (!isRootDirectory(dirName)) {
			l.add(new File(dirName + "/.."));
		}
		for (File f : d.listFiles()) {
			if (f.isDirectory() || f.getName().toLowerCase().endsWith("jrxml")) {
				l.add(f);
			}
		}
		return l.toArray(new File[l.size()]);
	}

	private boolean isRootDirectory(String dirName) {
		return dirName.equals("/") || dirName.endsWith(":\\") || dirName.endsWith(":/");
	}

	private void updateTitle(String dirName) {
		setTitle("JRC - " + dirName);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == files) {
			File f = files.getSelectedValue();
			if (f != null) {
				if (f.isDirectory()) {
					files.setListData(getFiles(f.getAbsolutePath()));
				} else {
					JRC.checkForFile(f.getAbsolutePath(), false);
				}
			}
		}
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