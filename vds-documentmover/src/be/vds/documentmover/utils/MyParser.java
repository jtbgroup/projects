package be.vds.documentmover.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyParser {

	private String fileName;
	private String extension;
	private String dtg;
	private String sender;
	private String description;

	public void evaluate(String fileName) {
		clear();
		this.fileName = fileName;

		int idxDot = this.fileName.lastIndexOf(".");
		this.extension = (idxDot == -1) ? null : this.fileName.substring(idxDot + 1);
		
		
		String rawFileName = this.fileName.substring(0, idxDot-1);
		System.out.println(rawFileName);
		Pattern pattern = Pattern.compile(rawFileName);
		Matcher matcher = pattern.matcher("(\\d{4,8})?((_)?([a-zA-Z0-9]*)?)((_)?(.*))");
		System.out.println(matcher.matches());
		
//		String[] parts = this.fileName.split("_");
//		if (parts.length > 3) {
//			String s = new String(parts[2]);
//			for (int i = 3; i < parts.length; i++) {
//				s = s + "_" + parts[i];
//			}
//			parts[2] = s;
//		}

	}

	private void clear() {
		fileName = null;
		extension = null;
	}

	public String getFileName() {
		return fileName;
	}

	public String getExtension() {
		return extension;
	}

	public String getSender() {
		return sender;
	}

	public String getDtg() {
		return dtg;
	}

	public String getDescription() {
		return description;
	}
}
