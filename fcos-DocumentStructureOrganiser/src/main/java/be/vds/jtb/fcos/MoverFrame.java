package be.vds.jtb.fcos;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MoverFrame extends JFrame {
	private static final String INSTRUCTION_TEXT = "1. Exécuter dans phpmyadmin les 2 queries SQL suivantes et exporter en CSV:\r\n     "
			+ "SELECT id, filename, catid FROM jml_phocadownload\r\n     "
			+ "SELECT id, parent_id, title FROM jml_phocadownload_categories\r\n"
			+ "   Params pour l'export en CSV: delimiter = ; --- pas de quotes --- encodage ISO5918-1\r\n"
			+ "2. remplir les params et exécuter\r\n"
			+ "3. exécuter le fichier update.sql dans phpmyadmin qui se trouve dans le folder tmpxxx au même niveau que userupload.";

	private DocumentMover documentMover;
	private FileSelector filesListSelector;
	private FileSelector categoryListSelector;
	private FileSelector userUploadPathSelector;

	private JTextArea messageTextArea;

	private FileSelector structuredPathSelector;

	private JButton executeButton;

	public MoverFrame(DocumentMover m) {
		this.documentMover = m;
		init();
	}

	private void init() {
		this.getContentPane().add(buildContentPane());
	}

	private Component buildContentPane() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createInstructionsPanel(), BorderLayout.NORTH);
		p.add(createResultPanel(), BorderLayout.CENTER);

		return p;
	}

	private Component createResultPanel() {
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setLeftComponent(createParamsPanel());
		split.setRightComponent(createMessagesPanel());
		return split;
	}

	private Component createMessagesPanel() {
		messageTextArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(messageTextArea);

		JPanel p = new JPanel(new BorderLayout());
		p.add(scroll, BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);

		return p;
	}

	private Component createButtonsPanel() {
		executeButton = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					documentMover.moveFiles();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		executeButton.setText("Move");
		executeButton.setEnabled(false);

		JButton clearButton = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				messageTextArea.setText(null);
			}
		});
		clearButton.setText("clear");

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(executeButton);
		p.add(clearButton);
		return p;
	}

	private void activateExecuteButton() {
		executeButton.setEnabled(documentMover.getMovingFiles() != null);
	}

	private Component createInstructionsPanel() {
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setText(INSTRUCTION_TEXT);
		return ta;
	}

	private Component createParamsPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(p, new JLabel(
				"Path of folder \"userupload\""), gc, 0, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createUserUploadPathSelector(),
				gc, 1, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new JLabel(
				"Path of folder \"structured\""), gc, 0, 1, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createStructuredPathSelector(),
				gc, 1, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, new JLabel("Categories List"), gc,
				0, 2, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createCategoryListSelector(), gc,
				1, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(p, new JLabel("Files List"), gc, 0, 3, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createFilesListSelector(), gc, 1,
				3, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createGoButton(), gc, 1, 4, 2, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);

		return p;
	}

	private Component createFilesListSelector() {
		filesListSelector = new FileSelector();
		filesListSelector
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(FileSelector.PROPERTY_FILE_CHANGED)) {
							documentMover.setFileList(new File((String)evt.getNewValue()));
						}
					}
				});
		return filesListSelector;
	}

	private Component createCategoryListSelector() {
		categoryListSelector = new FileSelector();
		categoryListSelector
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(FileSelector.PROPERTY_FILE_CHANGED)) {
							documentMover.setCatList(new File((String)evt.getNewValue()));
						}
					}
				});
		return categoryListSelector;
	}

	private Component createStructuredPathSelector() {
		structuredPathSelector = new FileSelector();
		structuredPathSelector
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		structuredPathSelector
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(FileSelector.PROPERTY_FILE_CHANGED)) {
							documentMover
									.setStructuredPath(new File((String)evt.getNewValue()));
						}
					}
				});
		return structuredPathSelector;
	}

	private Component createUserUploadPathSelector() {
		userUploadPathSelector = new FileSelector();
		userUploadPathSelector
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		userUploadPathSelector
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(FileSelector.PROPERTY_FILE_CHANGED)) {
							documentMover
									.setUseruploadPath(new File((String)evt.getNewValue()));
						}
					}
				});
		return userUploadPathSelector;
	}

	private Component createGoButton() {
		JButton b = new JButton(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					documentMover.generateFilesToMoveList();

					displayModifications();
					activateExecuteButton();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		b.setText("Go");
		return b;
	}

	private void displayModifications() {
		List<MovingFile> list = documentMover.getMovingFiles();
		for (MovingFile movingFile : list) {
			messageTextArea.append(movingFile.getCurrentFileName());
			messageTextArea.append("  ==>  ");
			messageTextArea.append(movingFile.getTargetFileName());
			messageTextArea.append("\r\n   ");
			messageTextArea.append(movingFile.getCurrentFile()
					.getAbsolutePath());
			messageTextArea.append("  ==>  ");
			messageTextArea
					.append(movingFile.getTargetFile().getAbsolutePath());
			messageTextArea.append("\r\n\r\n");

		}
	}

	public void closeWindow() {
		this.dispose();
		try {
			saveProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		firePropertyChange("windowClosed", this, null);
	}

	private void saveProperties() throws IOException {
		File f = getConfigFile(true);
		Properties props = new Properties();
		if (null != documentMover.getStructuredPath()) {
			props.setProperty("structured.folder", documentMover
					.getStructuredPath().getAbsolutePath());
		}

		if (null != documentMover.getUseruploadPath()) {
			props.setProperty("userupload.folder", documentMover
					.getUseruploadPath().getAbsolutePath());
		}

		if (null != documentMover.getCatList()) {
			props.setProperty("categories.list", documentMover.getCatList()
					.getAbsolutePath());
		}

		if (null != documentMover.getFileList()) {
			props.setProperty("files.list", documentMover.getFileList()
					.getAbsolutePath());
		}
		props.store(new FileWriter(f), "FCOS properties");
	}

	private File getConfigFile(boolean createIfNeeded) throws IOException {
		File folder = new File(System.getProperty("user.home") + File.separator
				+ ".fcos");
		if (createIfNeeded && !folder.exists()) {
			folder.mkdirs();
		}
		folder = new File(folder + File.separator + "config");
		if (createIfNeeded && !folder.exists()) {
			folder.createNewFile();
		}

		return folder;
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			closeWindow();
		} else {
			super.processWindowEvent(e);
		}
	}

	public void loadDefaultProperties() throws IOException {
		File f = getConfigFile(false);
		if (null != f && f.exists()) {
			Properties p = new Properties();
			p.load(new FileReader(f));
			if (null != p.getProperty("categories.list")){
				documentMover.setCatList(new File(p
						.getProperty("categories.list")));
				categoryListSelector.setSelectedFile(documentMover.getCatList());
			}

			if (null != p.getProperty("files.list")){
				documentMover
						.setFileList(new File(p.getProperty("files.list")));
				filesListSelector.setSelectedFile(documentMover.getFileList());
			}

			if (null != p.getProperty("structured.folder")){
				documentMover.setStructuredPath(new File(p
						.getProperty("structured.folder")));
				structuredPathSelector.setSelectedFile(documentMover.getStructuredPath());
			}

			if (null != p.getProperty("userupload.folder")){
				documentMover.setUseruploadPath(new File(p
						.getProperty("userupload.folder")));
				userUploadPathSelector.setSelectedFile(documentMover.getUseruploadPath());
			}

		}
	}
}
