package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.utils.ConfigurationHelper;
import be.jtb.vds.documentmover.utils.ResourceManager;

public abstract class AbstractFileExplorerView extends View implements FileExplorerListener {

	private static final Logger LOGGER = Logger.getLogger(AbstractFileExplorerView.class);
	private FileExplorer fileExplorer;
	private JComboBox favoritesComboBox;
	private DefaultComboBoxModel<Favorite> favoritesComboBoxModel;
	private boolean selectionNotificationBlocked;

	public AbstractFileExplorerView(String identifier, String name, boolean filesVisible) {
		super(identifier, name);
		initializeComponents(filesVisible);
		EventManager.getInstance().registerEventListener(this);
	}

	private void initializeComponents(boolean filesVisible) {
		this.setLayout(new BorderLayout());
		this.add(createButtons(), BorderLayout.NORTH);
		this.add(createTree(filesVisible), BorderLayout.CENTER);
	}

	private Component createButtons() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JButton(
				new AbstractAction(null, ResourceManager.getInstance().getImageIcon("refresh_16x16.png")) {

					@Override
					public void actionPerformed(ActionEvent e) {
						fileExplorer.refreshSelection();
					}
				}));

		p.add(new JButton(new AbstractAction(null, ResourceManager.getInstance().getImageIcon("favorite_16x16.png") ) {

			@Override
			public void actionPerformed(ActionEvent e) {
				File f = fileExplorer.getSelectedFile();
				if (!f.isDirectory()) {
					f = f.getParentFile();
				}

				FavoriteDialog dlg = new FavoriteDialog();
				int i = dlg.showDialog(f.getName(), f.getAbsolutePath());
				if (i == FavoriteDialog.SAVE) {

					ConfigurationHelper.getInstance()
							.addFavorite(new Favorite(dlg.getFavoriteName(), dlg.getFavoriteFolder()));
					try {
						ConfigurationHelper.getInstance().saveConfig();
						EventManager.getInstance().notifyPreferencesChanged(new EventProducer() {

							@Override
							public String getName() {
								return "favorites";
							}

							@Override
							public String getIdentifier() {
								return "dialog.favorite";
							}
						});
					} catch (IOException e1) {
						LOGGER.error(e1.getMessage());
					}
				}
			}
		}));

		favoritesComboBoxModel = new DefaultComboBoxModel<Favorite>();
		favoritesComboBox = new JComboBox(favoritesComboBoxModel);
		favoritesComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!selectionNotificationBlocked) {
					fileExplorer.setSelectedFolder(new File(((Favorite) e.getItem()).getPath()), true);
				}
			}
		});
		p.add(favoritesComboBox);

		return p;
	}

	private Component createTree(boolean filesVisible) {
		fileExplorer = new FileExplorer(filesVisible);
		fileExplorer.addUniqueFileExploreristener(this);
		return fileExplorer;
	}

	@Override
	public void notify(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.FILE_MOVED) {
			fileExplorer.refreshSelection();
			// TODO : refresh the source file instead of selection
		} else {
			notifyCustom(fileEvent);
		}
	}

	protected File getSelectedFile() {
		return fileExplorer.getSelectedFile();
	}

	protected void setSelectedFolder(File folder) {
		fileExplorer.setSelectedFolder(folder);
	}

	protected void setFavorites(List<Favorite> favorites) {
		Favorite selection = (Favorite) favoritesComboBox.getSelectedItem();

		selectionNotificationBlocked = true;
		favoritesComboBoxModel.removeAllElements();
		for (Favorite favorite : favorites) {
			favoritesComboBoxModel.addElement(favorite);
		}
		favoritesComboBox.repaint();

		favoritesComboBox.setSelectedItem(selection);
		selectionNotificationBlocked = false;
	}

	protected abstract void notifyCustom(FileEvent fileEvent);

	@Override
	public void notifyPreferencesChanged() {
		setFavorites(ConfigurationHelper.getInstance().getFavorites());
	}

}
