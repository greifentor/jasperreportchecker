package de.ollie.jrc.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.Generated;

@Generated // OLI: excluding GUI from unit testing.
public class FilenameSelector extends JPanel {

	public interface FilenameSelectorObserver {

		void pathChanged(String path);

	}

	private boolean strongdisabled = false;
	private JButton button = null;
	private JButton buttonClear = null;
	private JTextField textField = null;
	private javax.swing.filechooser.FileFilter filefilter = null;
	private FilenameSelectorObserver observer;
	private String path = ".";

	public FilenameSelector(String path, FilenameSelectorComponentFactory fsecf, FilenameSelectorObserver observer) {
		super(new BorderLayout(JRCFrame.HGAP, JRCFrame.VGAP));
		this.observer = observer;
		this.path = path;
		textField = fsecf.createTextField(this);
		textField.setText(path);
		textField.setPreferredSize(new Dimension(123, 24));
		textField.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F4) {
					button.doClick();
				}
			}
		});
		button = fsecf.createSelectButton(this);
		button.setPreferredSize(new Dimension(123, 24));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				doFilenameSelection();
			}
		});
		buttonClear = fsecf.createClearButton(this);
		buttonClear.setPreferredSize(new Dimension(123, 24));
		buttonClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				doClear();
			}
		});
		JPanel panelMain = new JPanel(new BorderLayout(JRCFrame.HGAP, JRCFrame.VGAP));
		panelMain.add(textField);
		JPanel panelButtons = new JPanel(new GridLayout(1, 2, JRCFrame.HGAP, JRCFrame.VGAP));
		panelButtons.add(buttonClear);
		panelButtons.add(button);
		panelMain.add(panelButtons, BorderLayout.EAST);
		add(panelMain);
		addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				textField.requestFocus();
			}
		});
	}

	public void doClear() {
		path = ".";
		textField.setText(path);
	}

	public void doFilenameSelection() {
		int returnVal = -1;
		JFileChooser fc = new JFileChooser(path);
		String dn = null;
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(getFileFilter());
		fc.setCurrentDirectory(new File(path));
		returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dn = fc.getSelectedFile().getAbsolutePath();
			setText(dn);
		}
	}

	public String getPath() {
		return path;
	}

	public javax.swing.filechooser.FileFilter getFileFilter() {
		return filefilter;
	}

	public void setFileFilter(javax.swing.filechooser.FileFilter ff) {
		filefilter = ff;
	}

	public void setText(String s) {
		path = s;
		textField.setText(s);
		if (observer != null) {
			observer.pathChanged(s);
		}
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		textField.requestFocus();
	}

	@Override
	public boolean hasFocus() {
		return textField.hasFocus() || button.hasFocus() || buttonClear.hasFocus();
	}

	@Override
	public void setEnabled(boolean b) {
		if (b && strongdisabled) {
			return;
		}
		super.setEnabled(b);
		textField.setEnabled(b);
	}

}
