package com.example.final_android_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;

;

public class WeatherDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "WeatherDatabaseHelper";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "weather_table";

    private static String userid="";
    String[] columnsForCreate = {"longitude DOUBLE", "latitude DOUBLE","description TEXT"};
    String[] columns = {"longitude", "latitude", "description"};
    public WeatherDatabaseHelper(@Nullable Context context, String userid) {
        super(context, TABLE_NAME, null,1);
        this.userid=userid;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            Log.i("","Deleting DB");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("userid INTEGER,");
        for (int i = 0; i < columnsForCreate.length; i++) {
            sb.append(columnsForCreate[i]);
            if (i < columnsForCreate.length - 1) {
                sb.append(", ");
            }
        }

        sb.append(", FOREIGN KEY(userid) REFERENCES user_table(ID) ON DELETE CASCADE)");
        db.execSQL(sb.toString());
    }


    public boolean addData(String[] values) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("userid",userid);

        for (int i = 0; i < columns.length; i++) {
            contentValues.put(columns[i], values[i]);
        }

        Log.d(TAG, "addData: Adding " + Arrays.toString(values) + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getDataOfUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE userid = '"+ userid +"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param id
     * @return
     */
    public Cursor getItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE ID = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    /**
     * Updates the name field
     * @param newColumns
     * @param id
     */
    public void updateProfile(String[] newColumns, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            sb.append(columns[i]).append(" = '").append(newColumns[i]).append("'");
            if (i < columns.length - 1) {
                sb.append(", ");
            }
        }


        String query = "UPDATE " + TABLE_NAME + " SET " +sb +" WHERE ID = '" + id + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + Arrays.toString(newColumns));
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     */
    public void deleteWeather(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ID = '" + id + "'";
        Log.d(TAG, "deleteProfile: query: " + query);
        Log.d(TAG, "deleteProfile: Deleting " + id + " from database.");
        db.execSQL(query);
    }

}
