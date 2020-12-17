package edu.polytechnique.inf553;

import java.util.List;

public class SubjectsPerCategory {
	
	private String programId;
	private String categoryId;
	private List<Subject> subjects;
	
	public SubjectsPerCategory(String programId, String categoryId, List<Subject> subjects) {
		this.subjects = subjects;
		this.categoryId = categoryId;
		this.programId = programId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}
	
	public String getProgramId() {
		return programId;
	}
}
