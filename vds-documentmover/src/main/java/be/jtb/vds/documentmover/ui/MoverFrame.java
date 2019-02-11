package be.jtb.vds.documentmover.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import be.jtb.vds.documentmover.ApplicationManager;

public class MoverFrame extends JFrame {
	private ApplicationManager applicationManager;
private DockingLayoutManager dockingLayoutManager;

	public MoverFrame(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
		intializeFrame();
	}

	private void intializeFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dockingLayoutManager = new DockingLayoutManager(this);
		this.setTitle("Document Manager");
		addWindowListener(createWindowsListener());
		setJMenuBar(createMenuBar());
	}


	private JMenuBar createMenuBar() {
		MoverMenuBar menuBar = new MoverMenuBar(applicationManager, dockingLayoutManager);
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


}
