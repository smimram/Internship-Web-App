package edu.polytechnique.inf553;

import javax.servlet.http.Part;

public class Subject {

	private String title;
	private String id;
	private String supervisorEmail;
	private String supervisorName;
	
	public Subject(String title, String id, String supervisorEmail, String supervisorName) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
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
}
