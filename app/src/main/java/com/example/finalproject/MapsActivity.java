package com.example.finalproject;

//OLD CODE
/*import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap myMap;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 1001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 1002;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private final String Geofence_ID = "SOME_GEOFENCE_ID";//Each Geofence needs its own id or the new ones will get overdriven by the old ones
    private final float GEOFENCE_RADIUS = 400;
    static final int LOITERING_DELAY = 3000; // 5 minutes in milliseconds, Increase this later to 5 minutes for user but for testing use only 3 seconds
    static final LatLng GEOFENCE_CENTER1 = new LatLng(18.4630539, 73.8536921);// Replace with your desired location
    static final LatLng GEOFENCE_CENTER2 = new LatLng(18.4615926, 73.8483617);
    static final int GEOFENCE_TRANSITIONS = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final String TAG = "MapsActivity";
    DataLocations d = new DataLocations();
    String Loc[] = {"Dhankawadi","Deccan Gymkhana","Aundh","Baner","Viman Nagar","Kothrud","Wakad","Hadapsar","Magarpatta City","Koregaon Park","Shivaji Nagar"};

    private int currentindex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Fetch latitude and longitude data from Firestore
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.w(TAG, "onComplete: "+task.isSuccessful() );
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming your Firestore document structure has fields 'latitude' and 'longitude'
                                Double latitude = document.getDouble("latitude");
                                Double longitude = document.getDouble("longitude");
                                Log.w(TAG, "onComplete: "+latitude);
                                if (latitude != null && longitude != null) {

                                    locationData[currentindex][0] = latitude;
                                    locationData[currentindex][1] = longitude;
                                    currentindex++;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });//
    }

    private void zoomToUserLocation(LatLng userLocation) {

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16)); // Zoom level 16 (adjustable)
    }

    private FusedLocationProviderClient fusedLocationClient;

    private void getDeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Got last known location, zoom to it
                        Location location = task.getResult();
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        zoomToUserLocation(userLocation);
                    } else {
                        Log.d(TAG, "Current location is null. Using default.");
                        // Handle location unavailable scenario (optional)
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "Error getting location: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        getDeviceLocation();

        /*LatLng ghatiya = new LatLng(18.461620, 73.850533);
        myMap.addMarker(new MarkerOptions().position(ghatiya).title("Dhankawadi"));//
        //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghatiya, 16));
        enableUserLocation();
        //myMap.setOnMapLongClickListener(this);
        addGeofencestatically();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myMap.setMyLocationEnabled(true);
            }
        }

        /*if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can add geofences", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"BG Location is necessary" , Toast.LENGTH_SHORT).show();
            }
        }//
    }

    //@Override
    /*public void onMapLongClick(@NonNull LatLng latLng) {

        if(Build.VERSION.SDK_INT>=29){
            //We need background permission
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED){
                handleMapLongClick(latLng);
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
                else{
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }else{
            handleMapLongClick(latLng);
        }
        handleMapLongClick(latLng);
    }

    private void handleMapLongClick(LatLng latLng){
        myMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }
    */

   /* Geofence geofence = new Geofence.Builder()
            .setRequestId(Geofence_ID)
            .setCircularRegion(GEOFENCE_CENTER.latitude, GEOFENCE_CENTER.longitude, GEOFENCE_RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(GEOFENCE_TRANSITIONS)
            .setLoiteringDelay(LOITERING_DELAY)  // Add loitering delay
            .build();
    //

    private void addGeofence() {
        List<Geofence> geofenceList = new ArrayList<>();
        float radius = GEOFENCE_RADIUS; // Set your desired radius in meters
        int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT; // Entering or exiting triggers

// Assuming GEOFENCE_CENTER1 and GEOFENCE_CENTER2 are objects with latitude and longitude properties
        /*double[][] locationData = {
                {GEOFENCE_CENTER1.latitude, GEOFENCE_CENTER1.longitude},
                {GEOFENCE_CENTER2.latitude, GEOFENCE_CENTER2.longitude}
        };//

        for (int i = 0; i < d.locationData.length; i++) {
            double latitude = d.locationData[i][0];
            double longitude = d.locationData[i][1];

            // Add other parameters if needed (e.g., unique request ID)
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("important_location_" + i) // Optional: Unique ID
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(transitionType)
                    .build();
            geofenceList.add(geofence);
            LatLng latLng = new LatLng(latitude ,longitude);
            addCircle(latLng, GEOFENCE_RADIUS);
            addMarker(latLng,Loc[currentindex]);
            currentindex++;
        }

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER) // Optional: define initial trigger
                .addGeofences(geofenceList) // Add all geofences at once
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getGeofencingClient(this)
                .addGeofences(geofencingRequest, geofenceHelper.getPendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofences Added....");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
        //List<Geofence> geofenceList = Collections.singletonList(geofence);

            /*GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .addGeofences(geofenceList)
                    .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER) // Optional: define initial trigger
                    .build();

             */

        /*GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            enableUserLocation();

        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofence Added....");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                enableUserLocation();

            }
            PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        /*GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingClient.addGeofences(new GeofencingRequest.Builder()
                        .addGeofences(Collections.singletonList(geofence))
                        .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER ) // Optional: define initial trigger
                        .build(), pendingIntent)


            LocationServices.getGeofencingClient(this)
                    .addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Geofence Added....");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errorMessage = geofenceHelper.getErrorString(e);
                            Log.d(TAG, "onFailure: " + errorMessage);
                        }
                    });
                    //

        }

    public void addMarker(LatLng latLng,String text){

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(text);
        myMap.addMarker(markerOptions);

    }
    public void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(75,128,0,128));
        circleOptions.strokeWidth(5);
        myMap.addCircle(circleOptions);
    }
    private void addGeofencestatically(){
        addGeofence();

    }

}*/

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap myMap;
    DataLocations dataLocations = new DataLocations();
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 1001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 1002;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private final String Geofence_ID = "SOME_GEOFENCE_ID";//Each Geofence needs its own id or the new ones will get overdriven by the old ones
    private final float GEOFENCE_RADIUS = 800;
    static final int LOITERING_DELAY = 3000; // 5 minutes in milliseconds, Increase this later to 5 minutes for user but for testing use only 3 seconds
    static final LatLng GEOFENCE_CENTER1 = new LatLng(18.4630539, 73.8536921);// Replace with your desired location
    static final LatLng GEOFENCE_CENTER2 = new LatLng(18.4615926, 73.8483617);
    static final int GEOFENCE_TRANSITIONS = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final String TAG = "MapsActivity";
    DataLocations d = new DataLocations();
    //LocationFecther f=new LocationFecther();
    //String Loc[] = {"Dhankawadi","Deccan Gymkhana","Aundh","Baner","Viman Nagar","Kothrud","Wakad","Hadapsar","Magarpatta City","Koregaon Park","Shivaji Nagar"};

    private int currentindex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Fetch latitude and longitude data from Firestore
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.w(TAG, "onComplete: "+task.isSuccessful() );
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming your Firestore document structure has fields 'latitude' and 'longitude'
                                Double latitude = document.getDouble("latitude");
                                Double longitude = document.getDouble("longitude");
                                Log.w(TAG, "onComplete: "+latitude);
                                if (latitude != null && longitude != null) {

                                    locationData[currentindex][0] = latitude;
                                    locationData[currentindex][1] = longitude;
                                    currentindex++;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
    }

    private void zoomToUserLocation(LatLng userLocation) {

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16)); // Zoom level 16 (adjustable)
    }

    private FusedLocationProviderClient fusedLocationClient;

    private void getDeviceLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Got last known location, zoom to it
                        Location location = task.getResult();
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        zoomToUserLocation(userLocation);
                    } else {
                        Log.d(TAG, "Current location is null. Using default.");
                        // Handle location unavailable scenario (optional)
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "Error getting location: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        getDeviceLocation();

        LatLng ghatiya = new LatLng(18.461620, 73.850533);
        myMap.addMarker(new MarkerOptions().position(ghatiya).title("Dhankawadi"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghatiya, 16));
        enableUserLocation();
        //myMap.setOnMapLongClickListener(this);
        addGeofencestatically();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myMap.setMyLocationEnabled(true);
            }
        }

        /*if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can add geofences", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"BG Location is necessary" , Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    //@Override
    /*public void onMapLongClick(@NonNull LatLng latLng) {

        if(Build.VERSION.SDK_INT>=29){
            //We need background permission
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED){
                handleMapLongClick(latLng);
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
                else{
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }else{
            handleMapLongClick(latLng);
        }
        handleMapLongClick(latLng);
    }

    private void handleMapLongClick(LatLng latLng){
        myMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }
    */

   /* Geofence geofence = new Geofence.Builder()
            .setRequestId(Geofence_ID)
            .setCircularRegion(GEOFENCE_CENTER.latitude, GEOFENCE_CENTER.longitude, GEOFENCE_RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(GEOFENCE_TRANSITIONS)
            .setLoiteringDelay(LOITERING_DELAY)  // Add loitering delay
            .build();
    */

    private void addGeofence() {

        //DataLocations dataLocations = new DataLocations();
        dataLocations.fetchLocationData(new DataLocations.DataFetchListener() {

            @Override
            public void onDataFetched(double[][] locations) {
                // Implement this method if needed
                // For example, you could throw an UnsupportedOperationException
                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public void onDataFetched(double[][] locations, String[] names, String[] risks) {
                List<Geofence> geofenceList = new ArrayList<>();
                float radius = GEOFENCE_RADIUS; // Set your desired radius in meters
                int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT; // Entering or exiting triggers

                for (int i = 0; i < locations.length; i++) {
                    double latitude = locations[i][0];
                    double longitude = locations[i][1];
                    String name = names[i];
                    String ris = risks[i];

                    // Add other parameters if needed (e.g., unique request ID)
                    Geofence geofence = new Geofence.Builder()
                            .setRequestId("important_location_" + i) // Optional: Unique ID
                            .setCircularRegion(latitude, longitude, radius)
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(transitionType)
                            .build();
                    geofenceList.add(geofence);
                    LatLng latLng = new LatLng(latitude, longitude);
                    addCircle(latLng, GEOFENCE_RADIUS, ris);
                    addMarker(latLng, name);
                    currentindex++;
                }
                GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                        .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER) // Optional: define initial trigger
                        .addGeofences(geofenceList) // Add all geofences at once
                        .build();
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                GeofenceHelper geofenceHelper1 = new GeofenceHelper(MapsActivity.this);
                LocationServices.getGeofencingClient(MapsActivity.this)
                        .addGeofences(geofencingRequest, geofenceHelper1.getPendingIntent())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: Geofence Added....");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String errorMessage = geofenceHelper.getErrorString(e);
                                Log.d(TAG, "onFailure: " + errorMessage);
                            }
                        });


            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

       /* LocationServices.getGeofencingClient(this)
                .addGeofences(geofencingRequest, geofenceHelper.getPendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofences Added....");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });*/


/*
    private void addMarker(LatLng latLng, String placeName) {
        String actualPlaceName = dataLocations.fetchLocationData(new DataLocations.DataFetchListener() {
            for (LocationFetcher.fetchLocations : locationData) {
                // ... (access latitude, longitude)
                String name = data.name; // Access the name property of each LocationData object

                // Use the retrieved name for your needs (e.g., display in a list)
                Log.d("Location Name", name);
            }
        }); // Fetch place name from Firebase

        Marker marker = myMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(actualPlaceName != null ? actualPlaceName : "Unknown Place") // Set title (fallback for missing name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) // Set marker icon color (optional)
                .snippet("Click for details")); // Optionally add a snippet for more information

        // Set custom marker info window (optional)
        marker.setTag(actualPlaceName); // Store place name for info window
        marker.setInfoWindowAnchor(0.5f, 0.5f); // Center info window on marker
        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeName = (String) marker.getTag();
                if (placeName != null) {
                    // Handle info window click (e.g., show details about the place)
                    Toast.makeText(getApplicationContext(), "Clicked on: " + placeName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
/*
public void addMarkers(List<DataLocations.LocationData> locationData) {
    for (DataLocations.LocationData data : locationData) {
        LatLng latLng = new LatLng(data.latitude, data.longitude);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(data.name) // Set marker title as the retrieved name
                .snippet("Click for details (optional)") // Optional snippet for more info
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) // Set marker icon color (optional)
        );

        // Set custom marker info window (optional)
        marker.setTag(data); // Store location data for info window
        marker.setInfoWindowAnchor(0.5f, 0.5f); // Center info window on marker
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DataLocations.LocationData data = (DataLocations.LocationData) marker.getTag();
                if (data != null) {
                    // Handle info window click (e.g., show details about the location)
                    Toast.makeText(mMap.getContext(), "Clicked on: " + data.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}*/

    public void addMarker(LatLng latLng,String text){

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(text);
        myMap.addMarker(markerOptions);

    }
    public void addCircle(LatLng latLng, float radius,String risk){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        if(risk.equals("high")) {
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(75, 128, 0, 128));
        } else if (risk.equals("medium")) {
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0)); // Red outline (you can change this if desired)
            circleOptions.fillColor(Color.argb(75, 255, 255, 0));   // Yellow fill (partially transparent)
        }else if(risk.equals("low"))
        {
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0)); // Red outline (opaque)
            circleOptions.fillColor(Color.argb(75, 0, 255, 0));   // Green fill (partially transparent)
        }
        circleOptions.strokeWidth(5);
        myMap.addCircle(circleOptions);
    }
    private void addGeofencestatically(){
        addGeofence();

    }

}
