package com.example.engy.filmasia;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Engy on 2/17/2018.
 */

public class Search {

    private final static String WEBSITE_URL="http://www.omdbapi.com";// "https://api.github.com/search/repositories";    //"https://www.elcinema.com/en/search";
    private final static String PARAM_QUERY="t";

    static URL buildUrl(String query){
        Uri builtUri=Uri.parse(WEBSITE_URL).buildUpon()
                .appendQueryParameter("apikey","16879612")
                .appendQueryParameter(PARAM_QUERY,query)
                .build();

        URL url=null;
        try {
            url=new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

        try {
            InputStream in=httpURLConnection.getInputStream();
            Scanner scanner=new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput=scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else
                return null;
        }
        finally {
            httpURLConnection.disconnect();
        }
    }


}
