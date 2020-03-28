package edu.apsu.csci.CalorieCounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import edu.apsu.csci.CalorieCounter.classes.Food;

public class Database {
    private SQLiteDatabase db;
    private MySqlLiteHelper dbHelper;

    public Database(Context c) {
        dbHelper = new MySqlLiteHelper(c);
    }
    public void openDatabase(){
        db = dbHelper.getReadableDatabase();
    }
    public void closeDatabase() {
        db.close();
    }

    public ArrayList<Food> queryAllFoods() {
        ArrayList<Food> foods = new ArrayList<>();
        String[] foodNames = MySqlLiteHelper.DetailsColumns.names();

        Cursor cursor = db.query(MySqlLiteHelper.FOOD_DETAILS_TABLE,
                foodNames,null,null,null,null,
                MySqlLiteHelper.DetailsColumns.food_name.toString());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Food food = cursorToFood(cursor);
            foods.add(food);
            cursor.moveToNext();
        }
        cursor.close();
        return foods;
    }

    private Food cursorToFood(Cursor cursor) {
        Food food = new Food();

        food.setName(cursor.getString(MySqlLiteHelper.DetailsColumns.food_name.ordinal()));
        food.setId(cursor.getInt(MySqlLiteHelper.DetailsColumns.food_id.ordinal()));
        food.setCalories(cursor.getDouble(MySqlLiteHelper.DetailsColumns.food_calories.ordinal()));
        food.setDateCreated(cursor.getString(MySqlLiteHelper.DetailsColumns.data_created.ordinal()));

        return food;
    }
    //database name

    //database tables

    //Columns


    //create samples to start off

    //functions to help

}
