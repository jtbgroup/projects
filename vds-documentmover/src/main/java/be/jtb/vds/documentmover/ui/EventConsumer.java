package be.jtb.vds.documentmover.ui;

public interface EventConsumer {
	
	public String getIdentifier();
	void notify(FileEvent fileEvent);

}
