package be.jtb.vds.documentmover.ui;

import be.jtb.vds.documentmover.ui.event.EventManager;

public class DestinationFileExplorerView extends AbstractFileExplorerView {

	public DestinationFileExplorerView(String identifier, String name, boolean filesVisible) {
		super(identifier, name, filesVisible);
	}

	@Override
	public void notifySelectionChanged() {
		if (null != getSelectedFile()) {
			EventManager.getInstance().notifyDestinationFileSelected(this, getSelectedFile());
		}
	}

	@Override
	public void notifyCustom(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.DESTINATIONFILE_SELECTED) {
			setSelectedFolder(fileEvent.getDestinationFile());
		}
	}

}
