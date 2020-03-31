/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.listeners;

import android.app.Activity;
import android.view.View;

import edu.apsu.csci.CalorieCounter.activities.MenuActivity;

public class GoToActivityClosingPrevious extends GoToActivity {
    public GoToActivityClosingPrevious(Activity fromActivity, Class<? extends Activity> toActivityClass) {
        super(fromActivity, toActivityClass);
    }

    // Prevents a menu from going back to the previous activity when 'Exit' is pressed
    @Override
    public void onClick(View v) {
        if (!MenuActivity.isOpen) {
            super.onClick(v);
        }
        fromActivity.finish();
    }
}
