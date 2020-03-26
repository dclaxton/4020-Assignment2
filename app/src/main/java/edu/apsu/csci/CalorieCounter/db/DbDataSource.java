package edu.apsu.csci.CalorieCounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telecom.Call;

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

        Cursor cursor = database.query(MySqlLiteHelper.FOOD_DETAILS_TABLE,columns,null,null,null,null,null);
        cursor.moveToNext();

        while(!cursor.isAfterLast())
        {
            Food food = cursorToFood(cursor);
            foods.add(food);
            cursor.moveToNext();
        }

        cursor.close();


        return foods;

    }

    private Food cursorToFood(Cursor cursor)
    {
        Food food = new Food();

        int foodId = cursor.getInt(MySqlLiteHelper.DetailsColumns.food_id.ordinal());
        food.setId(foodId);

        String foodName = cursor.getString(MySqlLiteHelper.DetailsColumns.food_name.ordinal());
        food.setName(foodName);

        String datestr = cursor.getString(MySqlLiteHelper.DetailsColumns.data_created.ordinal());

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
