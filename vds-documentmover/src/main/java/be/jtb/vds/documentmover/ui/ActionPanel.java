package be.jtb.vds.documentmover.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ConfigurationHelper;
import be.jtb.vds.documentmover.utils.FileUtils;
import be.jtb.vds.documentmover.utils.MyParser;

public class ActionPanel extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(ActionPanel.class.getName());
	private JTextField destFolderLabel;
	private DefaultComboBoxModel patternComboModel;
	private JComboBox senderComboBox;
	private JTextField senderTextField;
	private File sourceFile;
	private JButton savePatternButton;
	private JButton copySourceFileNameButton;
	private JTextField dtgTextField;
	private JTextField newFileNameTextField;
	private JTextField extensionTextField;

	public ActionPanel() {
		initializeComponents();
	}

	private void initializeComponents() {
		createComponents();

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, new JLabel("Dest Folder"), c, 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createDestFolderLabel(), c, 1, 0, 7, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		int i = 0;
		GridBagLayoutManager.addComponent(this, new JLabel("New Name"), c, i++, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, dtgTextField, c, i++, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, new JLabel("_"), c, i++, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, senderTextField, c, i++, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, new JLabel("_"), c, i++, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, newFileNameTextField, c, i++, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, new JLabel("."), c, i++, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, extensionTextField, c, i++, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, savePatternButton, c, i++, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, copySourceFileNameButton, c, i++, 1, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createMoveButton(), c, 0, 2, 7, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		
		GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), c, 0, 3, 7, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
	}

	private Component createDestFolderLabel() {
		destFolderLabel = new JTextField();
		destFolderLabel.setEditable(true);

		return destFolderLabel;
	}

	private void createComponents() {
		dtgTextField = new JTextField();

		newFileNameTextField = new JTextField();

//		patternComboModel = new DefaultComboBoxModel();
//		loadPatterns();
//		senderComboBox = new JComboBox(patternComboModel);
//		senderComboBox.setEditable(true);
		
		senderTextField = new JTextField();
		extensionTextField = new JTextField();

		savePatternButton = new JButton(new AbstractAction("Save pattern") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newPattern = (String) senderComboBox.getSelectedItem();
				List<String> patterns = new LinkedList<String>(ConfigurationHelper.getInstance().getFilePatterns());
				if (null == patterns) {
					patterns = new LinkedList<String>();
				}
				patterns.add(newPattern);
				Collections.sort(patterns);
				ConfigurationHelper.getInstance().setFilePatterns(patterns);
				try {
					ConfigurationHelper.getInstance().saveConfig();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loadPatterns();
			}
		});

		copySourceFileNameButton = new JButton(new AbstractAction("Source name") {

			@Override
			public void actionPerformed(ActionEvent e) {
				patternComboModel.setSelectedItem(sourceFile.getName());
			}
		});

//		JPanel p = new JPanel();
//		GridBagLayout layout = new GridBagLayout();
//		p.setLayout(layout);
//		GridBagConstraints c = new GridBagConstraints();
//
//		GridBagLayoutManager.addComponent(p, newFileNameComboBox, c, 0, 0, 1,
//				1, 1, 0, GridBagConstraints.HORIZONTAL,
//				GridBagConstraints.CENTER);
//		GridBagLayoutManager.addComponent(p, savePatternButton, c, 1, 0, 1, 1,
//				0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
//		GridBagLayoutManager.addComponent(p, copySourceFileNameButton, c, 2, 0,
//				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
//				GridBagConstraints.CENTER);
//
//		return p;
	}

	private void loadPatterns() {
		patternComboModel.removeAllElements();
		List<String> patterns = ConfigurationHelper.getInstance().getFilePatterns();
		if (null != patterns) {
			Collections.sort(patterns);
			for (String string : patterns) {
				patternComboModel.addElement(string);
			}
		}
		patternComboModel.setSelectedItem(null);
	}

	private Component createMoveButton() {
		JButton moveBtn = new JButton(new AbstractAction("Move") {

			public void actionPerformed(ActionEvent e) {
				moveFile();
			}
		});
		return moveBtn;
	}

	private void moveFile() {
		// String fileName = newFileNameTf.getText();
		String fileName = (String) senderComboBox.getSelectedItem();

		File newFileDest = new File(destFolderLabel.getText() + File.separatorChar + fileName);

		if (newFileDest.exists()) {
			JOptionPane.showMessageDialog(ActionPanel.this,
					"This File already exists. Not possible to replace an existing file.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Moving file ");
		sb.append("\r\n  " + sourceFile.getAbsolutePath());
		sb.append("\r\n    to " + newFileDest.getAbsolutePath());
		sb.append("\r\nPlease Confirm");
		LOGGER.info(sb.toString());
		int i = JOptionPane.showConfirmDialog(ActionPanel.this, sb.toString());
		if (i == JOptionPane.YES_OPTION) {
			try {
				// documentViewerPanel.releaseFile();
				// documentViewerPanel.showFile(newFileDest.getParentFile());

				FileUtils.moveFile(sourceFile, newFileDest);
				JOptionPane.showMessageDialog(ActionPanel.this, "File has been moved");
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(ActionPanel.this, e1.getMessage());
			}
		}
	}

	public void destinationFileChanged(File file) {
		String parentPath = null;
		if (file.isDirectory()) {
			parentPath = file.getAbsolutePath();
		} else {
			parentPath = file.getParent();
		}
		destFolderLabel.setText(parentPath);

		if (!file.isDirectory()) {
			patternComboModel.setSelectedItem(file.getName());
		}
	}

	public void sourceFileChanged(File file) {
		sourceFile = file;
//		if (senderComboBox.getSelectedItem() == null) {
//			patternComboModel.setSelectedItem(sourceFile.getName());
//		}
		
		MyParser parser = new MyParser();
		parser.evaluate(sourceFile.getName());
		dtgTextField.setText(parser.getDtg());
		senderTextField.setText(parser.getSender());
		newFileNameTextField.setText(parser.getDescription());
		extensionTextField.setText(parser.getExtension());
	}

}
