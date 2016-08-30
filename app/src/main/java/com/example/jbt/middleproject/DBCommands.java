package com.example.jbt.middleproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Meital on 25/07/2016.
 */
public class DBCommands {

    Context context;
    MySqlOpenHelper helper;

    public DBCommands(Context c) {

        context = c;
        helper = new MySqlOpenHelper(context);
    }

    public  void addMovie(Movie movie)
    {
        ContentValues cv= new ContentValues();
        cv.put(MovieDbContract.MovieTable.movieName, movie.moviename);
        cv.put(MovieDbContract.MovieTable.movieDecriptions, movie.moviedescription);
        cv.put(MovieDbContract.MovieTable.pictureLink, movie.picturelink);

        helper.getWritableDatabase().insert(MovieDbContract.MovieTable.TableName, null,cv );
    }

    public void updatemovie(Movie movie)
    {
        ContentValues cv= new ContentValues();
        cv.put(MovieDbContract.MovieTable.movieName, movie.moviename);
        cv.put(MovieDbContract.MovieTable.movieDecriptions, movie.moviedescription);
        cv.put(MovieDbContract.MovieTable.pictureLink, movie.picturelink);

        helper.getWritableDatabase().update(MovieDbContract.MovieTable.TableName,cv, "_id=?",  new String []{""+movie.sqlId } );

    }

    public Cursor getDataFromDBAsCursor()
    {
        Cursor tempTableDataCursor=   helper.getReadableDatabase().rawQuery("SELECT * FROM "+ MovieDbContract.MovieTable.TableName, null);

        return  tempTableDataCursor;
    }

    public Cursor getDataFromDBAsCursor(int movieID)
    {
        Cursor tempTableDataCursor=   helper.getReadableDatabase().rawQuery("SELECT * FROM "+ MovieDbContract.MovieTable.TableName+" WHERE _id="+movieID , null);

        return  tempTableDataCursor;
    }

    public void deleteMoviesDb() {

        helper.getWritableDatabase().delete(MovieDbContract.MovieTable.TableName, null, null);
    }
}
