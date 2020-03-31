package edu.apsu.csci.CalorieCounter.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivity;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivityClosingPrevious;

public class LicensingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_licensing);

        findViewById(R.id.to_menu_button).setOnClickListener(new GoToActivityClosingPrevious(this, MenuActivity.class));
    }
}
