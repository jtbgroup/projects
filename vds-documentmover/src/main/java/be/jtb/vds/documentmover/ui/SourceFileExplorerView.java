package be.jtb.vds.documentmover.ui;

import be.jtb.vds.documentmover.ui.event.EventManager;

public class SourceFileExplorerView extends AbstractFileExplorerView {

	public SourceFileExplorerView(String identifier, String name, boolean filesVisible) {
		super(identifier, name, filesVisible);
	}

	@Override
	public void notifySelectionChanged() {
		if (null != getSelectedFile()) {
			EventManager.getInstance().notifySourceFileSelected(this, getSelectedFile());
		}
	}

	@Override
	public void notifyCustom(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if (type == FileEvent.SOURCEFILE_SELECTED) {
			setSelectedFolder(fileEvent.getSourceFile());
		}
	}

}
