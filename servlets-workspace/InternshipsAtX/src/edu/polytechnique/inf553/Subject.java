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
	private Person affiliatedStudent;
	private boolean isConfidentialInternship;
	private boolean isConfidentialReport;

	public Subject(String title, String id, String supervisorEmail, String supervisorName, String programId, boolean adminValid, boolean sciValid, boolean isConfidentialInternship) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
		this.adminValid = adminValid;
		this.sciValid = sciValid;
		this.programId = programId;
		this.categories = new ArrayList<>();
		this.affiliatedStudent = null;
		this.isConfidentialInternship = isConfidentialInternship;
	}
	
	public Subject(String id, String title, String programId, boolean adminValid, boolean sciValid, boolean isConfidentialInternship) {
		this.title = title;
		this.id = id;
		this.adminValid = adminValid;
		this.sciValid = sciValid;
		this.programId = programId;
		this.categories = new ArrayList<>();
		this.affiliatedStudent = null;
		this.isConfidentialInternship = isConfidentialInternship;
	}
	
	public Subject(String title, String id, String supervisorEmail, String supervisorName, boolean isConfidentialInternship) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
		this.categories = new ArrayList<>();
		this.affiliatedStudent = null;
		this.isConfidentialInternship = isConfidentialInternship;
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

	public Person getAffiliatedStudent() { return this.affiliatedStudent; }
	
	public boolean isConfidentialInternship() {  return this.isConfidentialInternship; }

	public boolean isConfidentialReport() {  return this.isConfidentialReport; }

	public void addCategory(Category category) {
		categories.add(category);
	}

	public int categorySize() {
		return categories.size();
	}
	public List<Category> getCategories() {
		return categories;
	}

	public void setAffiliatedStudent(Person affiliatedStudent) {
		this.affiliatedStudent = affiliatedStudent;
	}
}
