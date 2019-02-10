package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JTextArea;

public class DefaultDocumentViewer extends AbstractDocumentViewer {

	private JTextArea label;

	public DefaultDocumentViewer() {
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		label = new JTextArea();
		label.setEditable(false);
		label.setWrapStyleWord(false);
		label.setLineWrap(true);
		this.add(label);
	}

	@Override
	public void showFile(File file) {
		label.setText(file == null ? "" : file.getAbsolutePath());
	}

	@Override
	public void releaseFile() {
		label.setText(null);
	}
}
