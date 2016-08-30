package com.example.jbt.middleproject;

/**
 * Created by Meital on 25/07/2016.
 */
public class Movie {

    String moviename;
    String moviedescription;
    String picturelink;
    String imdbID;
    int sqlId;

    public Movie(String movieName, String movieDescription, String pictureLink) {
        this.moviename = movieName;
        this.moviedescription = movieDescription;
        this.picturelink = pictureLink;

    }

    public Movie(String title, String imdbID) {
        this.moviename = title;
        this.imdbID = imdbID;
    }


    @Override
    public String toString() {
        return moviename.toString();
    }
}
