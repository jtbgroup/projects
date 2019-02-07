package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import be.jtb.vds.documentmover.ApplicationManager;
import be.jtb.vds.documentmover.ConfigurationHelper;
import bibliothek.gui.dock.common.action.CAction;

public class MoverFrame extends JFrame {
	private ApplicationManager applicationManager;
	private MoverPanel moverPanel;
	private File sourceFile;
	private DocumentViewerPanel documentViewerPanel;
	private JPanel sourceExplorerPanel;
	private FileExplorerPanel destinationExplorer;
	private JPanel destinationExplorerPanel;
	private FileExplorerPanel sourceExplorer;
	private ActionPanel actionPanel;

	public MoverFrame(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
		intializeFrame();
	}

	private void intializeFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setJMenuBar(createMenuBar());
		this.setTitle("Document Manager");
		addWindowListener(createWindowsListener());

		initializeComponents();

		// getContentPane().add(createContent());
		loadDefaultValues();
	}

	private void initializeComponents() {
		initializeDocumentViewerPanel();
		initializeSourceExplorer();
		initializeDestinationExplorer();
		initializeActionPanel();
		DockingLayoutManager dockingLayoutManager = new DockingLayoutManager(
				this);
	}

	private void initializeActionPanel() {
		actionPanel = new ActionPanel();
	}

	private void initializeDocumentViewerPanel() {
		documentViewerPanel = new DocumentViewerPanel();
	}

	private void initializeSourceExplorer() {
		sourceExplorer = new FileExplorerPanel(true);
		sourceExplorer.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent tse) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tse
						.getPath().getLastPathComponent();
				sourceFileChanged((File) node.getUserObject());

			}
		});
		sourceExplorer.setMinimumSize(new Dimension(200, 200));

		sourceExplorerPanel = new JPanel(new BorderLayout());
		sourceExplorerPanel.add(new JLabel("Source"), BorderLayout.NORTH);
		sourceExplorerPanel.add(sourceExplorer, BorderLayout.CENTER);
	}

	private void initializeDestinationExplorer() {
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

		destinationExplorerPanel = new JPanel(new BorderLayout());
		destinationExplorerPanel.add(new JLabel("Destination"),
				BorderLayout.NORTH);
		destinationExplorerPanel.add(destinationExplorer, BorderLayout.CENTER);
	}

	private void destinationFileChanged(File file) {
		actionPanel.destinationFileChanged(file);

	}

	private void sourceFileChanged(File file) {
		documentViewerPanel.showFile(file);
		actionPanel.sourceFileChanged(file);
	}

	private JMenuBar createMenuBar() {
		MoverMenuBar menuBar = new MoverMenuBar(applicationManager);
		return menuBar;
	}

	private void loadDefaultValues() {
		// moverPanel.setDestinationFolder(ConfigurationHelper.getInstance()
		// .getDestinationFolder());
		// moverPanel.setSourceFolder(ConfigurationHelper.getInstance()
		// .getSourceFolder());

		destinationExplorer.setSelectedFolder(ConfigurationHelper.getInstance()
				.getDestinationFolder());
		System.out.println(ConfigurationHelper.getInstance()
				.getSourceFolder());
		sourceExplorer.setSelectedFolder(ConfigurationHelper.getInstance()
				.getSourceFolder());

	}

	private WindowListener createWindowsListener() {
		WindowAdapter wa = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				applicationManager.closeApplication();
			}
		};
		return wa;
	}

	// private JComponent createContent() {
	// moverPanel = new MoverPanel();
	// return moverPanel;
	// }

	public JPanel getFileExplorerSourcePanel() {
		return sourceExplorerPanel;
	}

	public JPanel getDocumentViewerPanel() {
		return documentViewerPanel;
	}

	public JPanel getFileExplorerDestinationPanel() {
		return destinationExplorerPanel;
	}

	public JPanel getActionPanel() {
		return actionPanel;
	}

}
