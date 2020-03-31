/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.classes;

import androidx.annotation.NonNull;

public class Food {
    private String name;
    private int id;
    private String dateCreated;
    private double calories;

    public Food() {
        this("", 0,"",0);
    }

    private Food(String name, int id,String dateCreated,double calories) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.calories = calories;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Food)
            return name.equals(((Food) o).name);
        else
            return false;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
