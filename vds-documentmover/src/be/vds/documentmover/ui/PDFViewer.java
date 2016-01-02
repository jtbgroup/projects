package be.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.utility.annotation.BorderPanel;

public class PDFViewer extends AbstractViewer {

	private SwingController controller;
	private CardLayout cardLayout;

	static { // a few system properties that should be enabled
		Defs.setProperty("java.awt.headless", "true");
		Defs.setProperty("org.icepdf.core.scaleImages", "false");
		Defs.setProperty("org.icepdf.core.paint.disableAlpha", "true");
		// set the graphic rendering hints for speed, we loose quite a bit of
		// quality
		// when converting to TIFF, so no point painting with the extra quality
		Defs.setProperty("org.icepdf.core.print.alphaInterpolation",
				"VALUE_ALPHA_INTERPOLATION_SPEED");
		Defs.setProperty("org.icepdf.core.print.antiAliasing",
				"VALUE_ANTIALIAS_ON");
		Defs.setProperty("org.icepdf.core.print.textAntiAliasing",
				"VALUE_TEXT_ANTIALIAS_OFF");
		Defs.setProperty("org.icepdf.core.print.colorRender",
				"VALUE_COLOR_RENDER_SPEED");
		Defs.setProperty("org.icepdf.core.print.dither", "VALUE_DITHER_DEFAULT");
		Defs.setProperty("org.icepdf.core.print.fractionalmetrics",
				"VALUE_FRACTIONALMETRICS_OFF");
		Defs.setProperty("org.icepdf.core.print.interpolation",
				"VALUE_INTERPOLATION_NEAREST_NEIGHBOR");
		Defs.setProperty("org.icepdf.core.print.render", "VALUE_RENDER_SPEED");
		Defs.setProperty("org.icepdf.core.print.stroke", "VALUE_STROKE_PURE");
	}

	// See more at:
	// http://www.icesoft.org/JForum/posts/list/20628.page#sthash.RlQw5c9V.dpuf

	public PDFViewer() {
		init();
	}

	private void init() {
		 cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		this.add(createViewer(), "viewer");
		this.add(createwaitingComp(), "wait");
	}

	private Component createViewer() {
		controller = new SwingController();
		// Build a SwingViewFactory configured with the controller
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		// Use the factory to build a JPanel that is pre-configured
		// with a complete, active Viewer UI.
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		// add copy keyboard command
		ComponentKeyBinding.install(controller, viewerComponentPanel);
		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller
						.getDocumentViewController()));
		
//		JPanel p = new JPanel(new BorderLayout());
//		p.add(viewerComponentPanel);
		JScrollPane p = new JScrollPane(viewerComponentPanel);
		return p;
	}

	private Component createwaitingComp() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("operation in progress..."), BorderLayout.CENTER);
		return p;
	}

	@Override
	public void showFile(final File file) {
		new Thread(new Runnable() {
			
			public void run() {
				cardLayout.show(PDFViewer.this, "wait");
				controller.openDocument(file.getAbsolutePath());
				cardLayout.show(PDFViewer.this, "viewer");
			}
		}).start();
		
	}

	@Override
	public void releaseFile() {
		controller.closeDocument();
	}
}
