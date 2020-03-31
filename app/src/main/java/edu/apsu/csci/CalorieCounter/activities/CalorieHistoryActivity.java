/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;

import android.app.DatePickerDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.classes.Food;
import edu.apsu.csci.CalorieCounter.db.DbDataSource;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivityClosingPrevious;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class CalorieHistoryActivity extends AppCompatActivity {
    private DbDataSource dataSource;
    private final Calendar mCalendar = Calendar.getInstance();
    private String datePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calorie_history);

        // Initialize the DB source and initial date
        dataSource = new DbDataSource(this);
        datePicked = "";

        findViewById(R.id.to_menu_button).setOnClickListener(new GoToActivityClosingPrevious(this, MenuActivity.class));

        initializeCalendar();
    }

    // Yes, this is a duplicate from AddFoodActivity. In a perfect world, more classes would be used
    // to make sure the calendar is only initialized once
    private void initializeCalendar() {
        // Set up the calendar with date picker
        findViewById(R.id.set_date_edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpDialog = new DatePickerDialog(
                        CalorieHistoryActivity.this, date, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());
                dpDialog.show();
            }
        });

        // Initialize arrow image buttons to increase/decrease date
        findViewById(R.id.right_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDate(1);
            }
        });

        findViewById(R.id.left_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementDate(-1);
            }
        });
    }

    // Handles the setting of the date
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            datePicked = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
            updateDate();
        }
    };

    // Places the chosen date from the calender in the edit text and indicates the weekday
    private void updateDate() {
        ((EditText) findViewById(R.id.set_date_edit_text)).setText(
                new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(mCalendar.getTime()));
        ((TextView) findViewById(R.id.weekday_text_view)).setText(
                new SimpleDateFormat("EEEE", Locale.US).format(mCalendar.getTime()));
        getFoods();
    }

    // Increases the date by a factor of i (can be negative to decrease)
    private void incrementDate(int i) {
        EditText dateText = findViewById(R.id.set_date_edit_text);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(dateText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.add(Calendar.DATE, i);

        if (c.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            dateText.setText(sdf.format(c.getTime()));
            datePicked = dateText.getText().toString();
            datePicked = datePicked.substring(1);
            getFoods();
        }
    }

    // Gets a list of foods from the database
    public void getFoods()
    {
        TextView tv = findViewById(R.id.textViewfood);
        TextView calorieCount = findViewById(R.id.textViewTotalCalories);
        DecimalFormat df2 = new DecimalFormat("#.##");

        List<Food> foods = dataSource.getAllFood();
        ArrayList<String> arrayList = new ArrayList<>();

        for(Food food: foods)
        {
            arrayList.add(food.getName() + "   " + food.getCalories() + " Cal" + "\n");
        }

        // Set the default text for a day to no calories logged
        tv.setText("");
        calorieCount.setText("Calories: None logged for today!");

        // Log applicable calories on the given day
        double CalCount = 0;
        int i = 0;
        for(Food food: foods)
        {
            if(food.getDateCreated().compareTo(datePicked) == 0) {
                tv.append(food.getName() + "   " + food.getCalories() + " Cal" + "\n");
                CalCount += food.getCalories();
                calorieCount.setText("Calories: " + df2.format(CalCount) + "");
            }
            i++;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }
}
