/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 30 March 2020
    Description: A simple calorie counting app using API data from the US Department of Agriculture
 */

package edu.apsu.csci.CalorieCounter.activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.classes.Food;
import edu.apsu.csci.CalorieCounter.classes.JSONResultData;
import edu.apsu.csci.CalorieCounter.db.DbDataSource;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivityClosingPrevious;

public class AddFoodActivity extends AppCompatActivity {
    private final Calendar mCalendar = Calendar.getInstance();
    private JSONResultData data = new JSONResultData();
    private QueryJSON query;
    private AutoCompleteTextView editText;

    //for database
    private DbDataSource dataSource;
    private String foodName;
    private int foodId;
    private String dateEntry;
    private double calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        dataSource = new DbDataSource(this);

        findViewById(R.id.to_menu_button).setOnClickListener(new GoToActivityClosingPrevious(this, MenuActivity.class));
        findViewById(R.id.set_date_edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddFoodActivity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to add the food to the database
                //  test function for now until all data is saved
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                String dateStr = sdf.format(c.getTime());

                // gets the food name
                editText = findViewById(R.id.search_foods_actv);
                foodName = editText.getText().toString();

                // get id
                doQuery(Integer.toString(foodId));

                // get calories
                //calories = 230;

                //dataSource.addFoodToDb(foodName,foodId,dateStr,calories);
                //Food food = dataSource.createFood(foodName,foodId,dateEntry,calories);

            }
        });

        editText = findViewById(R.id.search_foods_actv);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editText.isPerformingCompletion()) {
                    doQuery(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateEntry = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;

            updateDate();
        }

    };

    private void updateDate() {
        ((EditText) findViewById(R.id.set_date_edit_text)).setText(
                new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(mCalendar.getTime()));
        ((TextView) findViewById(R.id.weekday_text_view)).setText(
                new SimpleDateFormat("EEEE", Locale.US).format(mCalendar.getTime()));
    }

    private void doQuery(String search) {
        if (query == null) {
            try {
                if (Integer.parseInt(search) == foodId) {
                    query = new QueryJSON(getApplicationContext(), foodId);
                }
            }
            catch (NumberFormatException e) {
                query = new QueryJSON(getApplicationContext(), search);
            }

            query.execute();
            query = null;
        }
    }

    private class QueryJSON extends AsyncTask<Void,Void, JSONResultData> {
        private Uri.Builder builder;

        // Constructor for Food Search API
        public QueryJSON(Context c, String searchParam)
        {
            builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/search").buildUpon();
            builder.appendQueryParameter("api_key", c.getResources().getString(R.string.api_key));
            builder.appendQueryParameter("generalSearchInput", searchParam);
        }

        // Constructor  for Food Details API
        public QueryJSON(Context c, int foodId) {
            builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/").buildUpon();
            builder.appendPath(Integer.toString(foodId));
            builder.appendQueryParameter("api_key", c.getResources().getString(R.string.api_key));
        }

        // Searches through the JSON from the API call and returns necessary data
        @Override
        protected JSONResultData doInBackground(Void... voids) {
            JSONResultData resultData = new JSONResultData();

            try {
                // Establish the connection
                URL url = new URL(builder.toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                StringBuilder jsonData = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonData.append(line);
                }

                JSONObject reader = new JSONObject(jsonData.toString());

                // If the URL contains a food ID, browse the Food Details JSON
                if (url.toString().contains(Integer.toString(foodId))) {
                    JSONArray items = reader.getJSONArray("foodNutrients");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject nutrient = item.getJSONObject("nutrient");

                        if (nutrient.getString("unitName").equals("kcal")) {
                            String calories = item.getString("amount");
                            resultData.caloriesPer100g = Double.parseDouble(calories);
                        }
                    }

                    // If the URL does not have a food ID, browse the Food Search JSON
                } else {
                    JSONArray items = reader.getJSONArray("foods");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String title = item.getString("description");
                        resultData.foodTitles.add(title);

                        String company = item.getString("brandOwner");
                        resultData.companyNames.add(company);

                        String foodId = item.getString("fdcId");
                        resultData.foodIDs.add(Integer.parseInt(foodId));
                    }
                }

                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resultData;
        }

        @Override
        protected void onPostExecute(final JSONResultData resultData) {
            super.onPostExecute(resultData);
            Log.i("Calories:", Double.toString(resultData.caloriesPer100g));

            if (!resultData.foodTitles.isEmpty()) {
                Log.i("Title:", resultData.foodTitles.toString());

                editText.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, resultData.foodTitles));
                editText.showDropDown();

                editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        foodId = resultData.foodIDs.get(i);
                        Log.i("FoodIDArray", resultData.foodIDs.toString());
                    }
                });

            }
        }
    }
}
