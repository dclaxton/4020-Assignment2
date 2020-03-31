/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.apsu.csci.CalorieCounter.classes.Food;

public class DbDataSource {
    private SQLiteDatabase database;
    private MySqlLiteHelper databaseHelper;

    public DbDataSource(Context context)
    {
        databaseHelper = new MySqlLiteHelper(context);
    }

    public void open()
    {
        database = databaseHelper.getWritableDatabase();
    }

    public void close()
    {
        database.close();
    }

    // Gets all food from DB
    public List<Food> getAllFood() {
        List<Food> foods = new ArrayList<>();
        String columns[] = MySqlLiteHelper.DetailsColumns.names();
        Cursor cursor = database.query(MySqlLiteHelper.FOOD_DETAILS_TABLE,columns,null,null,null,null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Food food = cursorToFood(cursor);
            foods.add(food);
            cursor.moveToNext();
            Log.i("SQL CHECK","check: " + food.getCalories());
        }
        cursor.close();
        return foods;
    }

    // Inserts a food into the DB
    public void insertFood(String foodName, int id, String dateCreated, double calories) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_id.toString(), id);
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_name.toString(), foodName);
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_calories.toString(), calories);
        contentValues.put(MySqlLiteHelper.DetailsColumns.date_created.toString(), dateCreated);

        open();
        database.insert(MySqlLiteHelper.FOOD_DETAILS_TABLE, null, contentValues);
    }

    // Casts a Cursor to a Food object
    private Food cursorToFood(Cursor cursor)
    {
        Food food = new Food();

        int foodId = cursor.getInt(MySqlLiteHelper.DetailsColumns.food_id.ordinal());
        food.setId(foodId);

        String foodName = cursor.getString(MySqlLiteHelper.DetailsColumns.food_name.ordinal());
        food.setName(foodName);

        String dateStr = cursor.getString(MySqlLiteHelper.DetailsColumns.date_created.ordinal());
        food.setDateCreated(dateStr);

        double calories = cursor.getDouble(MySqlLiteHelper.DetailsColumns.food_calories.ordinal());
        food.setCalories(calories);

        return food;
    }
}
