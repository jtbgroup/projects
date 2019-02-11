package be.jtb.vds.documentmover.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import be.jtb.vds.documentmover.ApplicationManager;

public class MoverMenuBar extends JMenuBar {

	private ApplicationManager applicationManager;
	private DockingLayoutManager dockingLayoutManager;

	public MoverMenuBar(ApplicationManager applicationManager, DockingLayoutManager dockingLayoutManager) {
		this.applicationManager = applicationManager;
		this.dockingLayoutManager = dockingLayoutManager;
		initComponent();
	}

	private void initComponent() {
		this.add(createFileMenu());
		this.add(createViewsMenu());
	}

	private JMenu createFileMenu() {
	JMenu fileMenu = new JMenu("File");
	fileMenu.add(createPreferencesMenuItem());
	fileMenu.add(createExitMenuItem());
	
	return fileMenu;
	}
	
	private JMenu createViewsMenu() {
		JMenu fileMenu = new JMenu("Views");
		fileMenu.add(createViewMenu("Actions",DockingLayoutManager.VIEW_ID_ACTIONS));
		fileMenu.add(createViewMenu("Source",DockingLayoutManager.VIEW_ID_SOURCE));
		fileMenu.add(createViewMenu("Destination",DockingLayoutManager.VIEW_ID_DESTINATION));
		fileMenu.add(createViewMenu("Documents",DockingLayoutManager.VIEW_ID_DOCUMENTS));
		fileMenu.add(createViewMenu("Preferences",DockingLayoutManager.VIEW_ID_PREFERENCES));
		
		return fileMenu;
		}

	private JMenuItem createViewMenu(String title, String viewId) {
		AbstractAction action = new AbstractAction(title) {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dockingLayoutManager.showView(viewId);
			}
		};
		JMenuItem menuItem = new JMenuItem(action);
		return menuItem;
	}

	private JMenuItem createPreferencesMenuItem() {
		AbstractAction action = new AbstractAction("preferences") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreferenceDialog.showDialog();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
		action.putValue(Action.ACCELERATOR_KEY, stroke);

		JMenuItem preferencesMenuItem = new JMenuItem(action);
		return preferencesMenuItem;
	}

	private JMenuItem createExitMenuItem() {
		JMenuItem exitMenuItem = new JMenuItem(new AbstractAction("exit") {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				applicationManager.closeApplication();
			}
		});
		return exitMenuItem;
	}

}
