package be.jtb.vds.documentmover.ui.event;

import be.jtb.vds.documentmover.ui.FileEvent;

public interface EventConsumer {
	
	public String getIdentifier();
	void notify(FileEvent fileEvent);
	public void notifyPreferencesChanged();

}
