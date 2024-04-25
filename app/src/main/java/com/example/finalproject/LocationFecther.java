package com.example.finalproject;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
//OLD Code

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationFecther {
    public interface LocationFetchListener {
        void onLocationsFetched(List<double[]> locationData,List<String> names,List<String>risks);
        void onError(String errorMessage);
    }

    public void fetchLocations(LocationFetchListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationsRef = database.getReference().child("locations");
        locationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<double[]> locationData = new ArrayList<>();
                List<String> names= new ArrayList<>();
                List<String>risks=new ArrayList<>();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    double latitude = locationSnapshot.child("latitude").getValue(Double.class);
                    double longitude = locationSnapshot.child("longitude").getValue(Double.class);
                    String name= locationSnapshot.child("name").getValue(String.class);
                    String risk=locationSnapshot.child("risk").getValue(String.class);
                    locationData.add(new double[]{latitude, longitude});
                    risks.add(risk);
                    names.add(name);
                }
                listener.onLocationsFetched(locationData,names,risks);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

}
