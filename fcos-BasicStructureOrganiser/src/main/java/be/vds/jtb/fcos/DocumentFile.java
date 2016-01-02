package be.vds.jtb.fcos;

import java.io.File;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class DocumentFile implements Comparable<DocumentFile> {

	private File file;
	private int id;

	public DocumentFile(File file, int id) {
		this.file = file;
		this.id = id;
	}

	@Override
	public String toString() {
		return (file.isDirectory()?"DIR : ":"FILE : " ) + id + " \t" + file.getAbsolutePath();
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}

	public String getTitle() {
		return file.getName();
	}

	public String getAlias() {
		String str = file.getName();
		return str.replaceAll("[^A-Za-z0-9]", "");
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public String getName() {
		return file.getName();
	}

	public int getId() {
		return id;
	}

	public String getParentAbsolutePath() {
		return file.getParent();
	}
	
	public int compareTo(DocumentFile o) {
		return this.getName().compareTo(o.getName());
	}
}
