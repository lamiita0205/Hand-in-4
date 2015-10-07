package com.example.lam.handin4;

import android.graphics.BitmapFactory;
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
import java.io.IOException;

public class FindYourself extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_yourself);

        MapFragment mapFragment;
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.gmap);

        mapFragment.getMapAsync(this);
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
                            addGeoTag(pos, file.getName(), gmap);
                        }
                    }
                }
            }
        }


    }

    private void addGeoTag(LatLng pos, String filename, GoogleMap gmap){
        gmap.setMyLocationEnabled(true);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));


        gmap.addMarker(new MarkerOptions()
                .position(pos)).setTitle(filename);
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this, "ImageName: " +marker.getTitle(),Toast.LENGTH_LONG).show();
        return true;


    }
}
