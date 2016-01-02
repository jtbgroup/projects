package be.vds.documentmover.ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JPanel;

public class DocumentViewerPanel extends JPanel {
	private static final Logger LOGGER = Logger
			.getLogger(DocumentViewerPanel.class.getName());

	enum CARD {
		DEFAULT("default"), PDF("pdf");
		private String id;

		CARD(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}

	private Map<CARD, AbstractViewer> viewersMap = new HashMap<CARD, AbstractViewer>();
	private CardLayout cardLayout;
	private CARD currentCard;

	public DocumentViewerPanel() {
		initializeComponent();
	}

	private void initializeComponent() {
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		createViewer(CARD.DEFAULT);
		currentCard = CARD.DEFAULT;
		cardLayout.show(this, CARD.DEFAULT.getId());
	}

	private void createViewer(CARD card) {
		switch (card) {
		case DEFAULT:
			viewersMap.put(CARD.DEFAULT, new DefaultViewer());
			break;
		case PDF:
			viewersMap.put(CARD.PDF, new PDFViewer());
			break;
		}
		this.add(viewersMap.get(card), card.getId());
		LOGGER.finer("Viewer created for " + card);
	}

	public void showFile(File file) {
		CARD card = selectCard(file);
		if (null == viewersMap.get(card)) {
			createViewer(card);
		}
		currentCard = card;
		cardLayout.show(this, card.getId());
		viewersMap.get(card).showFile(file);
	}

	private CARD selectCard(File file) {
		if (file.getName().toLowerCase().endsWith(".pdf")) {
			return CARD.PDF;
		}
		return CARD.DEFAULT;
	}

	public void releaseFile() {
		viewersMap.get(currentCard).releaseFile();
	}
}
