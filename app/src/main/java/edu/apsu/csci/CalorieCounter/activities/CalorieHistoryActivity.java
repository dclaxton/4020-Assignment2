/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.classes.Food;
import edu.apsu.csci.CalorieCounter.db.DbDataSource;
import android.widget.ArrayAdapter;

public class CalorieHistoryActivity extends ListActivity {


    private DbDataSource dataSource;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new DbDataSource(getApplicationContext());


    }

    @Override
    protected void onStop() {
        super.onStop();

        dataSource.close();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataSource.open();

        List<Food> foods = dataSource.getAllFood();

        ArrayAdapter<Food> adapter = new ArrayAdapter<Food>(this,
                R.layout.activity_view_calorie_history,foods);
        setListAdapter(adapter);



    }



}
