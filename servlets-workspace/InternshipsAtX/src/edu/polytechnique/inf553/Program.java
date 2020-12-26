package edu.polytechnique.inf553;

import java.util.ArrayList;
import java.util.List;

public class Program {

	private String name;
	private String id;
	private String year;
	private List<Category> categories;
	
	public Program(String id, String name, String year) {
		this.name = name;
		this.id = id;
		this.year = year;
		this.categories = new ArrayList<Category>();
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getYear() {
		return year;
	}
	
	public void addCategory(Category category) {
		categories.add(category);
	}
	
	public int categorySize() {
		return categories.size();
	}
	public List<Category> getCategories() {
		return categories;
	}
	
}
