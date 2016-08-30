package com.example.jbt.middleproject;

import android.net.Uri;

/**
 * Created by Meital on 24/07/2016.
 */
public class MovieDbContract {


    public static class MovieTable
    {
        public static String idColumn = "_id";
        public  static String TableName= "moviesTable";
        public  static String Authority= "com.example.jbt.middleproject";
        public static Uri ContentURI=Uri.parse("content://"+Authority+"/"+TableName);
        public static String movieName="moviename";
        public static String movieDecriptions="moviedescription";
        public static String pictureLink="picturelink";
    }
}
