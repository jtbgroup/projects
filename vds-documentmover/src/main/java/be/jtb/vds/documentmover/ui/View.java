package be.jtb.vds.documentmover.ui;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ui.event.EventConsumer;
import be.jtb.vds.documentmover.ui.event.EventProducer;

public abstract class View extends JPanel implements EventProducer, EventConsumer{

	private static final Logger LOGGER = Logger.getLogger(View.class);
	private String identifier;
	private String name;
	
	public View(String identifier, String name) {
		this.identifier = identifier;
		this.name = name;
	}
	
	
	public String getIdentifier() {
		return this.identifier;
	}
	public String getName() {
		return this.name;
	}

	@Override
	public void notifyPreferencesChanged() {
		LOGGER.debug("Method notifyPreferencesChanged() not implemented for this view");
	}
	
	@Override
	public void notify(FileEvent fileEvent) {
		LOGGER.debug("Method notify(FileEvent fileEvent) not implemented for this view");
	}
}
