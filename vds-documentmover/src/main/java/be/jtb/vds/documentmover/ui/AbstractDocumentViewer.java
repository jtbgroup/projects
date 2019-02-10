package be.jtb.vds.documentmover.ui;

import java.io.File;

import javax.swing.JPanel;

public abstract class AbstractDocumentViewer extends JPanel {
	abstract void showFile(File file);
	abstract void releaseFile();
}
