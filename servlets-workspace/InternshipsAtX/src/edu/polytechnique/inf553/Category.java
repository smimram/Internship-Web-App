package edu.polytechnique.inf553;

public class Category {

    private int id;
    private String name;

    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category cat = (Category) obj;
            return this.id == cat.id;
        }
        return false;
    }
}
