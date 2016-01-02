package be.vds.jtb.nasbackup;

import java.io.File;

public interface BackupEngineListener {

	void notifyFileBackuped(File copiedFile);
	
}
