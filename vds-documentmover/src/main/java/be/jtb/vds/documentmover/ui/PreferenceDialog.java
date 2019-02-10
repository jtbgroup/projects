package be.jtb.vds.documentmover.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import be.jtb.vds.documentmover.ConfigurationHelper;

public class PreferenceDialog extends JDialog {

	private FileSelector sourceFileSelector;
	private FileSelector destinationFileSelector;
	private FilePatternPanel filePatternPanel;
	private FavoritesPanel favoritesPanel;

	private PreferenceDialog() {
		initComponent();
	}

	private void initComponent() {
		this.getContentPane().add(createContentPane());
		loadValues();
	}

	private void loadValues() {
		if (ConfigurationHelper.getInstance().getSourceFolder() != null) {
			sourceFileSelector.setSelectedFile(new File(ConfigurationHelper
					.getInstance().getSourceFolder()));
		}

		if (ConfigurationHelper.getInstance().getDestinationFolder() != null) {
			destinationFileSelector.setSelectedFile(new File(
					ConfigurationHelper.getInstance().getDestinationFolder()));
		}
		
		filePatternPanel.setFilePatterns(ConfigurationHelper.getInstance()
				.getFilePatterns());
		
		favoritesPanel.setFavorites(ConfigurationHelper.getInstance().getFavoriteFolders());
	}

	private Component createContentPane() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int row = 0;
		GridBagLayoutManager.addComponent(p, createSourceFolderLabel(), c, 0,
				row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createSourceFolderComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createDestFolderLabel(), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createDestinationComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createFilePatternLabel(), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createFilePatternComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		
		GridBagLayoutManager.addComponent(p, new JLabel("Favorites"), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createFavoritesComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), c, 0, ++row, 2,
				1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createButtonsPanel(), c, 0, ++row,
				2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		return p;
	}

	private Component createFavoritesComponent() {
		favoritesPanel = new FavoritesPanel();
		return favoritesPanel;
	}
	
	private Component createFilePatternComponent() {
		filePatternPanel = new FilePatternPanel();
		return filePatternPanel;
	}

	private Component createFilePatternLabel() {
		return new JLabel("File Label");
	}

	private Component createButtonsPanel() {
		JButton cancelButton = new JButton(new AbstractAction("cancel") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreferenceDialog.this.dispose();
			}
		});

		JButton saveButton = new JButton(new AbstractAction("save") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					saveConfiguration();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							PreferenceDialog.this,
							"Error while saving the configuration:/r/n"
									+ e.getMessage());
				} finally {
					PreferenceDialog.this.dispose();
				}
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(cancelButton);
		p.add(saveButton);
		return p;
	}

	private void saveConfiguration() throws IOException {
		saveSourceFolder();
		saveDestinationFolder();
		saveFilePatterns();
		saveFavorites();

		ConfigurationHelper.getInstance().saveConfig();
	}

	private void saveFavorites() {
		ConfigurationHelper.getInstance().setFavoriteFolders(
				favoritesPanel.getFavorites());
	}

	private void saveFilePatterns() {
		ConfigurationHelper.getInstance().setFilePatterns(
				filePatternPanel.getFilePatterns());
	}

	private void saveDestinationFolder() {
		File destinationFolder = destinationFileSelector.getSelectedFile();
		ConfigurationHelper.getInstance().setDestinationFolder(
				destinationFolder == null ? "" : destinationFolder
						.getAbsolutePath());
	}

	private void saveSourceFolder() {
		File sourceFolder = sourceFileSelector.getSelectedFile();
		ConfigurationHelper.getInstance().setSourceFolder(
				sourceFolder == null ? "" : sourceFolder.getAbsolutePath());
	}

	private Component createSourceFolderComponent() {
		sourceFileSelector = new FileSelector();
		sourceFileSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return sourceFileSelector;
	}

	private Component createDestinationComponent() {
		destinationFileSelector = new FileSelector();
		destinationFileSelector
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		return destinationFileSelector;
	}

	private Component createSourceFolderLabel() {
		return new JLabel("Source folder");
	}

	private Component createDestFolderLabel() {
		return new JLabel("Destination folder");
	}

	public static int showDialog() {
		PreferenceDialog dlg = new PreferenceDialog();
		dlg.setModal(true);
		dlg.setSize(800, 400);
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		return 0;
	}
}
