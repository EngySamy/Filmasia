package com.example.engy.filmasia.contentProviders_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 2/22/2018.
 */

public class ToWatchActivity extends AppCompatActivity implements ToWatchMasterFragment.OnFilmClick {

    boolean twoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_watch_activity);


        //check if it's phone or tablet mode
        if(findViewById(R.id.notes_linear_layout)!=null){
            twoPane=true;

            NotesFragment notesFragment=new NotesFragment();
            Bundle b=new Bundle();
            b.putString("notes","Select a film to view its notes");
            notesFragment.setArguments(b);

            FragmentManager fm=getSupportFragmentManager();
            fm.beginTransaction().add(R.id.notes_container,notesFragment).commit();
        }
        else{
            twoPane=false;
        }
    }


    @Override
    public void onFilmClick(String notes) {
        //Toast.makeText(this,"position is "+position,Toast.LENGTH_SHORT).show();
        if(twoPane){

            NotesFragment notesFragment=new NotesFragment();
            Bundle b=new Bundle();
            b.putString("notes",notes);
            notesFragment.setArguments(b);

            FragmentManager fm=getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.notes_container,notesFragment).commit();
        }
        else{
            Bundle b=new Bundle();
            b.putString("notes",notes);
            Intent intent=new Intent(ToWatchActivity.this,NotesActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }

    }
}
