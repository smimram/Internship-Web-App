package edu.polytechnique.inf553;

public class Category {

	private String name;
	private String id;
	
	public Category(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
