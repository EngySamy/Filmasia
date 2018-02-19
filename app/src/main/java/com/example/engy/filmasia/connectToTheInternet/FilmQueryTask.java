package com.example.engy.filmasia.connectToTheInternet;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.engy.filmasia.preferences.SettingsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Engy on 2/17/2018.
 */

public class FilmQueryTask extends AsyncTask<URL,Void,String> {

    private TableLayout tableLayout;
    private TextView status;

    public FilmQueryTask(TableLayout table,TextView stat,boolean year){
        status=stat;
        tableLayout=table;
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
        if(s!=null&&!s.equals("")){
            try {
                JSONObject jsonObject=new JSONObject(s);
                String title=jsonObject.getString("Title");

                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setText(title);
                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setTextColor(SettingsUtils.getColor());
                ((TextView) ( (TableRow)tableLayout.getChildAt(0)).getChildAt(1) ).setTextSize(SettingsUtils.getSize());

                if(SettingsUtils.getShowYear()){
                    String year=jsonObject.getString("Year");
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


            } catch (JSONException e) {
                status.setText("Parsing Error");
                e.printStackTrace();
            }

        }
        else
            status.setText("Failed to find the film requested!");
    }
}
