package com.example.lam.handin4;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FindYourself extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_yourself);

        MapFragment mapFragment;
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.gmap);

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

            String filename = marker.getTitle();

            Bundle b = new Bundle();
            b.putString("filename", filename);

            Intent displayMarkerIntent = new Intent(FindYourself.this, DisplayMarker.class);

            displayMarkerIntent.putExtras(b);
            startActivity(displayMarkerIntent);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_yourself, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);

        myFindImagesWithGeoTagAndAddToGmap(googleMap);
    }

    private void myFindImagesWithGeoTagAndAddToGmap(GoogleMap gmap) {

        String storagestate = Environment.getExternalStorageState();
        if(storagestate.equals(Environment.MEDIA_MOUNTED)){

            File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyCameraApp");

            if(picturedir.exists()){
                File[] files = picturedir.listFiles();
                for(File file : files){
                    if(file.getName().endsWith(".jpg")){

                        LatLng pos = getLatLongFromExif(file.getAbsolutePath());

                        if(pos!=null){
                            addGeoTag(pos, file.getName(), gmap, file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void addGeoTag(LatLng pos, String filename, GoogleMap gmap, String path) {
        gmap.setMyLocationEnabled(true);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));

        Bitmap bitmap = decodeSampledBitmapFromPath(path, 100, 100);

        myMarker = gmap.addMarker(new MarkerOptions()
                .position(pos).title(filename).icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private LatLng getLatLongFromExif(String filename){
        float lalo[] = new float[2];
        LatLng pos = null;
        try{
            ExifInterface exifi = new ExifInterface(filename);
            if(exifi.getLatLong(lalo)){
                pos = new LatLng(lalo[0], lalo[1]);
            }
            else {
                return null;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return pos;
    }

    /*@Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, "ImageName: " +marker.getTitle(),Toast.LENGTH_LONG).show();
        return true;


    }*/
}
