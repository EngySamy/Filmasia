package com.example.engy.filmasia.contentProviders;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.engy.filmasia.R;
import com.example.engy.filmasia.sqlite.FilmasiaContract;

/**
 * Created by Engy on 2/22/2018.
 */

public class ToWatchAdapter extends RecyclerView.Adapter<ToWatchAdapter.FilmViewHolder>{

    private Cursor dataCursor;
    private Context mContext;

    ToWatchAdapter(Context context){
        mContext=context;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.to_watch_film_item,parent,false);
        FilmViewHolder vh= new FilmViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        int nameCol=dataCursor.getColumnIndex(FilmasiaContract.ToWatchEntry.COLUMN_NAME);
        holder.name.setText(dataCursor.getString(nameCol));

        int priorityCol=dataCursor.getColumnIndex(FilmasiaContract.ToWatchEntry.COLUMN_PRIORITY);
        String pri=""+dataCursor.getInt(priorityCol);
        holder.priority.setText(pri);

        int idCol=dataCursor.getColumnIndex(FilmasiaContract.ToWatchEntry._ID);
        holder.itemView.setTag(dataCursor.getInt(idCol));

        int priorityColor=getPriorityColor(dataCursor.getInt(priorityCol));
        GradientDrawable background= (GradientDrawable)holder.priority.getBackground();
        background.setColor(priorityColor);


    }

    int getPriorityColor(int p){
        int color=0;
        switch (p){
            case 1:
                color= ContextCompat.getColor(mContext,R.color.red);
                break;
            case 2:
                color= ContextCompat.getColor(mContext,R.color.yellow);
                break;
            case 3:
                color= ContextCompat.getColor(mContext,R.color.green);
                break;
        }
        return color;
    }

    @Override
    public int getItemCount() {
        if (dataCursor==null)
            return 0;
        return dataCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if(newCursor==dataCursor)
            return null;
        Cursor temp = dataCursor;
        this.dataCursor = newCursor; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class FilmViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView priority;

        public FilmViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.to_watch_film_name);
            priority=itemView.findViewById(R.id.to_watch_film_priority);
        }
    }
}
