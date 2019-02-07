package be.jtb.vds.documentmover;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigurationHelper {

	private Properties properties;
	private static ConfigurationHelper instance;

	private ConfigurationHelper() {
		properties = new Properties();
	}

	public static ConfigurationHelper getInstance() {
		if (null == instance) {
			instance = new ConfigurationHelper();
		}
		return instance;
	}

	private static final String CONFIG_FILE = System.getProperty("user.home")
			+ File.separatorChar + ".jtb" + File.separatorChar
			+ "documentMover" + File.separatorChar + "config.properties";

	public void saveConfig() throws IOException {
		File cf = new File(CONFIG_FILE);
		if (!cf.exists()) {
			File parent = cf.getParentFile();
			parent.mkdirs();
			cf.createNewFile();
		}

		properties.store(new FileOutputStream(cf),
				"Config for Document Mover application provided by Jt'B");
	}

	public void setSourceFolder(String sourceFolder) {
		properties.put("src.folder", sourceFolder);
	}

	public void setDestinationFolder(String destinationFolder) {
		properties.put("dest.folder", destinationFolder);
	}

	public String getDestinationFolder() {
		return properties.getProperty("dest.folder");
	}

	public String getSourceFolder() {
		return properties.getProperty("src.folder");
	}

	public void loadConfig() throws IOException {
		File cf = new File(CONFIG_FILE);
		if (cf.exists()) {
			properties.load(new FileInputStream(cf));
		}

	}

	public void setFilePatterns(List<String> filePatterns) {
		StringBuilder sb = new StringBuilder();
		for (String string : filePatterns) {
			sb.append(string).append(";");
		}
		properties.put("file.patterns", sb.toString());
	}
	
	public List<String> getFilePatterns(){
		String props = properties.getProperty("file.patterns");
		if(null == props){
			return null;
		}
		String[] patterns = props.split(";");
		return Arrays.asList(patterns);
	}
}
