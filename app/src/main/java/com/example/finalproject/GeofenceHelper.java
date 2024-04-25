package com.example.finalproject;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {
    private static final String TAG = "GeofenceHelper";
    PendingIntent pendingIntent;
    public GeofenceHelper(Context base) {
        super(base);
    }
    private final String Geofence_ID = "SOME_GEOFENCE_ID";//Each Geofence needs its own id or the new ones will get overdriven by the old ones
    private final float GEOFENCE_RADIUS = 200;
    static final LatLng GEOFENCE_CENTER = new LatLng(18.4630539,73.8536921);

    public GeofencingRequest getGeofencingRequest(Geofence geofence){

        return new GeofencingRequest.Builder()
                .addGeofence(geofence)//Can be used to add a list of geofences instead of just one.Use it to add the set of Geofences in the Final Project
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)//triggers the geofence at the first moment
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latlng,float radius,int transitiontypes){
        return  new Geofence.Builder().setCircularRegion(latlng.latitude,latlng.longitude,radius).setRequestId(ID).setTransitionTypes(transitiontypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE).build();
    }

    //new
    public PendingIntent createPendingIntent(Context context, int requestCode, Intent intent) {
        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag = PendingIntent.FLAG_MUTABLE;
        } else {
            flag = PendingIntent.FLAG_UPDATE_CURRENT; // Or another suitable flag
        }

        return PendingIntent.getBroadcast(context, requestCode, intent, flag);
    }

    public PendingIntent getPendingIntent() {
        try {
            //if (pendingIntent != null) {
            //return pendingIntent;
            //}
            Intent intent = new Intent(GeofenceHelper.this, GeofenceBroadcastReceiver.class);
            intent.setAction("com.google.android.gms.location.GeofenceTriggered");
            //pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_MUTABLE );
            pendingIntent=createPendingIntent(this,2607,intent);

            Log.d(TAG, "Intent action before sending: " + intent.getAction());
            return pendingIntent;
        } catch (Exception e) {
            Log.e(TAG, "Error creating PendingIntent: " + e.getMessage());
            e.printStackTrace();
            return null; // or handle the error in a way appropriate for your application
        }
    }

    public String getErrorString(Exception e){
        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch(apiException.getStatusCode()){
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }
}