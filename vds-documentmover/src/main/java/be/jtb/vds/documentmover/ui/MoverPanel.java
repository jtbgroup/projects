package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import be.jtb.vds.documentmover.ConfigurationHelper;
import be.jtb.vds.documentmover.utils.FileUtils;

public class MoverPanel extends JPanel {

	private static final Logger LOGGER = Logger.getLogger(MoverPanel.class
			.getName());
	private DocumentViewerPanel documentViewerPanel;
	private FileExplorerPanel destinationExplorer;
	private JTextField destFolderLabel;
	private File sourceFile;
	// private DefaultMutableTreeNode destinationNode;
	// private JTextField newFileNameTf;
	private FileExplorerPanel sourceExplorer;
	private DefaultComboBoxModel patternComboModel;
	private JComboBox newFileNameComboBox;

	public MoverPanel() {
		initializeComponent();
	}

	private void initializeComponent() {
		this.setLayout(new BorderLayout());

		JSplitPane split = new JSplitPane();
		split.setLeftComponent(createLeftPanel());
		split.setRightComponent(createRightPanel());
		split.setDividerLocation(0.3);
		this.add(split, BorderLayout.CENTER);
	}

	private Component createRightPanel() {
		Component p = createDocumentViewerPanel();
		return p;
	}

	private Component createLeftPanel() {
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setLeftComponent(createFileExplorerSource());
		split.setRightComponent(createFileExplorerDestination());

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(split, BorderLayout.CENTER);
		p.add(createActionPanel(), BorderLayout.SOUTH);
		p.setMinimumSize(new Dimension(300, 100));
		return p;
	}

	private Component createFileExplorerDestination() {
		destinationExplorer = new FileExplorerPanel(true);
		destinationExplorer
				.addTreeSelectionListener(new TreeSelectionListener() {

					public void valueChanged(TreeSelectionEvent tse) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse
								.getPath().getLastPathComponent();
						// destinationNode = node;
						destinationFileChanged((File) node.getUserObject());
					}
				});

		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Destination"), BorderLayout.NORTH);
		p.add(destinationExplorer, BorderLayout.CENTER);
		return p;
	}

	private Component createActionPanel() {
		JPanel p = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		p.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager
				.addComponent(p, new JLabel("Dest Folder"), c, 0, 0, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, new JLabel("New Name"), c, 0, 1,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createDestFolderLabel(), c, 1, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		GridBagLayoutManager
				.addComponent(p, createNewFileNameTf(), c, 1, 1, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createMoveButton(), c, 0, 2, 2, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
		return p;
	}

	private Component createDestFolderLabel() {
		destFolderLabel = new JTextField();
		destFolderLabel.setEditable(true);
		// destinationExplorer
		// .addTreeSelectionListener(new TreeSelectionListener() {
		//
		// public void valueChanged(TreeSelectionEvent e) {
		// DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
		// .getPath().getLastPathComponent();
		// destinationNode = node;
		// destFolderLabel.setText(((File) destinationNode
		// .getUserObject()).getAbsolutePath());
		// }
		// });

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
				List<String> patterns = new LinkedList<String>(ConfigurationHelper.getInstance()
						.getFilePatterns());
				if(null == patterns){
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

		GridBagLayoutManager
				.addComponent(p, newFileNameComboBox, c, 0, 0, 1, 1, 1,
						0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager
		.addComponent(p, savePatternButton, c, 1, 0, 1, 1, 0,
				0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager
		.addComponent(p, copySourceFileNameButton, c, 2, 0, 1, 1, 0,
				0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		return p;

		// newFileNameTf = new JTextField();
		// newFileNameTf.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyPressed(KeyEvent e) {
		// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		// moveFile();
		// }
		// }
		// });
		// return newFileNameTf;

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
					.showMessageDialog(MoverPanel.this,
							"This File already exists. Not possible to replace an existing file.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Moving file ");
		sb.append("\r\n  " + sourceFile.getAbsolutePath());
		sb.append("\r\n    to " + newFileDest.getAbsolutePath());
		sb.append("\r\nPlease Confirm");
		LOGGER.info(sb.toString());
		int i = JOptionPane.showConfirmDialog(MoverPanel.this, sb.toString());
		if (i == JOptionPane.YES_OPTION) {
			try {
				documentViewerPanel.releaseFile();
				documentViewerPanel.showFile(newFileDest.getParentFile());
				// fileExplorerDestination.refreshFile(newFileDest.getParentFile());
				//

				FileUtils.moveFile(sourceFile, newFileDest);
				JOptionPane.showMessageDialog(MoverPanel.this,
						"File has been moved");
			} catch (IOException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(MoverPanel.this, e1.getMessage());
			}
		}
	}

	private Component createDocumentViewerPanel() {
		documentViewerPanel = new DocumentViewerPanel();
		return documentViewerPanel;
	}

	private Component createFileExplorerSource() {
		sourceExplorer = new FileExplorerPanel(true);
		sourceExplorer.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent tse) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse
						.getPath().getLastPathComponent();
				sourceFileChanged((File) node.getUserObject());

			}
		});
		sourceExplorer.setMinimumSize(new Dimension(200, 200));

		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Source"), BorderLayout.NORTH);
		p.add(sourceExplorer, BorderLayout.CENTER);
		return p;
	}

	private void destinationFileChanged(File file) {
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

	private void sourceFileChanged(File file) {
		documentViewerPanel.showFile(file);
		sourceFile = file;
		// newFileNameTf.setText(sourceFile.getName());
		if (newFileNameComboBox.getSelectedItem() == null) {
			patternComboModel.setSelectedItem(sourceFile.getName());
		}
	}

	public File getDestinationFolder() {
		return getTrueFolder(destinationExplorer.getSelectedFile());
	}

	private File getTrueFolder(File f) {
		if (f == null) {
			return null;
		}
		if (f.isFile()) {
			return f.getParentFile();
		}

		return f;
	}

	public File getSourceFolder() {
		return getTrueFolder(sourceExplorer.getSelectedFile());
	}

	public void setSourceFolder(String sourceFolder) {
		sourceExplorer.setSelectedFolder(sourceFolder);
	}

	public void setDestinationFolder(String destinationFolder) {
		destinationExplorer.setSelectedFolder(destinationFolder);
	}

}
