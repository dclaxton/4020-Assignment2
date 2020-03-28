package edu.apsu.csci.CalorieCounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telecom.Call;
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

    public Food createFood(String foodName,int id,String dateCreated,double calories)
    {
        ContentValues contentValues = new ContentValues();

        //any data we want to put into the DB we add to content values
        //put in food name
        // put id into it
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_id.toString(),id);
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_name.toString(),foodName);

        // put cal into it
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_calories.toString(),calories);
        ///put in data created
        contentValues.put(MySqlLiteHelper.DetailsColumns.data_created.toString(),dateCreated);


        //fill out all fields then insert it

        //insert into food table food name, food id, datecreated, calories
         open();
         database.insert(MySqlLiteHelper.FOOD_DETAILS_TABLE,null,contentValues);


        //might need to seperate this into a different function called getfoodfromdb()
        String[] columnNames = MySqlLiteHelper.DetailsColumns.names();

        //get back the food we just created
        Cursor cursor = database.query(MySqlLiteHelper.FOOD_DETAILS_TABLE, columnNames,MySqlLiteHelper.DetailsColumns.food_id
                         + " = " + id,null,null,null,null);

        cursor.moveToFirst();

        Food food = cursorToFood(cursor);
        cursor.close();
        close();

        return food;

    }

    public void addFoodToDb(String foodString,int id,String dateCreated,double calories)
    {
        ContentValues contentValues = new ContentValues();

        //any data we want to put into the DB we add to content values
        //put in food name
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_name.toString(),foodString);
        ///put in data created
        contentValues.put(MySqlLiteHelper.DetailsColumns.data_created.toString(),dateCreated);
        //put id into it
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_id.toString(),id);
        //put cal into it
        contentValues.put(MySqlLiteHelper.DetailsColumns.food_calories.toString(),calories);
        //fill out all fields then insert it

        //insert into food table food name, food id, datecreated, calories

        database.insert(MySqlLiteHelper.FOOD_DETAILS_TABLE,null,contentValues);


    }

    private Food cursorToFood(Cursor cursor)
    {
        Food food = new Food();

        int foodId = cursor.getInt(MySqlLiteHelper.DetailsColumns.food_id.ordinal());
        food.setId(foodId);

        String foodName = cursor.getString(MySqlLiteHelper.DetailsColumns.food_name.ordinal());
        food.setName(foodName);

        String datestr = cursor.getString(MySqlLiteHelper.DetailsColumns.data_created.ordinal());
        food.setDateCreated(datestr);

        double calories = cursor.getDouble(MySqlLiteHelper.DetailsColumns.food_calories.ordinal());
        food.setCalories(calories);

        DateFormat dateFormat = new SimpleDateFormat("EEE MMM DD kk:mm:ss z yyyy", Locale.ENGLISH);

        try {
            Date date = dateFormat.parse(datestr);
            // can clear the comment when date is made for food class
            //food.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return food;

    }

}
