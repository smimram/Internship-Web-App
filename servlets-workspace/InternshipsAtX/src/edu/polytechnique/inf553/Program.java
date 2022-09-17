package edu.polytechnique.inf553;

import java.util.ArrayList;
import java.util.List;

public class Program {

    private int id;
    private String name;
    private String year;
    private List<Category> categories;
    private List<Person> students;

    public Program(int id, String name, String year) {
        this.name = name;
        this.id = id;
        this.year = year;
        this.categories = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public String getId() {
        return String.valueOf(id);
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

    public void addStudent(Person student) {
        this.students.add(student);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Person> getStudents() {
        return this.students;
    }
}
