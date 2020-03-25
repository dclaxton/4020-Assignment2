package edu.apsu.csci.CalorieCounter.classes;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeSet;

import edu.apsu.csci.CalorieCounter.R;


public class QueryJSON extends AsyncTask<Void,Void,ResultData> {
    //private String api_url = "https://api.nal.usda.gov/fdc/v1/search?api_key=" + R.string.api_key + "&";
    Uri.Builder builder;

    public QueryJSON(Context c, String searchParam)
    {
        builder = Uri.parse("http://api.nal.usda.gov/fdc/v1/search").buildUpon();
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

            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder fdcIdBuilder = new StringBuilder();

            JSONObject reader = new JSONObject(jsonData.toString());
            JSONArray items = reader.getJSONArray("foods");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                String title = item.getString("description");
                titleBuilder.append(title);
                titleBuilder.append("\n----------\n");

                String foodId = item.getString("fdcId");
                fdcIdBuilder.append(foodId);
                fdcIdBuilder.append("\n----------\n");
            }

            resultData.foodTitle = titleBuilder.toString();
            resultData.foodID = fdcIdBuilder.toString();

            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Title:", resultData.foodTitle);
        Log.i("Title:", resultData.foodID);

        return resultData;
    }


}
