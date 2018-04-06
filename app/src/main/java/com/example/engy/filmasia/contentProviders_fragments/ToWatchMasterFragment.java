package com.example.engy.filmasia.contentProviders_fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.engy.filmasia.R;
import com.example.engy.filmasia.sqlite.FilmasiaContract;

/**
 * Created by Engy on 3/30/2018.
 */

public class ToWatchMasterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerView;
    ToWatchAdapter adapter;
    private static final int TASK_LOADER_ID = 0;
    private OnFilmClick mCallback;

    public interface OnFilmClick{
        void onFilmClick(String notes);
    }

    public ToWatchMasterFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.to_watch_app_bar,container,false);


        FloatingActionButton fab = rootView.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent add=new Intent(getActivity(),AddToWatchActivity.class);
                startActivity(add);
            }
        });

        recyclerView=rootView.findViewById(R.id.recycler_view__to_watch_films);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter=new ToWatchAdapter(getActivity(),mCallback);
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
                Uri uri= FilmasiaContract.ToWatchEntry.CONTENT_URI; //add the id to the content uri
                uri=uri.buildUpon().appendPath(stringId).build();
                ContentResolver contentResolver=getActivity().getContentResolver();
                contentResolver.delete(uri,null,null);
                getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,ToWatchMasterFragment.this);

            }
        }).attachToRecyclerView(recyclerView);


        ///Ensure a loader is initialized and active.
        /// If the loader doesn't already exist, one is created, otherwise the last created loader is re-used.
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback=(OnFilmClick) getContext();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnFilmClick");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {
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
                    return getActivity().getContentResolver().query(FilmasiaContract.ToWatchEntry.CONTENT_URI,
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

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
}
