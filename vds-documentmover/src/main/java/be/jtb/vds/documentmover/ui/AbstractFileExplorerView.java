package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ConfigurationHelper;
import be.jtb.vds.documentmover.utils.ResourceManager;

public abstract class AbstractFileExplorerView extends View implements FileExplorerListener {

	private static final Logger LOGGER = Logger.getLogger(AbstractFileExplorerView.class);
	private FileExplorer fileExplorer;

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
				new AbstractAction("Refresh", ResourceManager.getInstance().getImageIcon("refresh_16x16.png")) {

					@Override
					public void actionPerformed(ActionEvent e) {
						fileExplorer.refreshSelection();
					}
				}));

		p.add(new JButton(new AbstractAction("*") {

			@Override
			public void actionPerformed(ActionEvent e) {
				File f = fileExplorer.getSelectedFile();
				if (!f.isDirectory()) {
					f = f.getParentFile();
				}

				FavoriteDialog dlg = new FavoriteDialog();
				int i = dlg.showDialog(f.getName(), f.getAbsolutePath());
				if (i == FavoriteDialog.SAVE) {

					ConfigurationHelper.getInstance().addFavorite(dlg.getFavoriteName(), dlg.getFavoriteFolder());
					try {
						ConfigurationHelper.getInstance().saveConfig();
					} catch (IOException e1) {
						LOGGER.error(e1.getMessage());
					}
				}
			}
		}));

		JComboBox favoritesComboBox = new JComboBox();
		favoritesComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				fileExplorer.setSelectedFolder(new File((String) e.getItem()));
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

	protected abstract void notifyCustom(FileEvent fileEvent);

}
