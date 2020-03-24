package edu.apsu.csci.CalorieCounter.classes;

public class Food {
    private String name;
    private int calories;

    public Food() {
        this("", 0);
    }

    public Food(String name, int calories) {
        this.calories = calories;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Food)
            return name.equals(((Food) o).name);
        else
            return false;
    }
}
