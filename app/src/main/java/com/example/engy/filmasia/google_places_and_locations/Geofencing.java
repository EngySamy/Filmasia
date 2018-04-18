package com.example.engy.filmasia.google_places_and_locations;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Engy on 4/6/2018.
 */

public class Geofencing implements ResultCallback<Status> {

    private List<Geofence> geofences;
    private GoogleApiClient client;
    private Context context;
    private PendingIntent geofencePendingIntent;
    private long GEOFENCE_TIME = 86400000;  //24 hours
    private float GEOFENCE_R = 50;
    private String TAG=Geofencing.class.getSimpleName();


    public Geofencing(Context context, GoogleApiClient client) {
        this.client = client;
        this.context = context;
        geofences = new ArrayList<>();
        geofencePendingIntent = null;
    }

    // to create the geofences list
    public void updateGeofencesList(PlaceBuffer places) {
        if (places == null || places.getCount() == 0) return;
        for (Place place : places) {
            //get the place info
            String id = place.getId();
            double lat = place.getLatLng().latitude;
            double lng = place.getLatLng().longitude;
            //create geofence
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(id)
                    .setExpirationDuration(GEOFENCE_TIME)
                    .setCircularRegion(lat, lng, GEOFENCE_R)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            //add it to the list
            geofences.add(geofence);
        }
    }

    public void registerAllGeofences() {
        //check we have conncted client and list of geofences to reg
        if (client == null || !client.isConnected() || geofences == null || geofences.size() == 0)
            return;
        //check for user permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.GeofencingApi.addGeofences(client, getGeofenceRequest(), getGeofencePendingRequest())
                .setResultCallback(this);
    }

    public void unregisterAllGeofences() {
        //check we have conncted client and list of geofences to reg
        if (client == null || !client.isConnected() || geofences == null )
            return;
        //check for user permission
        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        LocationServices.GeofencingApi.removeGeofences(client,getGeofencePendingRequest())
            .setResultCallback(this);
    }

    private GeofencingRequest getGeofenceRequest(){
        GeofencingRequest.Builder builder=new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER); //immediately when you enter the region
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingRequest(){
        if(geofencePendingIntent!=null) return geofencePendingIntent;
        Intent i=new Intent(context,GeofenceBroadcastReceiver.class);
        geofencePendingIntent=PendingIntent.getBroadcast(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.e(TAG,"Error while adding/removing geofence "+status.getStatus().toString());
    }
}
