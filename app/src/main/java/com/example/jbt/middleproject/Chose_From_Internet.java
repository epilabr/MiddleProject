package com.example.jbt.middleproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Chose_From_Internet extends AppCompatActivity {


    String textToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose__from__internet);





        Button cancel = (Button)findViewById(R.id.button7);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button go = (Button)findViewById(R.id.button6);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSearch = ((EditText)findViewById(R.id.editText4)).getText().toString();
                DownloadWebsite downloadWebsite= new DownloadWebsite();
                downloadWebsite.execute("http://www.omdbapi.com/?s="+textToSearch.replace(" ","%20"));
            }
        });

    }


    public  class DownloadWebsite extends AsyncTask<String, Integer, String > {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Chose_From_Internet.this);
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int lineConut=0;
            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                }
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line="";
                while ((line=input.readLine())!=null){
                    response.append(line+"\n");
                    lineConut++;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(connection!=null){
                    connection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String resutFromWebsite) {
                 final ArrayList<Movie>jasonMovies = new ArrayList<Movie>();
            try {

                //the main JSON object - initialize with string
                JSONObject mainObject= new JSONObject(resutFromWebsite);

                //extract data with getString, getInt getJsonObject - for inner objects or JSONArray- for inner arrays

                JSONArray allMovies= mainObject.getJSONArray("Search");

                for(int i=0; i<allMovies.length(); i++)
                {
                    //inner objects inside the array
                    JSONObject innerObj= allMovies.getJSONObject(i);
                    String title= innerObj.getString("Title");
                    String imdbID= innerObj.getString("imdbID");

                    Movie tempMovie = new Movie(title , imdbID);
                    jasonMovies.add(tempMovie);



                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

            ListView movielist = (ListView) findViewById(R.id.listView2);

            ArrayAdapter<Movie> adapter= new ArrayAdapter<Movie>(Chose_From_Internet.this, R.layout.list_item, R.id.textView4, jasonMovies );

            movielist.setAdapter(adapter);
            movielist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String imdbid = jasonMovies.get(position).imdbID;


                    Intent intent = new Intent(Chose_From_Internet.this,Add_Edit_Activity.class);
                    intent.putExtra("imdbID",imdbid);
                    startActivity(intent);

                }
            });

            if (pd != null)
            {
                pd.dismiss();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView progress = (TextView)findViewById(R.id.textView3);
            progress.setText(""+values[0]);
        }

    }

}
