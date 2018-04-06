package com.example.engy.filmasia.contentProviders_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 3/30/2018.
 */

public class NotesFragment extends Fragment {

    public NotesFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.to_watch_notes_fragment,container,false);
        TextView textView=rootView.findViewById(R.id.to_watch_notes_text_view);

        String notes = getArguments().getString("notes");
        textView.setText(notes);


        return rootView;
    }
}
