package edu.apsu.csci.CalorieCounter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlLiteHelper extends SQLiteOpenHelper {
// data, id, food name, cals


    private static final String DB_NAME = "fooddetails.sqlite";
    private static final int DB_VERSION = 1;

    public static final String FOOD_DETAILS_TABLE = "Details";

    public enum DetailsColumns
    {
        //for now we have
        food_id,
        food_name,
        food_calories,
        data_created;

        public static String[] names()
        {
            DetailsColumns[] v = values();
            String[] names = new String[v.length];
            for(int i = 0; i < v.length;i++)
            {
                names[i] = v[i].toString();
            }
            return names;
        }


    }

    public MySqlLiteHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + FOOD_DETAILS_TABLE + " (" +
                DetailsColumns.food_id + " INTEGER NOT NULL UNIQUE , " +
                DetailsColumns.food_name + " TEXT ," +
                DetailsColumns.food_calories + " REAL, " +
                DetailsColumns.data_created + " TEXT, " +
                " PRIMARY KEY(\"food_id\") " +
                ");";

        db.execSQL(sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);


    }
}
