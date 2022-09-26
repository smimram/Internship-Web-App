package edu.polytechnique.inf553;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Program implements Comparable<Object> {

    private int id;
    private String name;
    private String year;
    private String description;
    private List<Category> categories;
    private List<Person> students;

    public Program(int id, String name, String year, String description) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Program) {
            Program prog = (Program) obj;
            return this.id == prog.id;
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.id, ((Program) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Program{" + "id=" + id + ", name='" + name + ", year=" + year + "}";
    }
}
