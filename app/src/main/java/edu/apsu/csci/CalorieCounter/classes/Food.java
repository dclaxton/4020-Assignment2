package edu.apsu.csci.CalorieCounter.classes;

public class Food {
    private String name;
    private int id;

    public Food() {
        this("", 0);
    }

    public Food(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int calories) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Food)
            return name.equals(((Food) o).name);
        else
            return false;
    }
}
