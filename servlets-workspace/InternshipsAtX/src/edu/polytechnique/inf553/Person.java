package edu.polytechnique.inf553;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
	private String name;
	private int id;
	private String role;
	private List<Program> programs;
	private boolean valid;
	
	public Person(String name, int id, String role, boolean valid) {
		this.name = name;
		this.id = id;
		this.role = role;
		this.valid = valid;
		this.programs = new ArrayList<Program>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public void addProgram(Program program) {
		programs.add(program);
	}
	
	public int programSize() {
		return programs.size();
	}
	public List<Program> getPrograms() {
		return programs;
	}
	
	

}
