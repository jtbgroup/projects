package be.vds.documentmover.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.vds.documentmover.ConfigurationHelper;
import be.vds.documentmover.utils.FileUtils;

public class ActionPanel extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(ActionPanel.class
			.getName());
	private JTextField destFolderLabel;
	private DefaultComboBoxModel patternComboModel;
	private JComboBox newFileNameComboBox;
	private File sourceFile;

	public ActionPanel() {
		initializeComponents();
	}

	private void initializeComponents() {

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, new JLabel("Dest Folder"), c,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager
				.addComponent(this, new JLabel("New Name"), c, 0, 1, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createDestFolderLabel(), c, 1,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createNewFileNameTf(), c, 1, 1,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createMoveButton(), c, 0, 2, 2,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	}

	private Component createDestFolderLabel() {
		destFolderLabel = new JTextField();
		destFolderLabel.setEditable(true);

		return destFolderLabel;
	}

	private Component createNewFileNameTf() {
		patternComboModel = new DefaultComboBoxModel();
		loadPatterns();
		newFileNameComboBox = new JComboBox(patternComboModel);
		newFileNameComboBox.setEditable(true);

		JButton savePatternButton = new JButton(new AbstractAction(
				"Save pattern") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String newPattern = (String) newFileNameComboBox
						.getSelectedItem();
				List<String> patterns = new LinkedList<String>(
						ConfigurationHelper.getInstance().getFilePatterns());
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

		JButton copySourceFileNameButton = new JButton(new AbstractAction(
				"Source name") {

			@Override
			public void actionPerformed(ActionEvent e) {
				patternComboModel.setSelectedItem(sourceFile.getName());
			}
		});

		JPanel p = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		p.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, newFileNameComboBox, c, 0, 0, 1,
				1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, savePatternButton, c, 1, 0, 1, 1,
				0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, copySourceFileNameButton, c, 2, 0,
				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		return p;
	}

	private void loadPatterns() {
		patternComboModel.removeAllElements();
		List<String> patterns = ConfigurationHelper.getInstance()
				.getFilePatterns();
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
		String fileName = (String) newFileNameComboBox.getSelectedItem();

		File newFileDest = new File(destFolderLabel.getText()
				+ File.separatorChar + fileName);

		if (newFileDest.exists()) {
			JOptionPane
					.showMessageDialog(ActionPanel.this,
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
				JOptionPane.showMessageDialog(ActionPanel.this,
						"File has been moved");
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane
						.showMessageDialog(ActionPanel.this, e1.getMessage());
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
		if (newFileNameComboBox.getSelectedItem() == null) {
			patternComboModel.setSelectedItem(sourceFile.getName());
		}
	}

}
