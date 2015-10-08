package com.example.lam.handin4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePicture extends AppCompatActivity {

    private Button takePictureButton;
    private Button backButton;
    private ImageView iv;

    public static final int MY_REQUEST_CODE = 42;
    public static final String MY_LASTFILENAME = "MY_LASTFILENAME";

    File mypicturedirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        //Get directory for saving images. Create if it does not exist
        //Create a folder inside: DIRECTORY_PICTURES
        mypicturedirectory = myGetPictureDirectory();

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(clickOnBack);

        takePictureButton = (Button) findViewById(R.id.cameraButton);
        takePictureButton.setOnClickListener(clickOnTakePicture);

        iv = (ImageView) findViewById(R.id.photo);
    }

    View.OnClickListener clickOnTakePicture = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //Create unique filename, and create file for image
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = mypicturedirectory.getPath()+File.separator+"IMG_"+timestamp+".jpg";
            File imagefile = new File(filename);
            Uri imageuri = Uri.fromFile(imagefile);

            //Save imagepath to preferences (why?)
            mySaveLastAttemptedImageCaptureFilename(filename);

            //Take picture with implicit intent: MediaStore.ACTION_IMAGE_CAPTURE
            //Use putextra on the intent to set URI as EXTRA_OUTPUT
            myTakePictureMethod(imageuri);
        }
    };


    View.OnClickListener clickOnBack = new  View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void mySetExif() throws IOException {

        //Get the filename from preferences
        String filename = myGetLastAttemptedImageCaptureFilename();

        ExifInterface exif = new ExifInterface(filename);
        int or = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        TextView t2 = (TextView) findViewById(R.id.tv2);
        TextView t3 = (TextView) findViewById(R.id.tv3);

        float[] latlong = new float[2];
        if(exif.getLatLong(latlong)){
            t2.setText("Latitude: "+latlong[0]);
            t3.setText("Longitude: "+latlong[1]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get the filename from preferences
        String filename = myGetLastAttemptedImageCaptureFilename();

        //Put image in image view
        setPictureToImageView(filename, iv);

        try {
            mySetExif();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void mySaveLastAttemptedImageCaptureFilename(String filename){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MY_LASTFILENAME, filename);
        editor.commit();
    }

    private String myGetLastAttemptedImageCaptureFilename() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(MY_LASTFILENAME,"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK){

            //Get the filename from preferences
            String filename = myGetLastAttemptedImageCaptureFilename();

            //Put image in image view
            setPictureToImageView(filename, iv);
        }
    }

    private void setPictureToImageView(String filename, ImageView iv) {
        // Dimensions of your the View
        int targetW = 380;
        int targetH = 320;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(filename, bmOptions);

        // Do proper rotation according to EXIF
        iv.setRotation(0);


        //Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }

    private void myTakePictureMethod(Uri imageuri){

        Intent myintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        myintent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);

        if(myintent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(myintent, MY_REQUEST_CODE);
        }

    }

    private boolean myStorageMountedAndWriteableCheck() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File myGetPictureDirectory() {
        File pth = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File pdir = new File(pth,"MyCameraApp");
        return pdir;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_picture, menu);
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


}
