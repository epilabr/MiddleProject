package com.example.jbt.middleproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Add_Edit_Activity extends AppCompatActivity {
    String imdbid;
    boolean isEditMode=false;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__edit_);


        Button show= (Button) findViewById(R.id.button5);
        imageView=(ImageView) findViewById(R.id.imageView);
       show.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               EditText et= (EditText) findViewById(R.id.editText3);
               DownloadImagesTask downloadImage= new DownloadImagesTask();
               downloadImage.execute(et.getText().toString());
           }
       });

        final int movieID= getIntent().getIntExtra("id", -1);

        if(movieID != -1)
        {
            isEditMode=true;
            EditText movieNameET= (EditText) findViewById(R.id.editText);
            EditText movieDescriptionET= (EditText) findViewById(R.id.editText2);
            EditText moviePicET= (EditText)findViewById(R.id.editText3);
            DBCommands commands= new DBCommands(this);
            Cursor resultCursor= commands.getDataFromDBAsCursor(movieID);

            if(resultCursor.moveToNext())
            {
                String name = resultCursor.getString(resultCursor.getColumnIndex(MovieDbContract.MovieTable.movieName));
                String description =  resultCursor.getString(resultCursor.getColumnIndex(MovieDbContract.MovieTable.movieDecriptions));
                String link = resultCursor.getString(resultCursor.getColumnIndex(MovieDbContract.MovieTable.pictureLink));
                movieNameET.setText(name);
                movieDescriptionET.setText(description);
                moviePicET.setText(link);

            }

        }


        Button ok1 = (Button)findViewById(R.id.button3);
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText moviname = (EditText)findViewById(R.id.editText);
                EditText moviedescription = (EditText)findViewById(R.id.editText2);
                EditText picturelink = (EditText) findViewById(R.id.editText3);

                Movie movie = new Movie(moviname.getText().toString(),moviedescription.getText().toString(),picturelink.getText().toString());

                DBCommands commands = new DBCommands(Add_Edit_Activity.this);

                if(isEditMode)
                {
                    movie.sqlId=movieID;
                    commands.updatemovie(movie);
                    Toast.makeText(Add_Edit_Activity.this, "Edited the Movie!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    //not edit mode add new artist....
                    commands.addMovie(movie);

                    Toast.makeText(Add_Edit_Activity.this, "Added a new Movie!", Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        });

        Button cancell = (Button)findViewById(R.id.button4);
        cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                imdbid= null;
            } else {
                imdbid= extras.getString("imdbID");
            }
        } else {
            imdbid= (String) savedInstanceState.getSerializable("imdbID");
        }


        DownloadWebsite downloadWebsite= new DownloadWebsite();
        downloadWebsite.execute("http://www.omdbapi.com/?i="+imdbid+"&plot=short&r=json");


    }

    public  class DownloadWebsite extends AsyncTask<String, Integer, String > {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Add_Edit_Activity.this);
            pd.setMessage("loading");
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {


            int lineConut = 0;
            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                }
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                while ((line = input.readLine()) != null) {
                    response.append(line + "\n");
                    lineConut++;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String resutFromWebsite) {

            if (pd != null)
            {
                pd.dismiss();
            }

            final ArrayList<Movie> jasonMovies = new ArrayList<Movie>();
            try {

                JSONObject mainObject= new JSONObject(resutFromWebsite);

                String title= mainObject.getString("Title");
                String plot= mainObject.getString("Plot");
                String pictureLink = mainObject.getString("Poster");

                EditText movieTitle = (EditText)findViewById(R.id.editText);
                movieTitle.setText(title);
                EditText moviePlot = (EditText)findViewById(R.id.editText2);
                moviePlot.setText(plot);
                EditText moviePic = (EditText)findViewById(R.id.editText3);
                moviePic.setText(pictureLink);




                Movie tempMovie = new Movie(title , plot, pictureLink);
                jasonMovies.add(tempMovie);

            } catch (JSONException e) {
                e.printStackTrace();

            }




        }

    }

    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Add_Edit_Activity.this);
            pd.setMessage("loading");
            pd.show();
        }


        @Override
        protected Bitmap doInBackground(String... urls) {
            return download_Image(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            if (pd != null)
            {
                pd.dismiss();
            }
        }


        private Bitmap download_Image(String url) {
            //---------------------------------------------------
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
            }
            return bm;
            //---------------------------------------------------
        }


    }

}

