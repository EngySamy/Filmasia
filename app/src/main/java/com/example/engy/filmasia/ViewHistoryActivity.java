package com.example.engy.filmasia;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.engy.filmasia.sqlite.FilmasiaContract;
import com.example.engy.filmasia.sqlite.FilmasiaDBHelper;

/**
 * Created by Engy on 2/20/2018.
 */

public class ViewHistoryActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_history);

        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
        }

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view_films);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // use our helper to get ref to actual db
        FilmasiaDBHelper dbHelper=new FilmasiaDBHelper(this);
        db=dbHelper.getReadableDatabase();

        //get the films and pass it to the adapter
        Cursor films=getAllFilms();
        final FilmsAdapter adapter=new FilmsAdapter(films);

        //set this adapter to our recycler view
        recyclerView.setAdapter(adapter);

        ///add itemTouchHelper listener to help in removing one item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id=(long)viewHolder.itemView.getTag();
                removeFilm(id);
                adapter.swapCursor(getAllFilms());
            }
        }).attachToRecyclerView(recyclerView);
    }

    public Cursor getAllFilms(){
        return db.query(
                FilmasiaContract.FilmEntry.TABLE_NAME,null,null,null,null,null, FilmasiaContract.FilmEntry._ID);
    }

    public boolean removeFilm(long id){
        return db.delete(
                FilmasiaContract.FilmEntry.TABLE_NAME, FilmasiaContract.FilmEntry._ID+"="+id,null)>0;
    }
}
