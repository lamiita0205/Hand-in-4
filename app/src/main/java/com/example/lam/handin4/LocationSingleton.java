package com.example.lam.handin4;

/**
 * Created by Lam on 08-Oct-15.
 */
public class LocationSingleton {
     public double getLatitude() {
        return lat;
    }

     public double getLongitude() {
        return lng;
    }
    public void set(double lat, double lng)
    {
        this.lat = lat;
        this.lng = lng;
    }
    private double lat;
    private double lng;
    private static LocationSingleton ourInstance = new LocationSingleton();

    public static LocationSingleton getInstance() {
        return ourInstance;
    }

    private LocationSingleton() {
        lat = lng = 0;
    }
}
