package com.example.jbt.middleproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.widget.PopupMenu.OnMenuItemClickListener {

    SimpleCursorAdapter adapter;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.plus_menu);
                popupMenu.show();
            }
        });

        refreshList();

    }

    public  void  refreshList()
    {

        ListView lv= (ListView)findViewById(R.id.listView);
        DBCommands commands= new DBCommands(this);
        final Cursor tempTableDataCursor =commands.getDataFromDBAsCursor();

        String[] from={MovieDbContract.MovieTable.movieName, MovieDbContract.MovieTable.movieDecriptions, MovieDbContract.MovieTable.pictureLink };
        int[] to= { R.id.textView4, R.id.textView5, R.id.imageView2 };

        adapter= new SimpleCursorAdapter(this, R.layout.list_item ,tempTableDataCursor, from, to );
        lv.setAdapter(adapter);

        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(tempTableDataCursor.moveToPosition(position))
                {
                    int dbID= tempTableDataCursor.getInt(tempTableDataCursor.getColumnIndex("_id"));

                    Intent intent= new Intent(MainActivity.this, Add_Edit_Activity.class);
                    intent.putExtra("id", dbID);
                    startActivity(intent);

                }

            }
        });



    }


    @Override
    protected void onResume() {


        refreshList();

        super.onResume();
    }



    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button3:
                Intent intent = new Intent(MainActivity.this, Add_Edit_Activity.class);
                startActivity(intent);
                return true;
            case R.id.button4:
                Intent intent1 = new Intent(MainActivity.this, Chose_From_Internet.class);
                startActivity(intent1);
                return true;
        }
        return  true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DBCommands commands = new DBCommands(this);
        if(item.getTitle().equals("EXIT"))
        {
            finish();
        }
        else if(item.getTitle().equals("Delete all items"))
        {
            commands.deleteMoviesDb();
            Toast.makeText(MainActivity.this, "All Movies Deleted!", Toast.LENGTH_SHORT).show();
            refreshList();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        MySqlOpenHelper helper= new MySqlOpenHelper(this);
        c= helper.getReadableDatabase().query(MovieDbContract.MovieTable.TableName, null,null,null,null,null,null);
        DBCommands commands= new DBCommands(this);
        final Cursor tempTableDataCursor =commands.getDataFromDBAsCursor();
        int position= info.position;
        if(c.moveToPosition(position))

        {
            int DBid= c.getInt(c.getColumnIndex("_id"));
            MySqlOpenHelper helper1 = new MySqlOpenHelper(this);
            if (item.getItemId() == R.id.delete) {
                helper1.getWritableDatabase().delete(MovieDbContract.MovieTable.TableName, "_id=?", new String[]{"" + DBid});
            }else {
                int dbID= c.getInt(c.getColumnIndex("_id"));
                Intent intent= new Intent(MainActivity.this, Add_Edit_Activity.class);
                intent.putExtra("id", dbID);
                startActivity(intent);
            }
            refreshList();
        }



        return true;
    }


}
