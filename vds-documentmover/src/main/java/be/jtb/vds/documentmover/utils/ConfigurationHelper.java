package be.jtb.vds.documentmover.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import be.jtb.vds.documentmover.ui.Favorite;

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

	private static final String CONFIG_FILE = System.getProperty("user.home") + File.separatorChar + ".jtb"
			+ File.separatorChar + "documentMover" + File.separatorChar + "config.properties";

	public void saveConfig() throws IOException {
		File cf = new File(CONFIG_FILE);
		if (!cf.exists()) {
			File parent = cf.getParentFile();
			parent.mkdirs();
			cf.createNewFile();
		}

		properties.store(new FileOutputStream(cf), "Config for Document Mover application provided by Jt'B");
	}

	public void setSourceFolder(String sourceFolder) {
		properties.put("src.folder", sourceFolder);
	}

	public void setDestinationFolder(String destinationFolder) {
		properties.put("dest.folder", destinationFolder);
	}

	public String getDestinationFolder() {
		String prop = properties.getProperty("dest.folder");
		if (null == prop) {
			prop = System.getProperty("user.home");
			setDestinationFolder(prop);
		}
		return prop;
	}

	public String getSourceFolder() {
		String prop = properties.getProperty("src.folder");
		if (null == prop) {
			prop = System.getProperty("user.home");
			setSourceFolder(prop);
		}
		return prop;
	}

	public void loadConfig() throws IOException {
		File cf = new File(CONFIG_FILE);
		if (cf.exists()) {
			properties.load(new FileInputStream(cf));
		} else {
			loadDefaultValues();
			saveConfig();
		}
	}

	private void loadDefaultValues() {
		setDestinationFolder(System.getProperty("user.home"));
		setSourceFolder(System.getProperty("user.home"));
	}

	public void setFilePatterns(List<String> filePatterns) {
		StringBuilder sb = new StringBuilder();
		for (String string : filePatterns) {
			sb.append(string).append(";");
		}
		properties.put("file.patterns", sb.toString());
	}

	public List<String> getFilePatterns() {
		String props = properties.getProperty("file.patterns");
		if (null == props) {
			return new ArrayList<String>();
		}
		String[] patterns = props.split(";");
		return Arrays.asList(patterns);
	}

	public List<Favorite> getFavorites() {
		String props = properties.getProperty("folders.favorites");
		if (null == props) {
			return new ArrayList<Favorite>();
		}

		List<Favorite> favorites = new ArrayList<Favorite>();
		String[] entries = props.split(";");
		for (String entry : entries) {
			String[] split = entry.split(">");
			if (entry.length() > 0 && split.length == 2) {
				favorites.add(new Favorite(split[0], split[1]));
			}
		}
		Collections.sort(favorites);
		return favorites;
	}

	public void setFavorites(List<Favorite> favorites) {
		StringBuilder sb = new StringBuilder();
		int count = favorites.size() - 1;
		for (Favorite favorite : favorites) {
			sb.append(favorite.getName() + ">" + favorite.getPath());
			if (count-- > 0) {
				sb.append(";");
			}
		}
		properties.put("folders.favorites", sb.toString());
	}

	public void addFavorite(Favorite favorite) {
		List<Favorite> favs = getFavorites();
		favs.add(favorite);
		setFavorites(favs);
	}
}
