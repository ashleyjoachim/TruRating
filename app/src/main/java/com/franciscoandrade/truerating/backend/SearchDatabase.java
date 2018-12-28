package com.franciscoandrade.truerating.backend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.franciscoandrade.truerating.model.InspectionResultsModel;

import java.util.ArrayList;
import java.util.List;

public class SearchDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "search.db";
    private static final String TABLE_NAME = "search";
    private static final int SCHEMA_VERSION = 1;

    public SearchDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase search) {
        search.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSearchTerm(InspectionResultsModel name) {
        String nameAPI = name.getDba();
        Log.d("Whatevs", "addSearchTerm: " + nameAPI);
        if (nameAPI != null) {
            nameAPI = nameAPI.replaceAll("'", "");
        }

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE name = '" + nameAPI +
                        "';", null);
        if (cursor.getCount() == 0) {
            getWritableDatabase().execSQL("INSERT INTO " + TABLE_NAME +
                    "(name) VALUES('" +
                    nameAPI +
                    "');");
        }
        cursor.close();
    }

    public List<InspectionResultsModel> getNames() {
        List<InspectionResultsModel> nameList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    InspectionResultsModel name = new InspectionResultsModel(
                            cursor.getString(cursor.getColumnIndex("name")));
                    nameList.add(name);
                } while (cursor.moveToNext());
            }
        }
        return nameList;
    }
}
