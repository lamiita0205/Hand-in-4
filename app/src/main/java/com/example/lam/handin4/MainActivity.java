package com.example.lam.handin4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    private Button exitButton;
    private Button takePictureButton;
    private Button mapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePictureButton = (Button)findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(clickOnTakePicture);

        mapButton = (Button)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(clickOnMap);

    }


    View.OnClickListener clickOnMap = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            Intent mapIntent = new Intent(MainActivity.this, FindYourself.class);
            startActivity(mapIntent);

        }
    };
    View.OnClickListener clickOnTakePicture = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            Intent takePictureIntent = new Intent(MainActivity.this, TakePicture.class);
            startActivity(takePictureIntent);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void OnResume(){
        super.onResume();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(status == ConnectionResult.SUCCESS)
        {
            Toast.makeText(this, "Google Play Services Are Available", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this, "Google Play Services Are not available", Toast.LENGTH_LONG).show();
        }
    }
}
