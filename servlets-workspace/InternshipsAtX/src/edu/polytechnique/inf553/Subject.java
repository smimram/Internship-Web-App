package edu.polytechnique.inf553;

import javax.servlet.http.Part;

public class Subject {

	private String title;
	private String id;
	private String supervisorEmail;
	private String supervisorName;
	private byte[] content;
	
	public Subject(String title, String id, String supervisorEmail, String supervisorName, byte[] content) {
		this.title = title;
		this.id = id;
		this.supervisorEmail = supervisorEmail;
		this.supervisorName = supervisorName;
		this.content = content;
	}
	
	private String getTitle() {
		return title;
	}
	
	private String getId() {
		return id;
	}
	
	private String getSupervisorEmail() {
		return supervisorEmail;
	}
	
	private byte[] getContent() {
		return content;
	}
	
	private String getSupervisorName() {
		return supervisorName;
	}
}
