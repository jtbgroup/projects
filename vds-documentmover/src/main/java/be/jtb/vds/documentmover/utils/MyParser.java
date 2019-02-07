package be.jtb.vds.documentmover.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyParser {

	private String fileName;
	private String extension;
	private String dtg;
	private String sender;
	private String description;
	private static final String PATTERN_DTG_SEN_DESC = "(\\d{4,8})_([a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*)_(.*)";
	private static final String PATTERN_DTG_SEN = "(\\d{4,8})_([a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*)";
	private static final String PATTERN_DTG_DESC = "(\\d{4,8})_(.*)";
	private static final String PATTERN_SEN_DESC = "([a-zA-Z0-9]*[a-zA-Z][a-zA-Z0-9]*)_(.*)";

	public void evaluate(String fileName) {
		clear();
		this.fileName = fileName;

		int idxDot = this.fileName.lastIndexOf(".");
		this.extension = (idxDot == -1) ? null : this.fileName.substring(idxDot + 1);

		String rawFileName = (idxDot == -1) ? fileName : this.fileName.substring(0, idxDot);

		Pattern pattern = Pattern.compile(PATTERN_DTG_SEN_DESC);
		Matcher matcher = pattern.matcher(rawFileName);
		if (matcher.matches()) {
			dtg = matcher.group(1);
			sender = matcher.group(2);
			description = matcher.group(3);
			return;
		}

		pattern = Pattern.compile(PATTERN_DTG_SEN);
		matcher = pattern.matcher(rawFileName);
		if (matcher.matches()) {
			dtg = matcher.group(1);
			sender = matcher.group(2);
			return;
		}

		pattern = Pattern.compile(PATTERN_DTG_DESC);
		matcher = pattern.matcher(rawFileName);
		if (matcher.matches()) {
			dtg = matcher.group(1);
			description = matcher.group(2);
			return;
		}

		pattern = Pattern.compile(PATTERN_SEN_DESC);
		matcher = pattern.matcher(rawFileName);
		if (matcher.matches()) {
			sender = matcher.group(1);
			description = matcher.group(2);
			return;
		}

		description = rawFileName;

	}

	private void clear() {
		fileName = null;
		extension = null;
		dtg=null;
		sender=null;
		description=null;
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
