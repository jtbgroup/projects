package be.jtb.vds.documentmover.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;

public class DockingLayoutManager {

	public static final String VIEW_ID_ACTIONS = "actions.controller";
	public static final String VIEW_ID_DOCUMENTS = "documents";
	public static final String VIEW_ID_SOURCE = "fileexplorer.source";
	public static final String VIEW_ID_DESTINATION = "fileexplorer.destination";
	public static final String VIEW_ID_PREFERENCES = "preferences";
	private MoverFrame parentFrame;
	private CControl control;
	private Map<String, SingleCDockable> views = new HashMap<String, SingleCDockable>();

	public DockingLayoutManager(MoverFrame parentFrame) {
		this.parentFrame = parentFrame;
		initController();
		initDefaultLayout();
	}

	private void initController() {
		control = new CControl(parentFrame);
		control.setTheme("eclipse");
		parentFrame.add(control.getContentArea());
	}

	private void initDefaultLayout() {

		SingleCDockable docDock = createDocumentViewerDock();
		SingleCDockable sourceDock = createFileBrowserSourceDock();
		SingleCDockable destDock = createFileBrowserDestinationDock();
		SingleCDockable actionsDock = createActionDock();

		CGrid grid = new CGrid(control);
		grid.add(0, 0, 3, 5, sourceDock);
		grid.add(0, 5, 3, 5, destDock);
		grid.add(3, 0, 7, 8, docDock);
		grid.add(3, 8, 7, 2, actionsDock);

		control.getContentArea().deploy(grid);

		docDock.setLocation(CLocation.base().normalWest(0.7).north(0.8));
		docDock.setVisible(true);
		sourceDock.setLocation(CLocation.base().normalEast(0.20).north(0.5));
		sourceDock.setVisible(true);
		destDock.setLocation(CLocation.base().normalEast(0.20).south(0.5));
		destDock.setVisible(true);
		actionsDock.setLocation(CLocation.base().normalWest(0.7).south(0.2));
		actionsDock.setVisible(true);
	}

	private SingleCDockable createActionDock() {
		if(views.get(VIEW_ID_ACTIONS) != null) {
			return views.get(VIEW_ID_ACTIONS);
		}
		
		ActionView actionPanel = new ActionView(VIEW_ID_ACTIONS, "Actions");
		DefaultSingleCDockable dockable = createCDockable(actionPanel);
		return dockable;
	}

	private SingleCDockable createFileBrowserDestinationDock() {
		if(views.get(VIEW_ID_DESTINATION) != null) {
			return views.get(VIEW_ID_DESTINATION);
		}
		
		DestinationFileExplorerView destinationExplorer = new DestinationFileExplorerView(VIEW_ID_DESTINATION, "Destination", true);
		DefaultSingleCDockable dockable = createCDockable(destinationExplorer);
		return dockable;
	}

	private SingleCDockable createFileBrowserSourceDock() {
		if(views.get(VIEW_ID_SOURCE) != null) {
			return views.get(VIEW_ID_SOURCE);
		}
		SourceFileExplorerView sourceExplorer = new SourceFileExplorerView(VIEW_ID_SOURCE, "Source", true);
		DefaultSingleCDockable dockable = createCDockable(sourceExplorer);
		return dockable;
	}

	private DefaultSingleCDockable createCDockable(View view) {
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(view.getIdentifier(), view.getName(), view);
		dockable.setCloseable(true);
		views.put(view.getIdentifier(), dockable);
		control.addDockable(dockable);
		return dockable;
	}

	private SingleCDockable createDocumentViewerDock() {
		if(views.get(VIEW_ID_DOCUMENTS) != null) {
			return views.get(VIEW_ID_DOCUMENTS);
		}
		DocumentView documentViewerPanel = new DocumentView(VIEW_ID_DOCUMENTS, "Documents");
		DefaultSingleCDockable dockable = createCDockable(documentViewerPanel);
		return dockable;
	}
	
	private SingleCDockable createPreferencesViewerDock() {
		if(views.get(VIEW_ID_PREFERENCES) != null) {
			return views.get(VIEW_ID_PREFERENCES);
		}
		PreferencesView view = new PreferencesView(VIEW_ID_PREFERENCES, "Preferences");
		DefaultSingleCDockable dockable = createCDockable(view);
		dockable.setLocation(views.get(VIEW_ID_DOCUMENTS).getBaseLocation());
		return dockable;
	}
	
	

//	public SingleCDockable createDockable(String title, Color color) {
//		JPanel panel = new JPanel();
//		panel.setOpaque(true);
//		panel.setBackground(color);
//		DefaultSingleCDockable dockable = new DefaultSingleCDockable(title, title, panel);
//		dockable.setCloseable(true);
//		control.addDockable(dockable);
//		return dockable;
//	}

	public void showView(String viewId) {
		SingleCDockable dock = null;
		
		switch (viewId) {
		case VIEW_ID_ACTIONS:
			dock = createActionDock();
			break;
		case VIEW_ID_DESTINATION:
			dock = createFileBrowserDestinationDock();
			break;
		case VIEW_ID_SOURCE:
			dock = createFileBrowserSourceDock();
			break;
		case VIEW_ID_DOCUMENTS:
			dock = createDocumentViewerDock();
			break;
		case VIEW_ID_PREFERENCES:
			dock = createPreferencesViewerDock();
			break;
		default:
			dock = createActionDock();
			break;
		}
		
		dock.setVisible(true);
		dock.getFocusComponent();
	}
}
