/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.classes.Food;
import edu.apsu.csci.CalorieCounter.db.DbDataSource;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalorieHistoryActivity extends ListActivity {


    private DbDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_calorie_history);

        dataSource = new DbDataSource(getApplicationContext());


    }



/*
    @Override
    protected void onStop() {
        super.onStop();
        dataSource.close();
    }
*/
    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();

        List<Food> foods = dataSource.getAllFood();

        ArrayAdapter<Food> adapter = new ArrayAdapter<Food>(this,
                R.layout.activity_view_calorie_history,foods);
        setListAdapter(adapter);



    }

    public void addFood(Food food)
    {
        ArrayAdapter<Food> adapter = (ArrayAdapter<Food>) getListAdapter();

        adapter.add(food);
        adapter.notifyDataSetChanged();



    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {


        List<Food> foodList = dataSource.getAllFood();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Food food = (Food) l.getItemAtPosition(position);


        builder.setMessage( "Name: " + foodList.get(position).getName() + "\n"
                + "Calories: " + foodList.get(position).getCalories() + "\n"
                +"Date Created: " + foodList.get(position).getDateCreated()


        );

        builder.setPositiveButton("OK",null);
        builder.show();





    }

}
