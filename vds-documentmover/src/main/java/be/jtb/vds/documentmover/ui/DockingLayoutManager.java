package be.jtb.vds.documentmover.ui;

import java.awt.Color;

import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;

public class DockingLayoutManager {

	private MoverFrame parentFrame;
	private CControl control;

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

		SingleCDockable dock1 = createFileViewerDock();
		SingleCDockable dock2 = createFileBrowserSourceDock();
		SingleCDockable dock3 = createFileBrowserDestinationDock();
		SingleCDockable dock4 = createActionDock();

		control.addDockable(dock1);
		control.addDockable(dock2);
		control.addDockable(dock3);
		control.addDockable(dock4);

		dock1.setVisible(true);
		dock2.setLocation(CLocation.base().normalWest(0.20));
		dock2.setVisible(true);
		dock3.setLocation(CLocation.base().normalWest(0.20).south(0.5));
		dock3.setVisible(true);
		dock4.setLocation(CLocation.base().normalSouth(0.3));
		dock4.setVisible(true);

		
//		RootMenuPiece menu = new RootMenuPiece("Colors", false);
//		menu.add(new SingleCDockableListMenuPiece(control));
//		JMenuBar menuBar = new JMenuBar();
//		menuBar.add(menu.getMenu());
//		parentFrame.setJMenuBar(menuBar);
	}


	private SingleCDockable createActionDock() {
		View producer = parentFrame.getView(MoverFrame.VIEW_ID_ACTIONS);
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(
				producer.getIdentifier(), producer.getName(), producer);
		dockable.setCloseable(true);
		return dockable;
	}

	private SingleCDockable createFileBrowserDestinationDock() {
		View producer = parentFrame.getView(MoverFrame.VIEW_ID_DESTINATION);
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(
				producer.getIdentifier(), producer.getName(), producer);
		dockable.setCloseable(true);
		return dockable;
	}

	private SingleCDockable createFileBrowserSourceDock() {
		View producer = parentFrame.getView(MoverFrame.VIEW_ID_SOURCE);
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(
				producer.getIdentifier(), producer.getName(), producer);
		dockable.setCloseable(true);
		return dockable;
	}

	private SingleCDockable createFileViewerDock() {
		View producer = parentFrame.getView(MoverFrame.VIEW_ID_DOCUMENTS);
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(
				producer.getIdentifier(), producer.getName(), producer);
		dockable.setCloseable(true);
		return dockable;
	}

	public static SingleCDockable createDockable(String title, Color color) {
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(color);
		DefaultSingleCDockable dockable = new DefaultSingleCDockable(title,
				title, panel);
		dockable.setCloseable(true);
		return dockable;
	}
}
