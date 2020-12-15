package edu.polytechnique.inf553;

import java.util.List;

public class SubjectsPerCategory {
	
	private String categoryId;
	private List<Subject> subjects;
	
	public SubjectsPerCategory(String categoryId, List<Subject> subjects) {
		this.subjects = subjects;
		this.categoryId = categoryId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}
}
