package be.jtb.vds.documentmover.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.utils.FileUtils;
import be.jtb.vds.documentmover.utils.MessageHelper;
import be.jtb.vds.documentmover.utils.MyParser;
import be.jtb.vds.documentmover.utils.ResourceManager;

public class ActionView extends View {
	private static final Logger LOGGER = Logger.getLogger(ActionView.class.getName());
	private JTextField destFolderLabel;
	private JTextField senderTextField;
	private File sourceFile;
	private JButton copySourceFileNameButton;
	private JTextField dtgTextField;
	private JTextField descriptionTextField;
	private JTextField extensionTextField;

	public ActionView(String identifier, String name) {
		super(identifier, name);
		initializeComponents();
		EventManager.getInstance().registerEventListener(this);
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
		GridBagLayoutManager.addComponent(this, descriptionTextField, c, i++, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, new JLabel("."), c, i++, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, extensionTextField, c, i++, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
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

		descriptionTextField = new JTextField();

		senderTextField = new JTextField();
		extensionTextField = new JTextField();

		copySourceFileNameButton = new JButton(new AbstractAction("Source name") {

			@Override
			public void actionPerformed(ActionEvent e) {
				destinationFileChanged(sourceFile);
			}
		});
	}

	private Component createMoveButton() {
		JButton moveBtn = new JButton(
				new AbstractAction("Move", ResourceManager.getInstance().getImageIcon("move_24x24.png")) {

					public void actionPerformed(ActionEvent e) {
						moveFile();
					}
				});
		return moveBtn;
	}

	private void moveFile() {
		String fileName = buildFileName();

		File newFileDest = new File(destFolderLabel.getText() + File.separatorChar + fileName);

		if (newFileDest.exists()) {
			MessageHelper.getInstance().displayMessage("This File already exists.\r\nNot possible to replace an existing file.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Moving file ");
		sb.append("\r\n\t" + sourceFile.getAbsolutePath());
		sb.append("\r\nto " + newFileDest.getAbsolutePath());
		LOGGER.debug(sb.toString());
		sb.append("\r\n\tPlease Confirm");
		int i = MessageHelper.getInstance().displayConfirm(sb.toString());
				
		if (i == MessageHelper.YES_OPTION) {
			try {
				notifyFileEvent(new FileEvent(FileEvent.FILE_WILL_MOVE, sourceFile, newFileDest));
				FileUtils.moveFile(sourceFile, newFileDest);
				LOGGER.info("File moved : " + sourceFile + " >> " + newFileDest);
				notifyFileEvent(new FileEvent(FileEvent.FILE_MOVED, sourceFile, newFileDest));
				MessageHelper.getInstance().displayMessage ("File has been moved");
			} catch (IOException e1) {
				e1.printStackTrace();
				MessageHelper.getInstance().displayMessage(e1.getMessage());
			}
		}
	}

	private void notifyFileEvent(FileEvent fileEvent) {
		EventManager.getInstance().notifyFileEvent(this, fileEvent);
	}

	private String buildFileName() {
		MyParser parser = new MyParser();
		String dtg = dtgTextField.getText();
		if (null != dtg && dtg.length() == 0) {
			dtg = null;
		}

		String sender = senderTextField.getText();
		if (null != sender && sender.length() == 0) {
			sender = null;
		}

		String description = descriptionTextField.getText();
		if (null != description && description.length() == 0) {
			description = null;
		}

		String extension = extensionTextField.getText();
		if (null != extension && extension.length() == 0) {
			extension = null;
		}
		parser.load(dtg, sender, description, extension);
		String fileName = parser.getFileName();
		return fileName;
	}

	private void destinationFileChanged(File file) {
		String parentPath = null;
		if (null == file) {
			return;
		}

		if (file.isDirectory()) {
			parentPath = file.getAbsolutePath();
		} else {
			parentPath = file.getParent();
		}
		destFolderLabel.setText(parentPath);

		if (!file.isDirectory()) {
			loadDestinationFileParts(file.getName());
		}
	}

	private void sourceFileChanged(File file) {
		sourceFile = file;
		loadDestinationFileParts(sourceFile.getName());
	}

	private void loadDestinationFileParts(String fileName) {
		MyParser parser = new MyParser();
		parser.evaluate(fileName);
		dtgTextField.setText(parser.getDtg());
		senderTextField.setText(parser.getSender());
		descriptionTextField.setText(parser.getDescription());
		extensionTextField.setText(parser.getExtension());
	}

	@Override
	public void notify(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.SOURCEFILE_SELECTED) {
			sourceFileChanged(fileEvent.getSourceFile());
		} else if (type == FileEvent.DESTINATIONFILE_SELECTED) {
			destinationFileChanged(fileEvent.getDestinationFile());
		}
	}
}
