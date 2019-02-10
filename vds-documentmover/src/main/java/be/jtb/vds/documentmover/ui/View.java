package be.jtb.vds.documentmover.ui;

import javax.swing.JPanel;

public abstract class View extends JPanel implements EventProducer, EventConsumer{
	
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

}
