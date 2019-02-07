package be.jtb.vds.documentmover.ui;

import java.io.File;

import javax.swing.JPanel;

public abstract class AbstractViewer extends JPanel {
	public abstract void showFile(File file);

	public abstract void releaseFile();
}
