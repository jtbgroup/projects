package be.jtb.vds.documentmover.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.utils.ConfigurationHelper;

public class FavoriteDialog extends JDialog {
	private static final Logger LOGGER = Logger.getLogger(FavoriteDialog.class);

	public static final int ERROR = -1;
	public static final int CANCEL = 0;
	public static final int SAVE = 1;
	
	private int action;

	private JTextField nameTextField;
	private JTextField folderTextField;

	public FavoriteDialog() {
		initComponent();
	}

	private void initComponent() {
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setAlwaysOnTop(true);

		this.getContentPane().add(createContentPane());
	}

	private Component createContentPane() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int row = 0;
		GridBagLayoutManager.addComponent(p, createNameLabel(), c, 0, row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createNameComponent(), c, 1, row, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createFolderLabel(), c, 0, ++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createFolderComponent(), c, 1, row, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), c, 0, ++row, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createButtonsPanel(), c, 0, ++row, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		return p;
	}

	private Component createFolderComponent() {
		folderTextField = new JTextField();
		return folderTextField;
	}

	private Component createNameComponent() {
		nameTextField = new JTextField();
		return nameTextField;
	}

	private Component createButtonsPanel() {
		JButton cancelButton = new JButton(new AbstractAction("cancel") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = CANCEL;
				dispose();
			}
		});

		JButton saveButton = new JButton(new AbstractAction("save") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = SAVE;
				dispose();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(cancelButton);
		p.add(saveButton);
		return p;
	}

	private Component createNameLabel() {
		return new JLabel("Name");
	}

	private Component createFolderLabel() {
		return new JLabel("Folder");
	}

	public int showDialog() {
		return showDialog(null, null);
	}

	public int showDialog(String name, String folder) {
		this.setFavorite(name, folder);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		return action;
	}

	public String getFavoriteFolder() {
		return folderTextField.getText();
	}

	public String getFavoriteName() {
		return nameTextField.getText();
	}

	private void setFavorite(String name, String folder) {
		nameTextField.setText(name);
		folderTextField.setText(folder);
	}
}
