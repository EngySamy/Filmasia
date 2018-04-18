package com.example.engy.filmasia.google_places_and_locations;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.engy.filmasia.R;
import com.example.engy.filmasia.sqlite.FilmasiaContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Engy on 4/6/2018.
 */

public class CinemasActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor>   {

    private String TAG="CINEMAS";
    private int PLACE_PICKER_REQ=100;
    private RecyclerView recyclerView;
    private CinemasAdapter adapter;
    private GoogleApiClient client;
    private boolean isEnabled;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private Geofencing geofencing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinemas_activity_layout);

        //define google client that will connect to Google Play Services and use that connection to communicate with the APIs
        client=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();
        geofencing=new Geofencing(this,client);

        Switch onOffSwitch=(Switch) findViewById(R.id.switch_enable_geo);
        //get the value of the shared pref
        isEnabled=getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.setting_enabled),false);
        onOffSwitch.setChecked(isEnabled);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                //update the value of the shared pref
                editor.putBoolean(getString(R.string.setting_enabled), isChecked);
                isEnabled = isChecked;
                editor.apply();
                if (isChecked) geofencing.registerAllGeofences();
                else geofencing.unregisterAllGeofences();
            }
        });

        //handling how to add new cinema place to your list by clicking add fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_cinema);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(CinemasActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(CinemasActivity.this,"You need to give the app the permission",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    PlacePicker.IntentBuilder builder =new PlacePicker.IntentBuilder();
                    Intent i= builder.build(CinemasActivity.this);
                    startActivityForResult(i,PLACE_PICKER_REQ);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG,"Google play services are unavailable!"+ e.getMessage());
                }

            }
        });

        ///
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view_cinemas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CinemasAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshPlacesData();
    }

    void refreshPlacesData(){
        Uri uri=FilmasiaContract.PlaceEntry.CONTENT_URI;
        //get all ids from the db
        Cursor data=getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        if(data==null|| data.getCount()==0) return;
        List<String> ids=new ArrayList<>();
        //put these ids in a list
        int i=0;
        while (data.moveToNext()){
            ids.add(data.getString(data.getColumnIndex(FilmasiaContract.PlaceEntry.COLUMN_PLACE_ID)));
            i++;
        }
        //get their data from google server and pass it to the adapter
        PendingResult<PlaceBuffer> placeResult=Places.GeoDataApi.getPlaceById(client,
                ids.toArray(new String[ids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                adapter.swapCinemas(places);
                //to update geofences
                geofencing.updateGeofencesList(places);
                if (isEnabled) geofencing.registerAllGeofences();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER_REQ&&resultCode==RESULT_OK){
            Place place=PlacePicker.getPlace(this,data);
            if(place==null){
                Toast.makeText(this,"No place selected",Toast.LENGTH_SHORT).show();
                return;
            }

            String placeId=place.getId();
            Log.e(TAG,"before insert");

            ContentValues cv=new ContentValues();
            cv.put(FilmasiaContract.PlaceEntry.COLUMN_PLACE_ID,placeId);
            Uri uri=getContentResolver().insert(FilmasiaContract.PlaceEntry.CONTENT_URI,cv);

            Log.e(TAG,"After insert"+uri.toString());
            refreshPlacesData();
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG,"API Client Connection Successful!");
        refreshPlacesData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"API Client Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"API Client Connection Failed!");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize location permissions checkbox
        CheckBox locationPermissions = (CheckBox) findViewById(R.id.checkbox_loc_perm);
        if (ActivityCompat.checkSelfPermission(CinemasActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }
    }

    public void onLocationPermissionClicked(View view) {
        ActivityCompat.requestPermissions(CinemasActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }
}
