package com.example.engy.filmasia.contentProviders;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.engy.filmasia.R;
import com.example.engy.filmasia.sqlite.FilmasiaContract;

/**
 * Created by Engy on 2/22/2018.
 */

public class AddToWatchActivity extends AppCompatActivity  {

    private int mPriority;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_watch_add_activity);

        ((RadioButton) findViewById(R.id.first_button)).setChecked(true);
        mPriority = 1;
    }

    public void AddFilmToWatch(View view) {
        String name=((EditText)findViewById(R.id.to_watch_film_edit_text)).getText().toString();
        if(name.length()==0){
            return;
        }
        ContentValues cv=new ContentValues();
        cv.put(FilmasiaContract.ToWatchEntry.COLUMN_NAME,name);
        cv.put(FilmasiaContract.ToWatchEntry.COLUMN_PRIORITY,mPriority);

        Uri uri=getContentResolver().insert(FilmasiaContract.ToWatchEntry.CONTENT_URI,cv);

        if(uri!=null){
            Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.first_button)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.second_button)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.third_button)).isChecked()) {
            mPriority = 3;
        }
    }
}
