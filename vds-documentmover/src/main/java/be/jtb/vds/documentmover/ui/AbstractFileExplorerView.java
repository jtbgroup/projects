package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.utils.ResourceManager;

public abstract class AbstractFileExplorerView extends View {
	private static final Logger LOGGER = Logger.getLogger(AbstractFileExplorerView.class.getName());
	private DefaultTreeModel treeModel;
	private JTree tree;
	private FileSystemView fileSystemView;
	private boolean filesVisible;
	private DefaultMutableTreeNode root;
	private boolean selectionNotificationBlocked;

	public AbstractFileExplorerView(String identifier, String name, boolean filesVisible) {
		super(identifier, name);
		this.filesVisible = filesVisible;
		initializeComponent();
		registerTreeSelectionListener();
		EventManager.getInstance().registerEventListener(this);
	}



	private void registerTreeSelectionListener() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (!selectionNotificationBlocked) {
					notifySelectionChanged();
				}
			}
		});
	}

	protected abstract void notifySelectionChanged();

	private void initializeComponent() {
		this.setLayout(new BorderLayout());
		this.add(createButtons(), BorderLayout.NORTH);
		this.add(createTree(), BorderLayout.CENTER);
	}

	private Component createButtons() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(new JButton(
				new AbstractAction("Refresh", ResourceManager.getInstance().getImageIcon("refresh_16x16.png")) {

					@Override
					public void actionPerformed(ActionEvent e) {
						refreshSelection();
					}
				}));
		return p;
	}

	private Component createTree() {
		// the File tree
		root = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(root);

		fileSystemView = FileSystemView.getFileSystemView();
//		// show the file system roots.
//		File[] roots = fileSystemView.getRoots();

		File[] roots = File.listRoots();

		for (File fileSystemRoot : roots) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
			root.add(node);
//			File[] files = fileSystemView.getFiles(fileSystemRoot, true);
//
//			for (File file : sortedFiles(files)) {
//				if (file.isDirectory()) {
//					node.add(new DefaultMutableTreeNode(file));
//				}
//			}
			//
		}

		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(createTreeSelectionListener());
		tree.addKeyListener(createRefreshKeyListener());
		tree.addKeyListener(createDeleteKeyListener());
		tree.setCellRenderer(new FileExplorerTreeCellRenderer());
		tree.expandRow(0);
		JScrollPane treeScroll = new JScrollPane(tree);

		// as per trashgod tip
		tree.setVisibleRowCount(15);
		return treeScroll;
	}

	private KeyListener createDeleteKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					if (node.isLeaf()) {
						File f = ((File) node.getUserObject());
						int i = JOptionPane.showConfirmDialog(null,
								"Are you sure you want to delete file\r\n" + f.getAbsolutePath(), "Confirm delete",
								JOptionPane.WARNING_MESSAGE);
						if (i == JOptionPane.YES_OPTION) {
							try {
								DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
								Files.delete(f.toPath());
								tree.setSelectionPath(new TreePath(parentNode.getPath()));
								refreshSelection();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}

				}
			}
		};
	}

	private KeyListener createRefreshKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					refreshSelection();
				}
			}
		};
	}

	/**
	 * Refreshes the selection or the parent
	 */
	protected void refreshSelection() {
		if (tree.isSelectionEmpty()) {
			LOGGER.debug("Nothing to refresh");
			return;
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		if (node.isLeaf()) {
			node = (DefaultMutableTreeNode) node.getParent();
		}
		boolean isExpanded = tree.isExpanded(new TreePath(node.getPath()));
		LOGGER.debug("[" + getIdentifier() + "] Path " + new TreePath(node.getPath()) + "is expanded : " + isExpanded);

		Rectangle rect = tree.getVisibleRect();
		node.removeAllChildren();
		selectionNotificationBlocked = true;
		treeModel.reload(node);
		selectionNotificationBlocked = false;
		setSelectedFolder((File)node.getUserObject());
		tree.scrollRectToVisible(rect);
//		jTreeValueChanged();

		if (isExpanded) {
			tree.expandPath(new TreePath(node.getPath()));
		}

		LOGGER.debug("[" + getIdentifier() + "] Refreshed node " + node.getUserObject().toString());
	}

	private void jTreeValueChanged() {
		if (tree.isSelectionEmpty()) {
			return;
		}

		File file = getSelectedFile();

		if (!file.isFile()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();

			if (node.getChildCount() == 0) {
				addAllChildNodes(node, file);
				LOGGER.debug("[" + getIdentifier() + "] added node for file " + file);
			}
		}
	}

	private void addAllChildNodes(DefaultMutableTreeNode parentNode, File parentfile) {
		for (File file : parentfile.listFiles()) {
			parentNode.add(new DefaultMutableTreeNode(file));
		}
	}

	private List<File> sortedFiles(File[] files) {
		List<File> f = Arrays.asList(files);
		Collections.sort(f, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return f;
	}

	private TreeSelectionListener createTreeSelectionListener() {
		TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent tse) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
				showChildren(node);
				// setFileDetails((File)node.getUserObject());
			}

		};
		return treeSelectionListener;
	}

	/**
	 * Add the files that are contained within the directory of this node. Thanks to
	 * Hovercraft Full Of Eels for the SwingWorker fix.
	 */
	private void showChildren(final DefaultMutableTreeNode node) {
		tree.setEnabled(false);
		// progressBar.setVisible(true);
		// progressBar.setIndeterminate(true);

		SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {

			@Override
			public Void doInBackground() {
				File file = (File) node.getUserObject();
				if (file.isDirectory()) {
					File[] files = fileSystemView.getFiles(file, true); // !!

					if (node.isLeaf()) {
						for (File child : sortedFiles(files)) {
							if (child.isDirectory() || filesVisible) {
								publish(child);
							}
						}
					}
					// setTableData(files);
				}
				return null;
			}

			@Override
			protected void process(List<File> chunks) {
				for (File child : chunks) {
					node.add(new DefaultMutableTreeNode(child));
				}
			}

			@Override
			protected void done() {
				// progressBar.setIndeterminate(false);
				// progressBar.setVisible(false);
				tree.setEnabled(true);
			}
		};
		worker.execute();
	}

	public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
		tree.addTreeSelectionListener(treeSelectionListener);
	}

	protected File getSelectedFile() {
		if (tree.getSelectionPath() == null) {
			return null;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		if (null == node) {
			return null;
		}

		return (File) node.getUserObject();
	}

	protected void setSelectedFolder(File folder) {
		if (null == folder) {
			return;
		}
		LOGGER.debug("selecting folder " + folder);
		List<File> l = new ArrayList<File>();
		l.add(folder);
		File parentFile = folder.getParentFile();
		while (parentFile != null) {
			l.add(parentFile);
			folder = parentFile;
			parentFile = folder.getParentFile();
		}
		Collections.reverse(l);

		DefaultMutableTreeNode parentNode = root;
		for (File file : l) {
			LOGGER.debug("open " + file);
			for (int i = 0; i < parentNode.getChildCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
				if (((File) node.getUserObject()).getAbsoluteFile().equals(file.getAbsoluteFile())) {
					buildNode(node);
					TreePath path = new TreePath(node.getPath());
					tree.expandPath(path);
					tree.setSelectionPath(path);
					tree.scrollPathToVisible(path);
					tree.invalidate();
					tree.repaint();
					tree.validate();
					parentNode = node;
					break;
				}
			}
		}
	}

	private void buildNode(DefaultMutableTreeNode node) {
		File file = (File) node.getUserObject();
		if (file.isDirectory()) {
			File[] files = fileSystemView.getFiles(file, true); // !!

			if (node.isLeaf()) {
				for (File child : sortedFiles(files)) {
					if (child.isDirectory() || filesVisible) {
						// publish(child);
						node.add(new DefaultMutableTreeNode(child));
					}
				}
			}
		}
	}
	
	@Override
	public void notify(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.FILE_MOVED) {
			refreshSelection();
			// TODO : refresh the source file instead of selection
		}else{
			notifyCustom(fileEvent);
		}
	}



	protected abstract void notifyCustom(FileEvent fileEvent);

}
