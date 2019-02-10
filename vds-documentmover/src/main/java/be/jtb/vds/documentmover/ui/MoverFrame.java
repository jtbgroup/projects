package be.jtb.vds.documentmover.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import be.jtb.vds.documentmover.ApplicationManager;

public class MoverFrame extends JFrame {
	public static final String VIEW_ID_ACTIONS = "actions.controller";
	public static final String VIEW_ID_DOCUMENTS = "documents";
	public static final String VIEW_ID_SOURCE = "fileexplorer.source";
	public static final String VIEW_ID_DESTINATION = "fileexplorer.destination";
	private ApplicationManager applicationManager;
	private DocumentView documentViewerPanel;
	private AbstractFileExplorerView destinationExplorer;
	private AbstractFileExplorerView sourceExplorer;
	private ActionView actionPanel;
	private Map<String, View> views = new HashMap<String, View>();

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
	}

	private void initializeComponents() {
		initializeDocumentViewerPanel();
		initializeSourceExplorer();
		initializeDestinationExplorer();
		initializeActionPanel();
		DockingLayoutManager dockingLayoutManager = new DockingLayoutManager(this);
	}

	private void initializeActionPanel() {
		actionPanel = new ActionView(VIEW_ID_ACTIONS, "Actions");
		registerView(actionPanel);
	}

	private void registerView(View view) {
		views.put(view.getIdentifier(), view);
	}

	private void initializeDocumentViewerPanel() {
		documentViewerPanel = new DocumentView(VIEW_ID_DOCUMENTS, "Documents");
		registerView(documentViewerPanel);
	}

	private void initializeSourceExplorer() {
		sourceExplorer = new SourceFileExplorerView(VIEW_ID_SOURCE, "Source", true);
		registerView(sourceExplorer);
	}

	private void initializeDestinationExplorer() {
		destinationExplorer = new DestinationFileExplorerView(VIEW_ID_DESTINATION, "Destination", true);
		registerView(destinationExplorer);
	}

	private JMenuBar createMenuBar() {
		MoverMenuBar menuBar = new MoverMenuBar(applicationManager);
		return menuBar;
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


	public View getView(String identifier) {
		return views.get(identifier);
	}

}
