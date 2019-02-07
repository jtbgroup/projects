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

	public MoverMenuBar(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
		initComponent();
	}

	private void initComponent() {
		this.add(createFileMenu());
	}

	private JMenu createFileMenu() {
	JMenu fileMenu = new JMenu("File");
	fileMenu.add(createPreferencesMenuItem());
	fileMenu.add(createExitMenuItem());
	
	return fileMenu;
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
