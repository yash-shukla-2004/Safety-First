package com.example.finalproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final  String TAG = "GeofenceBroadcastReceiver";


    public void onReceive(Context context, Intent geofencingIntent) {
        //Old Code


        /*
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "Received Intent: " + intent.getAction());
        //Toast.makeText(context, "Geofence Triggered", Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "Intent action: " + intent.getAction());
// Log other relevant data from the intent if needed
        context = context.getApplicationContext();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        GeofencingEvent geofencingEvent  = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            Log.w(TAG, "onReceive:geofencingEvent: "+geofencingEvent);
        }

        //if (geofencingEvent != null) {
            // Handle the geofencing event here
        //    Log.d("GeofenceReceiver", "Geofence transition: " + geofencingEvent.getGeofenceTransition());
        //} else {
        //    Log.w("GeofenceReceiver", "GeofencingEvent is null");
        //}
        //geofencingEvent.getErrorCode();
        //List<Geofence> geofenceList=geofencingEvent.getTriggeringGeofences();
        //if (geofenceList != null) {
            //for(Geofence geofence: geofenceList){
                //Log.d(TAG,"onReceive: "+geofence.getRequestId());
            //}
        //}


        //Location location = (Location)geofencingEvent.getTriggeringLocation();


        int transitionType = (int)geofencingEvent.getGeofenceTransition();



        switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Toast.makeText(context, "GEOFENCE_ENTERED!", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_ENTERED!" , "" , context , MapsActivity.class);
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    Toast.makeText(context, "GEOFENCE_DWELLED!", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_DWELLED!" , "" ,context, MapsActivity.class);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Toast.makeText(context, "GEOFENCE_EXITED!", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("GEOFENCE_EXITED!" , "" , context , MapsActivity.class);
                    break;
                default:
                    Log.e(TAG, "onReceive: Error in switch " );


        }
               /* GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
                if (geofencingEvent.hasError()) {
                    String errorMessage = GeofenceStatusCodes
                            .getStatusCodeString(geofencingEvent.getErrorCode());
                    Log.e(TAG, errorMessage);
                    return;
                }

                // Get the transition type.
                int geofenceTransition = geofencingEvent.getGeofenceTransition();

                // Test that the reported transition was of interest.
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

                    // Get the geofences that were triggered. A single event can trigger
                    // multiple geofences.
                    List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

                    // Get the transition details as a String.
                    String geofenceTransitionDetails = getGeofenceTransitionDetails(
                            this,
                            geofenceTransition,
                            triggeringGeofences
                    );

                    // Send notification and log the transition details.
                    Toast.makeText(context, geofenceTransitionDetails, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, geofenceTransitionDetails);
                } else {
                    // Log the error.
                    Log.e(TAG, "Not working");
                }
            }

    private String getGeofenceTransitionDetails(GeofenceBroadcastReceiver geofenceBroadcastReceiver, int geofenceTransition, List<Geofence> triggeringGeofences) {
        switch (geofenceTransition) {

            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entering";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "Dwelling";

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exiting";

            default:
                throw new IllegalStateException("Unexpected value: " + geofenceTransition);
        }*/



//new
        // ... process geofence transition
       // NotificationHelper notificationHelper = new NotificationHelper(context);
        GeofencingEvent geofencingEvent  = GeofencingEvent.fromIntent(geofencingIntent);
        String title=null, message=null;
        int transitionType = (int)geofencingEvent.getGeofenceTransition();
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                title = "Entered Geofence!";
                message = "You have entered the designated area.";
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                title = "Dwelled Geofence!";
                message = "You have dwelled the designated area.";
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                title = "Exited Geofence!";
                message = "You have exited the designated area.";
                break;
        }
       // notificationHelper.sendHighPriorityNotification(title, message, context, MapsActivity.class); // Replace with your target activity

    }
}