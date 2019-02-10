package be.jtb.vds.documentmover.ui;

import java.awt.CardLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DocumentView extends View {
	private static final Logger LOGGER = Logger
			.getLogger(DocumentView.class.getName());

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

	private Map<CARD, AbstractDocumentViewer> viewersMap = new HashMap<CARD, AbstractDocumentViewer>();
	private CardLayout cardLayout;
	private CARD currentCard;

	public DocumentView(String identifier, String name) {
		super(identifier, name);
		initializeComponent();
		registerAsListener();
	}

	private void registerAsListener() {
		EventManager.getInstance().registerEventListener(this);
		
	}
	
	@Override
	public void notify(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.FILE_WILL_MOVE) {
			clear();
			// TODO : is there a better option than null?		
		}else if(type == FileEvent.SOURCEFILE_SELECTED) {
			showFile(fileEvent.getSourceFile());
		}
	}

	private void initializeComponent() {
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		displayDefaultViewer();
	}

	private void displayDefaultViewer() {
		createViewer(CARD.DEFAULT);
		currentCard = CARD.DEFAULT;
		cardLayout.show(this, CARD.DEFAULT.getId());
	}

	private void createViewer(CARD card) {
		switch (card) {
		case DEFAULT:
			viewersMap.put(CARD.DEFAULT, new DefaultDocumentViewer());
			break;
		case PDF:
			viewersMap.put(CARD.PDF, new PDFDocumentViewer());
			break;
		}
		this.add(viewersMap.get(card), card.getId());
		LOGGER.info("Viewer created for " + card);
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
		if (file == null) {
			return CARD.DEFAULT;
		}else if (file.getName().toLowerCase().endsWith(".pdf")) {
			return CARD.PDF;
		}
		return CARD.DEFAULT;
	}

	public void releaseFile() {
		viewersMap.get(currentCard).releaseFile();
	}

	public void clear() {
		viewersMap.clear();	
		displayDefaultViewer();
		LOGGER.debug("viewers cleared and reset to default");
	}
}
