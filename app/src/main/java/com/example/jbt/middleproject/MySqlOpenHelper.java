package com.example.jbt.middleproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Meital on 25/07/2016.
 */
public class MySqlOpenHelper extends SQLiteOpenHelper {


    public MySqlOpenHelper(Context context) {
        super(context, "moviesDb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String command="CREATE TABLE  "+ MovieDbContract.MovieTable.TableName+" ( "+ MovieDbContract.MovieTable.idColumn+"  INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+ MovieDbContract.MovieTable.movieName +" TEXT," +
                " "+ MovieDbContract.MovieTable.movieDecriptions +" TEXT," +
                " "+ MovieDbContract.MovieTable.pictureLink+" TEXT );";
        db.execSQL(command);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
