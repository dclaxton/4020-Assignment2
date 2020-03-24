/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class GoToActivity implements View.OnClickListener {
    private Activity fromActivity;
    private Class toActivityClass;

    public GoToActivity(Activity fromActivity, Class<? extends Activity> toActivityClass) {
        this.fromActivity = fromActivity;
        this.toActivityClass = toActivityClass;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(fromActivity, toActivityClass);
        fromActivity.startActivity(intent);
    }
}
