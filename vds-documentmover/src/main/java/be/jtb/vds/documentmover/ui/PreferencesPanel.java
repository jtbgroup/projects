package be.jtb.vds.documentmover.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import be.jtb.vds.documentmover.ui.event.EventManager;
import be.jtb.vds.documentmover.ui.event.EventProducer;
import be.jtb.vds.documentmover.utils.ConfigurationHelper;

public class PreferencesPanel extends JPanel{

	private FileSelector sourceFileSelector;
	private FileSelector destinationFileSelector;
	private FilePatternPanel filePatternPanel;
	private FavoritesPanel favoritesPanel;
	
	public PreferencesPanel() {
	initComponents();
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		int row = 0;
		GridBagLayoutManager.addComponent(this, createSourceFolderLabel(), c, 0,
				row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createSourceFolderComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createDestFolderLabel(), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createDestinationComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createFilePatternLabel(), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createFilePatternComponent(), c,
				1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		
		GridBagLayoutManager.addComponent(this, new JLabel("Favorites"), c, 0,
				++row, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createFavoritesComponent(), c,
				1, row, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
	}
	
	private Component createFavoritesComponent() {
		favoritesPanel = new FavoritesPanel();
		favoritesPanel.setPreferredSize(new Dimension(300, 300));
		return favoritesPanel;
	}
	
	private Component createFilePatternComponent() {
		filePatternPanel = new FilePatternPanel();
		return filePatternPanel;
	}

	private Component createFilePatternLabel() {
		return new JLabel("File Label");
	}
	

	public void saveConfiguration() throws IOException {
		saveSourceFolder();
		saveDestinationFolder();
		saveFilePatterns();
		saveFavorites();

		ConfigurationHelper.getInstance().saveConfig();
		EventManager.getInstance().notifyPreferencesChanged(new EventProducer() {
			
			@Override
			public String getName() {
				return "Preference Dialog";
			}
			
			@Override
			public String getIdentifier() {
				return "dialog.preference";
			}
		});
	}

	private void saveFavorites() {
		ConfigurationHelper.getInstance().setFavorites(
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
	
	public void loadValues() {
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
		
		favoritesPanel.setFavorites(ConfigurationHelper.getInstance().getFavorites());
	}
}
