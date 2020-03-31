/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.classes;

import java.util.ArrayList;

// The data returned from the QueryJSON AsyncTask
public class JSONResultData {
    public ArrayList<String> foodTitles = new ArrayList<>();
    public ArrayList<String> companyNames = new ArrayList<>();
    public ArrayList<Integer> foodIDs = new ArrayList<>();

    public double caloriesPer100g;
    public double servingSizeWeight;
}
