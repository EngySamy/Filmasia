package com.example.engy.filmasia.sqlite;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 2/20/2018.
 */

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    private Cursor filmsCursor;
    public FilmsAdapter(Cursor cursor){
        filmsCursor=cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_film_item,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // move the cursor to the current item
        if(!filmsCursor.moveToPosition(position))
            return;
        //get the info from the current row
        String name=filmsCursor.getString(filmsCursor.getColumnIndex(FilmasiaContract.FilmEntry.COLUMN_NAME));
        int year=filmsCursor.getInt(filmsCursor.getColumnIndex(FilmasiaContract.FilmEntry.COLUMN_YEAR));
        String type=filmsCursor.getString(filmsCursor.getColumnIndex(FilmasiaContract.FilmEntry.COLUMN_TYPE));
        float rating=filmsCursor.getFloat(filmsCursor.getColumnIndex(FilmasiaContract.FilmEntry.COLUMN_RATING));
        long id=filmsCursor.getLong(filmsCursor.getColumnIndex(FilmasiaContract.FilmEntry._ID));

        ((TextView) ( (TableRow)holder.tableLayout.getChildAt(0)).getChildAt(1) ).setText(name);
        ((TextView) ( (TableRow)holder.tableLayout.getChildAt(1)).getChildAt(1) ).setText(String.valueOf(year));
        ((TextView) ( (TableRow)holder.tableLayout.getChildAt(2)).getChildAt(1) ).setText(type);
        ((TextView) ( (TableRow)holder.tableLayout.getChildAt(3)).getChildAt(1) ).setText(String.valueOf(rating));
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return filmsCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (filmsCursor != null) filmsCursor.close();
        filmsCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TableLayout tableLayout;
        ViewHolder(View v){
            super(v);
            tableLayout=v.findViewById(R.id.film_item_table);
        }

    }


}
