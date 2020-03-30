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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import edu.apsu.csci.CalorieCounter.R;
import edu.apsu.csci.CalorieCounter.classes.Food;
import edu.apsu.csci.CalorieCounter.classes.ResultData;
import edu.apsu.csci.CalorieCounter.db.DbDataSource;
import edu.apsu.csci.CalorieCounter.listeners.GoToActivity;

public class AddFoodActivity extends AppCompatActivity {
    private final Calendar mCalendar = Calendar.getInstance();
    private ResultData data = new ResultData();
    private QueryJSON query;
    private DbDataSource dataSource;
    private AutoCompleteTextView editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        dataSource = new DbDataSource(this);

        findViewById(R.id.to_menu_button).setOnClickListener(new GoToActivity(this, MenuActivity.class));
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
            public void onClick(View v){
                //we need to add the food to the database
                //test function for now until all data is saved
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                String dateStr = sdf.format(c.getTime());

                int foodId = 0024;
                double calories = 230;
                String foodName = "Steak";

                //dataSource.addFoodToDb(foodName,foodId,dateStr,calories);

                Food food = dataSource.createFood(foodName,foodId,dateStr,calories);

            }
        });

        /*
        ((SearchView) findViewById(R.id.food_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    doQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    //doQuery(newText);
                }
                return true;
            }
        });

         */

        editText = findViewById(R.id.search_foods_actv);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doQuery(charSequence.toString());
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
            query = new QueryJSON(getApplicationContext(), search);
            query.execute();
            query = null;
        }
    }

    private class QueryJSON extends AsyncTask<Void,Void,ResultData> {
        //private String api_url = "https://api.nal.usda.gov/fdc/v1/search?api_key=" + R.string.api_key + "&";
        private Uri.Builder builder;

        public QueryJSON(Context c, String searchParam)
        {
            builder = Uri.parse("https://api.nal.usda.gov/fdc/v1/search").buildUpon();
            builder.appendQueryParameter("api_key", c.getResources().getString(R.string.api_key));
            builder.appendQueryParameter("generalSearchInput", searchParam);
        }

        @Override
        protected ResultData doInBackground(Void... voids) {
            ResultData resultData = new ResultData();

            try {
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
        protected void onPostExecute(ResultData resultData) {
            super.onPostExecute(resultData);
            Log.i("Title:", resultData.foodTitles.toString());
            Log.i("FoodID:", resultData.foodIDs.toString());

            editText.setAdapter(
                    new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, resultData.foodTitles));
        }
    }
}
