package com.example.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
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

public class MapsFragment extends Fragment implements OnMapReadyCallback ,ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap myMap;
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
    String Loc[] = {"Dhankawadi","Deccan Gymkhana","Aundh","Baner","Viman Nagar","Kothrud","Wakad","Hadapsar","Magarpatta City","Koregaon Park","Shivaji Nagar"};

    private int currentindex = 0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        getDeviceLocation();
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                        }
                    });
        } catch (SecurityException e) {
            Log.e("MapsFragment", "Error getting location: " + e.getMessage());
        }
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

    private void addGeofence() {
        // List<Geofence> geofenceList = new ArrayList<>();
        /*float radius = GEOFENCE_RADIUS; // Set your desired radius in meters
        int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT; // Entering or exiting triggers

// Assuming GEOFENCE_CENTER1 and GEOFENCE_CENTER2 are objects with latitude and longitude properties
        /*double[][] locationData = {
                {GEOFENCE_CENTER1.latitude, GEOFENCE_CENTER1.longitude},
                {GEOFENCE_CENTER2.latitude, GEOFENCE_CENTER2.longitude}
        };*/

    /*    for (int i = 0; i < d.locationData.length; i++) {
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

     */
       /* double[][] locationData2 =d.getloc();
        for (int i = 0; i < locationData2.length; i++) {
            double latitude = locationData2[i][0];
            double longitude = locationData2[i][1];

            // Add other parameters if needed (e.g., unique request ID)
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("important_location_" + i) // Optional: Unique ID
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionT6ypes(transitionType)
                    .build();
            geofenceList.add(geofence);
            LatLng latLng = new LatLng(latitude ,longitude);
            addCircle(latLng, GEOFENCE_RADIUS);
            addMarker(latLng,Loc[currentindex]);
            currentindex++;
        }*/
        DataLocations dataLocations = new DataLocations();
        dataLocations.fetchLocationData(new DataLocations.DataFetchListener() {
            @Override
            public void onDataFetched(double[][] locations) {
                List<Geofence> geofenceList = new ArrayList<>();
                float radius = GEOFENCE_RADIUS; // Set your desired radius in meters
                int transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT; // Entering or exiting triggers

                for (int i = 0; i < locations.length; i++) {
                    double latitude = locations[i][0];
                    double longitude = locations[i][1];

                    // Add other parameters if needed (e.g., unique request ID)
                    Geofence geofence = new Geofence.Builder()
                            .setRequestId("important_location_" + i) // Optional: Unique ID
                            .setCircularRegion(latitude, longitude, radius)
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(transitionType)
                            .build();
                    geofenceList.add(geofence);
                    LatLng latLng = new LatLng(latitude, longitude);
                    addCircle(latLng, GEOFENCE_RADIUS);
                    addMarker(latLng, Loc[currentindex]);
                    currentindex++;
                }
                GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                        .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER) // Optional: define initial trigger
                        .addGeofences(geofenceList) // Add all geofences at once
                        .build();
            }

            @Override
            public void onDataFetched(double[][] data, String[] names, String[] risks) {

            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });




        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
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

    }

    // Other methods such as adding markers, circles, and handling permissions can be included here.
}
