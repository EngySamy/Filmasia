package com.example.engy.filmasia.contentProviders_fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 3/31/2018.
 */

public class NotesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_watch_notes_activity);

        //if(savedInstanceState!=null){
            String notes=getIntent().getExtras().getString("notes");

            NotesFragment notesFragment=new NotesFragment();
            Bundle b=new Bundle();
            b.putString("notes",notes);
            notesFragment.setArguments(b);

            FragmentManager fm=getSupportFragmentManager();
            fm.beginTransaction().add(R.id.notes_container,notesFragment).commit();

        //}

    }
}
