package com.example.lam.handin4;

/**
 * Created by Dan Iorga on 10/7/2015.
 */

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class UpdateWidgetService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener mlocListener = new MyLocationListener();
        try {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mlocListener);
        }catch(Exception e) {
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());
        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        RemoteViews remoteViews = new RemoteViews(this
                .getApplicationContext().getPackageName(),
                R.layout.nearby_layout);
        // Register an onClickListener
        Intent clickIntent = new Intent(this.getApplicationContext(),
                nearby_class.class);

        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                allWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn, pendingIntent);

        for (int widgetId : allWidgetIds) {

            // Set the text
            int picturesNear = getPicturesNearby();
            String message;
            switch (picturesNear) {
                case 0:
                    message = "No pictures taken nearby. (500m)";
                    break;
                case 1:
                    message = "One picture taken nearby. (500m)";
                    break;
                default:
                    message = picturesNear + " pictures taken nearby. (500m)";
                    break;
            }
            remoteViews.setTextViewText(R.id.update,
                    message);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int getPicturesNearby() {
        File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyCameraApp");
        int picturesNear = 0;

        if (picturedir == null)
            return 0;
        File[] pictures = picturedir.listFiles();
        if (pictures == null)
            return 0;
        for (int i = 0; i < pictures.length; i++) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(pictures[i].getAbsolutePath());
            } catch (Exception e) {
                continue;
            }
            float[] lat = new float[2];
            exif.getLatLong(lat);
            float[] dist = new float[1];
            Location.distanceBetween(lat[0],lat[1], LocationSingleton.getInstance().getLatitude(), LocationSingleton.getInstance().getLongitude(), dist);
            if(dist[0]<500)
                picturesNear++;
        }
        return picturesNear;
    }


    private class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location loc) {
           LocationSingleton.getInstance().set(loc.getLatitude(), loc.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}