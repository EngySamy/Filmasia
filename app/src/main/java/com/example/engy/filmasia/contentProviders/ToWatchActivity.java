package com.example.engy.filmasia.contentProviders;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.engy.filmasia.R;
import com.example.engy.filmasia.sqlite.FilmasiaContract;

/**
 * Created by Engy on 2/22/2018.
 */

public class ToWatchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    RecyclerView recyclerView;
    ToWatchAdapter adapter;

    private static final int TASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_watch_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent add=new Intent(ToWatchActivity.this,AddToWatchActivity.class);
                startActivity(add);
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view__to_watch_films);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter=new ToWatchAdapter(this);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //delete the swiped item
                int id=(int)viewHolder.itemView.getTag();
                String stringId=""+id;
                Uri uri=FilmasiaContract.ToWatchEntry.CONTENT_URI; //add the id to the content uri
                uri=uri.buildUpon().appendPath(stringId).build();
                ContentResolver contentResolver=getContentResolver();
                contentResolver.delete(uri,null,null);
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,ToWatchActivity.this);

            }
        }).attachToRecyclerView(recyclerView);


        ///Ensure a loader is initialized and active.
        /// If the loader doesn't already exist, one is created, otherwise the last created loader is re-used.
        getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mData = null;

            @Override
            protected void onStartLoading() {
                if(mData!=null){
                    deliverResult(mData);
                }
                else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FilmasiaContract.ToWatchEntry.CONTENT_URI,
                            null,null,null, FilmasiaContract.ToWatchEntry.COLUMN_PRIORITY);
                }
                catch (Exception e){
                    Log.e("ToWatchActivity class","Failed to load data");
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}
