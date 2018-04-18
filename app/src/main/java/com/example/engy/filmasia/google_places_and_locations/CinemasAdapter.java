package com.example.engy.filmasia.google_places_and_locations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.engy.filmasia.R;
import com.google.android.gms.location.places.PlaceBuffer;

/**
 * Created by Engy on 4/6/2018.
 */

public class CinemasAdapter extends RecyclerView.Adapter<CinemasAdapter.CinemaHolder> {
    private Context context;
    private PlaceBuffer places;

    public CinemasAdapter(Context context){
        this.context=context;
        places=null;
    }

    @Override
    public CinemaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cinema_item,parent,false);
        CinemaHolder ch= new CinemaHolder(v);
        return ch;
    }

    @Override
    public void onBindViewHolder(CinemaHolder holder, int position) {
        holder.name.setText(places.get(position).getName());
        holder.address.setText(places.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        if(places!=null) return places.getCount();
        else return 0;
    }

    public void swapCinemas(PlaceBuffer newPlaces ){
        places=newPlaces;
        if(newPlaces!=null) notifyDataSetChanged();
    }

    class CinemaHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;

        public CinemaHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.txt_view_cinema_name);
            address=itemView.findViewById(R.id.txt_view_cinema_address);
        }
    }
}
