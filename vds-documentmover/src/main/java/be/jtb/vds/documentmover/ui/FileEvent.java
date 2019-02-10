package be.jtb.vds.documentmover.ui;

import java.io.File;

public class FileEvent {

	public static final int FILE_MOVED = 0;
	public static final int FILE_WILL_MOVE = 1;
	public static final int SOURCEFILE_SELECTED = 3;
	public static final int DESTINATIONFILE_SELECTED = 4;
	private int fileEventType;
	private File sourceFile;
	private File destinationFile;

	public FileEvent(int fileEventType, File sourceFile, File destinationFile) {
		this.fileEventType = fileEventType;
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}
	
	public File getDestinationFile() {
		return destinationFile;
	}
	
	public int getFileEventType() {
		return fileEventType;
	}
}
