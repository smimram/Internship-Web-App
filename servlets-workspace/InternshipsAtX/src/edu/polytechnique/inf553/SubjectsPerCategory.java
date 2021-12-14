package edu.polytechnique.inf553;

import java.util.List;

public class SubjectsPerCategory {
	
	private int programId;
	private int categoryId;
	private List<Subject> subjects;
	
	public SubjectsPerCategory(int programId, int categoryId, List<Subject> subjects) {
		this.subjects = subjects;
		this.categoryId = categoryId;
		this.programId = programId;
	}
	
	public String getCategoryId() {
		return String.valueOf(categoryId);
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}
	
	public String getProgramId() {
		return String.valueOf(programId);
	}
}
