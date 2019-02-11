package be.jtb.vds.documentmover.ui.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ui.FileEvent;

public class EventManager {
	private static final Logger LOGGER = Logger.getLogger(EventManager.class);
	private static EventManager instance;
	private List<EventConsumer> eventConsumers = new ArrayList<EventConsumer>();

	private EventManager() {
	}

	public static EventManager getInstance() {
		if (null == instance) {
			instance = new EventManager();
		}
		return instance;
	}

	public void registerEventListener(EventConsumer eventConsumer) {
		eventConsumers.add(eventConsumer);
	}

	public void notifySourceFileSelected(EventProducer eventProducer, File sourceFile) {
		LOGGER.debug("New source file selected by " + eventProducer.getIdentifier() + ": " + sourceFile.getAbsolutePath());
		notifyFileEvent(eventProducer, new FileEvent(FileEvent.SOURCEFILE_SELECTED, sourceFile, null));
	}
	
	public void notifyDestinationFileSelected(EventProducer eventProducer, File destinationFile) {
		LOGGER.debug("New destination file selected by " + eventProducer.getIdentifier() + ": " + destinationFile.getPath());
		notifyFileEvent(eventProducer, new FileEvent(FileEvent.DESTINATIONFILE_SELECTED, null, destinationFile));
	}

	public void notifyFileEvent(EventProducer eventProducer, FileEvent fileEvent) {
		for (EventConsumer eventConsumer : eventConsumers) {
			if (eventProducer.getIdentifier() != eventConsumer.getIdentifier()) {
				eventConsumer.notify(fileEvent);
			}
		}
	}

	public void notifyPreferencesChanged(EventProducer producer) {
		for (EventConsumer eventConsumer : eventConsumers) {
				eventConsumer.notifyPreferencesChanged();
		}
	}
}
