//OLD Code
package com.example.finalproject;

import android.content.Context;

import com.example.finalproject.LocationFecther;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DataLocations {

    private FirebaseDatabase database;

    public DataLocations(Context context) {
        database = FirebaseDatabase.getInstance();
    }

    public DataLocations() {

    }

    public interface DataFetchListener {
        void onDataFetched(double[][] locations);

        void onDataFetched(double[][] data, String[] names, String[] risks);
        void onError(String errorMessage);
    }
    public void fetchLocationData(DataFetchListener listener) {
        LocationFecther f=new LocationFecther();
        f.fetchLocations(new LocationFecther.LocationFetchListener() {
            @Override
            public void onLocationsFetched(List<double[]> locationData,List<String> names,List<String> risks) {
                double[][] locations = convertToList(locationData);
                String[] name = convertToList2(names);
                String[] risk=convertToList2(risks);
                listener.onDataFetched(locations,name,risk);

            }


            @Override
            public void onError(String errorMessage) {
                listener.onError(errorMessage);
            }
        });
    }
    private double[][] convertToList(List<double[]> dataList) {
        int numRows = dataList.size();
        int numCols = dataList.get(0).length;

        double[][] result = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            double[] row = dataList.get(i);
            for (int j = 0; j < numCols; j++) {
                result[i][j] = row[j];
            }
        }

        return result;
    }
    public static String[] convertToList2(List<String> dataList) {
        String[] result = dataList.toArray(new String[0]); // Efficient and type-safe
        return result;
    }
}

