/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findViewById(R.id.new_entry_button).setOnClickListener(new GoToActivity(this, AddFoodActivity.class));
        findViewById(R.id.new_entry_button).setOnClickListener(new GoToActivity(this, CalorieHistoryActivity.class));
        findViewById(R.id.new_entry_button).setOnClickListener(new GoToActivity(this, LicensingActivity.class));
    }

    public void onExitButton(final View view) {
        finish();
    }
}
