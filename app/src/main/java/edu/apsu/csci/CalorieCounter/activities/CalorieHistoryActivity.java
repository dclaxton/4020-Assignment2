/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
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

import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class CalorieHistoryActivity extends AppCompatActivity {


    private DbDataSource dataSource;
    private final Calendar mCalendar = Calendar.getInstance();
    private String datePicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calorie_history);

        dataSource = new DbDataSource(getApplicationContext());
        datePicked = "";

        findViewById(R.id.to_menu_button).setOnClickListener(new GoToActivityClosingPrevious(this, MenuActivity.class));

        findViewById(R.id.set_date_edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CalorieHistoryActivity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.right_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dateText = findViewById(R.id.set_date_edit_text);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dateText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 1);
                dateText.setText(sdf.format(c.getTime()));
                datePicked = dateText.getText().toString();
                datePicked = datePicked.substring(1,datePicked.length());
                getFoods();
            }
        });

        findViewById(R.id.left_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dateText = findViewById(R.id.set_date_edit_text);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(dateText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, -1);
                dateText.setText(sdf.format(c.getTime()));
                datePicked = dateText.getText().toString();
                datePicked = datePicked.substring(1,datePicked.length());
                getFoods();
            }
        });
    }

    public void getFoods()
    {
        List<Food> foods = dataSource.getAllFood();

        ArrayList<String> arrayList = new ArrayList<>();

        for(Food food: foods)
        {
            arrayList.add(food.getName() + "   " + food.getCalories() + " Cal" + "\n");
        }

        TextView tv = findViewById(R.id.textViewfood);
        ScrollView sv = findViewById(R.id.scrollviewfoods);
        TextView calorieCount = findViewById(R.id.textViewTotalCalories);
        double CalCount = 0;
        DecimalFormat df2 = new DecimalFormat("#.##");
        int i = 0;
        tv.setText("");
        calorieCount.setText("Calories: None logged for today!");
        for(Food food: foods)
        {

            if(food.getDateCreated().compareTo(datePicked) == 0) {
                tv.append(food.getName() + "   " + food.getCalories() + " Cal" + "\n");
                //tv.setText(arrayList.get(i));
                CalCount += food.getCalories();
                calorieCount.setText("Calories: " + df2.format(CalCount) + "");
            }
            i++;
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            datePicked= (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
            Log.i("DATEPICK1", datePicked);
            updateDate();
        }

    };

    private void updateDate() {
        ((EditText) findViewById(R.id.set_date_edit_text)).setText(
                new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(mCalendar.getTime()));
        ((TextView) findViewById(R.id.weekday_text_view)).setText(
                new SimpleDateFormat("EEEE", Locale.US).format(mCalendar.getTime()));

        //populates View
        getFoods();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }
}
