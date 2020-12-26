package edu.polytechnique.inf553;

public class Person {
	
	private String name;
	private int id;
	private String role;
	
	public Person(String name, int id, String role) {
		this.name = name;
		this.id = id;
		this.role = role;
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
	
	

}
