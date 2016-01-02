package be.vds.jtb.nasbackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BackupEngine {

	private File srcFolder;
	private File destFolder;
	private String userName;
	private char[] password;
	private Set<BackupEngineListener> backupEngineListeners = new HashSet<BackupEngineListener>();

	public BackupEngine() {
	}
	
	public void addBackupEngineListener (BackupEngineListener backupEnineListener){
		this.backupEngineListeners.add(backupEnineListener);
	}

	public void runEngine() throws FileNotFoundException, IOException{
		copyFiles(this.srcFolder, this.destFolder);
	}
	
	/**
	 * Copies the folder structure from src to destination. Notifies every copy (FILE copy & FOLDER creation)
	 * @param srcFolder
	 * @param destFolder
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void copyFiles(File srcFolder, File destFolder) throws FileNotFoundException, IOException {
		for (File child : srcFolder.listFiles()) {
			if(child.isDirectory()){
				File destFolderChild = new File(destFolder.getAbsolutePath()+File.separatorChar+child.getName());
				destFolderChild.mkdir();
				notifyListener(destFolder);
				copyFiles(child, destFolder);
			}else{
				File destFile = new File(destFolder.getAbsolutePath()+File.separatorChar+child.getName());
				Files.copy(child.toPath(), new FileOutputStream(destFile));
				notifyListener(destFile);
			}
		}
	}

	private void notifyListener(File copiedFile) {
		for (BackupEngineListener backupEngineListener : backupEngineListeners) {
			backupEngineListener.notifyFileBackuped(copiedFile);
		}
	}

	/**
	 * Counts the number of FILES & FOLDERS present in the source folder (src folder is not count)
	 * @param parentFolder
	 * @return
	 */
	public int countAllFiles(File parentFolder) {
		int counter = 0;
		for (File child : parentFolder.listFiles()) {
			counter++;
			if(child.isDirectory()){
				counter += countAllFiles(child);
			}
		}
		return counter;
	}

	public File getSrcFolder() {
		return srcFolder;
	}

	public void setSrcFolder(File srcFolder) {
		this.srcFolder = srcFolder;
	}

	public File getDestFolder() {
		return destFolder;
	}

	public void setDestFolder(File destFolder) {
		this.destFolder = destFolder;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

}
