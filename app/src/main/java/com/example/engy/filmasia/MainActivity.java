package com.example.engy.filmasia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engy.filmasia.connectToTheInternet.FilmQueryTask;
import com.example.engy.filmasia.connectToTheInternet.Search;
import com.example.engy.filmasia.contentProviders_fragments.ToWatchActivity;
import com.example.engy.filmasia.firabase.ChatActivity;
import com.example.engy.filmasia.preferences.SettingsActivity;
import com.example.engy.filmasia.preferences.SettingsUtils;
import com.example.engy.filmasia.sqlite.ViewHistoryActivity;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , SharedPreferences.OnSharedPreferenceChangeListener {  //1.to add trigger for pref change

    EditText query;
    TextView results;
    TableLayout table;
    String currentRes="";
    FilmQueryTask queryTask;

    private static final String LIFECYCLE_CALLBACK_TEXT_KEY = "callbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /////
        initialize();
        ///// Restore results
        if(savedInstanceState!=null){
            currentRes=savedInstanceState.getString(LIFECYCLE_CALLBACK_TEXT_KEY);
            Toast.makeText(this,currentRes,Toast.LENGTH_SHORT).show();
            FilmQueryTask.showResults(currentRes,table,results);
        }
        /////
        settingsSetup();
    }

    //initialize views
    private void initialize(){
        query=(EditText) findViewById(R.id.query);
        results=(TextView) findViewById(R.id.search_results);
        table=(TableLayout) findViewById(R.id.film_info);
    }

    //the initial settings for show year & text color
    private void settingsSetup(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        // show year checkbox
        SettingsUtils.setShowYear(sharedPreferences.getBoolean(getString(R.string.pref_show_year_key),getResources().getBoolean(R.bool.show_year_value)));
        // text color list pref
        SettingsUtils.setColor(sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_black_color_value)),this);
        // text size edit pref
        SettingsUtils.setSize(Float.parseFloat(
                sharedPreferences.getString(getString(R.string.pref_size_key),getString(R.string.pref_size_default_value))));

        //3.
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override        //2.
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_show_year_key))){
            Toast.makeText(this,"listener triggered",Toast.LENGTH_SHORT).show();
            SettingsUtils.setShowYear(sharedPreferences.getBoolean(key,getResources().getBoolean(R.bool.show_year_value)));
        }
        else if(key.equals(getString(R.string.pref_color_key))){
            Toast.makeText(this,"listener triggered",Toast.LENGTH_SHORT).show();
            SettingsUtils.setColor(sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_black_color_value)),this);
        }
        else if(key.equals(getString(R.string.pref_size_key))){
            Toast.makeText(this,"listener triggered",Toast.LENGTH_SHORT).show();
            SettingsUtils.setSize(Float.parseFloat(
                    sharedPreferences.getString(getString(R.string.pref_size_key),getString(R.string.pref_size_default_value))));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    ////
    void makeSearchQuery(){
        table.setVisibility(View.INVISIBLE);
        results.setText("searching ... ");
        String queryText=query.getText().toString();
        URL url= Search.buildUrl(queryText);
        Toast.makeText(this,url.toString(),Toast.LENGTH_SHORT).show();
        queryTask= new FilmQueryTask(table,results, this);
        queryTask.execute(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivityIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsActivityIntent);
        }
        else if (id == R.id.action_search){
            makeSearchQuery();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_to_watch) {
            Intent toWatchIntent=new Intent(MainActivity.this,ToWatchActivity.class);
            startActivity(toWatchIntent);
        } else if (id == R.id.nav_history) {
            Intent historyIntent=new Intent(MainActivity.this,ViewHistoryActivity.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_chat) {
            Intent chatIntent=new Intent(MainActivity.this,ChatActivity.class);
            startActivity(chatIntent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    ////////////
    ///To save the current result so that if we rotate th screen the results still exist
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(queryTask!=null)
            currentRes=queryTask.getStringResult();
        outState.putString(LIFECYCLE_CALLBACK_TEXT_KEY,currentRes);
        Toast.makeText(this,currentRes,Toast.LENGTH_SHORT).show();
    }
}
