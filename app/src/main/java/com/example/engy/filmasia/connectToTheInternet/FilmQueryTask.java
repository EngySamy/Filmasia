package com.example.engy.filmasia.connectToTheInternet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.engy.filmasia.MainActivity;
import com.example.engy.filmasia.preferences.SettingsUtils;
import com.example.engy.filmasia.sqlite.FilmasiaContract;
import com.example.engy.filmasia.sqlite.FilmasiaDBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Engy on 2/17/2018.
 */

public class FilmQueryTask extends AsyncTask<URL,Void,String> {

    private TableLayout table;
    private TextView stat;
    private String json;
    static Context context;

    public FilmQueryTask(TableLayout table,TextView stat,Context context1){
        this.stat=stat;
        this.table=table;
        this.context=context1;
    }
    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl=urls[0];
        String results=null;

        try {
            results=Search.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected void onPostExecute(String s) {
        json=s;
        showResults(s,table,stat);
    }

    public String getStringResult(){
        return json;
    }

    public static void showResults(String s,TableLayout tableLayout,TextView status){
        if(s!=null&&!s.equals("")){
            try {
                JSONObject jsonObject=new JSONObject(s);
                String title=jsonObject.getString("Title");

                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setText(title);
                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setTextColor(SettingsUtils.getColor());
                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setTextSize(SettingsUtils.getSize());

                String year=jsonObject.getString("Year");
                if(SettingsUtils.getShowYear()){
                    (tableLayout.getChildAt(1)).setVisibility(View.VISIBLE);
                    ((TextView) ( (TableRow)tableLayout.getChildAt(1)).getChildAt(1) ).setText(year);
                    ((TextView) ( (TableRow)tableLayout.getChildAt(1)).getChildAt(1) ).setTextColor(SettingsUtils.getColor());
                    ((TextView) ( (TableRow)tableLayout.getChildAt(1)).getChildAt(1) ).setTextSize(SettingsUtils.getSize());
                }
                else{
                    (tableLayout.getChildAt(1)).setVisibility(View.INVISIBLE);
                }


                String genre=jsonObject.getString("Genre");
                ((TextView) ( (TableRow)tableLayout.getChildAt(2)).getChildAt(1) ).setText(genre);
                ((TextView) ( (TableRow)tableLayout.getChildAt(2)).getChildAt(1) ).setTextColor(SettingsUtils.getColor());
                ((TextView) ( (TableRow)tableLayout.getChildAt(2)).getChildAt(1) ).setTextSize(SettingsUtils.getSize());


                String rating=jsonObject.getString("imdbRating");
                ((TextView) ( (TableRow)tableLayout.getChildAt(3)).getChildAt(1) ).setText(rating);
                ((TextView) ( (TableRow)tableLayout.getChildAt(3)).getChildAt(1) ).setTextColor(SettingsUtils.getColor());
                ((TextView) ( (TableRow)tableLayout.getChildAt(3)).getChildAt(1) ).setTextSize(SettingsUtils.getSize());


                tableLayout.setVisibility(View.VISIBLE);
                status.setText("Done!");

                ///save this to sqlite db
                // use our helper to get ref to actual db
                SQLiteDatabase db;
                FilmasiaDBHelper dbHelper=new FilmasiaDBHelper(context);
                db=dbHelper.getWritableDatabase();
                //first check if this film exists befor in our history
                String[] col={FilmasiaContract.FilmEntry.COLUMN_NAME};
                Cursor check=db.query(
                        FilmasiaContract.FilmEntry.TABLE_NAME,
                        col, FilmasiaContract.FilmEntry.COLUMN_NAME+"=?",
                        new String[]{title},
                        null,null,null);
                if(check.getCount()>0)
                    return;

                ContentValues cv= new ContentValues();
                cv.put(FilmasiaContract.FilmEntry.COLUMN_NAME,title);
                cv.put(FilmasiaContract.FilmEntry.COLUMN_TYPE,genre);
                cv.put(FilmasiaContract.FilmEntry.COLUMN_YEAR,year);
                cv.put(FilmasiaContract.FilmEntry.COLUMN_RATING,rating);
                db.insert(FilmasiaContract.FilmEntry.TABLE_NAME,null,cv);



            } catch (JSONException e) {
                status.setText("Parsing Error");
                e.printStackTrace();
            }

        }
        else
            status.setText("Failed to find the film requested!");
    }
}
