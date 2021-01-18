package edu.polytechnique.inf553;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

public class Subject {

	private String title;
	private String id;
	private String supervisorEmail;
	private String supervisorName;
	private String programId;
	private boolean adminValid;
	private boolean sciValid;
	private List<Category> categories;
	
	public Subject(String title, String id, String supervisorEmail, String supervisorName, String programId, boolean adminValid, boolean sciValid) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
		this.adminValid = adminValid;
		this.sciValid = sciValid;
		this.programId = programId;
		this.categories = new ArrayList<>();
	}
	
	public Subject(String id, String title, String programId, boolean adminValid, boolean sciValid) {
		this.title = title;
		this.id = id;
		this.adminValid = adminValid;
		this.sciValid = sciValid;
		this.programId = programId;
		this.categories = new ArrayList<>();
	}
	
	public Subject(String title, String id, String supervisorEmail, String supervisorName) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
		this.categories = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}
	
	public String getId() {
		return id;
	}
	
	public String getSupervisorEmail() {
		return supervisorEmail;
	}
	
	public String getSupervisorName() {
		return supervisorName;
	}
	
	public String getProgramId() {
		return programId;
	}
	
	public boolean getAdminValid() {
		return adminValid;
	}

	public boolean getSciValid() {
		return sciValid;
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
