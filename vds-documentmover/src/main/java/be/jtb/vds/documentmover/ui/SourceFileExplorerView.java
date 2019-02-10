package be.jtb.vds.documentmover.ui;

public class SourceFileExplorerView extends AbstractFileExplorerView {

	public SourceFileExplorerView(String identifier, String name, boolean filesVisible) {
		super(identifier, name, filesVisible);
	}

	@Override
	protected void notifySelectionChanged() {
		EventManager.getInstance().notifySourceFileSelected(this, getSelectedFile());
	}
	
	@Override
	public void notifyCustom(FileEvent fileEvent) {
		int type = fileEvent.getFileEventType();
		if(type == FileEvent.SOURCEFILE_SELECTED) {
			setSelectedFolder(fileEvent.getSourceFile());
		}
	}
}
