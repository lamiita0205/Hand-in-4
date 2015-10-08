package com.example.lam.handin4;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;

public class DisplayMarker extends AppCompatActivity {

    private EditText notes;
    private ImageView picture;
    private Button saveButton;
    private final static String STORETEXT="storetext.txt";
    private String filename;
    private ExifInterface exif;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_marker);

        notes = (EditText) findViewById(R.id.notes);

        saveButton = (Button)findViewById(R.id.save);
        saveButton.setOnClickListener(clickSave);


        picture = (ImageView) findViewById(R.id.pictureFromMap);

        Bundle b = getIntent().getExtras();
        filename = b.getString("filename");

        File picturedir = null;
        try {
             picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyCameraApp/");
             exif = new ExifInterface(picturedir.getAbsolutePath()+"/"+filename);
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Error creating EXIF interface", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), picturedir.getAbsolutePath()+"/"+filename, Toast.LENGTH_LONG).show();
        loadSavedPreferences();
        File image = new File(picturedir, filename);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = decodeSampledBitmapFromPath(image.getAbsolutePath(), 400, 300);
        picture.setImageBitmap(bitmap);
    }

    private void loadSavedPreferences() {
       /* SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String text = sharedPreferences.getString(filename, "");*/
        String text;
        if(exif == null)
            text = "null exif";
        else
            text = exif.getAttribute("UserComment");
        notes.setText(text);
    }


    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    View.OnClickListener clickSave = new View.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //savePreferences(filename, notes.getText().toString());
            ///Toast.makeText(getApplicationContext(),picturedir.getAbsolutePath()+"/"+filename, Toast.LENGTH_LONG ).show();
            exif.setAttribute("UserComment", notes.getText().toString());
            try {
                exif.saveAttributes();
                Toast.makeText(getApplicationContext(), exif.getAttribute("UserComment"), Toast.LENGTH_LONG).show();

            }catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Error saving note", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    };

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_marker, menu);
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
