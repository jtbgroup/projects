package be.jtb.vds.documentmover.ui;

public class Favorite implements Comparable<Favorite>{
	private String name;
	private String path;
	
	
	public Favorite(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(Favorite favorite) {
		return this.getName().compareTo(favorite.getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
