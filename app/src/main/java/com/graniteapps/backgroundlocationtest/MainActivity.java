package com.graniteapps.backgroundlocationtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "MainActivity";

    final Handler handler = new Handler();

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, 42);
                        toggleButton.setChecked(false);
                    } else {
                        handler.post(requestUpdates);
                    }
                } else {
                    stopListening();
                }
            }
        });
    }

    final Runnable requestUpdates = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            locationManager.removeUpdates(MainActivity.this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    0, MainActivity.this);

            handler.postDelayed(this, 5000);
        }
    };

    private void stopListening() {
        handler.removeCallbacks(requestUpdates);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
        Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged() called with: provider = [" + provider + "], status = ["
                + status + "], extras = [" + extras + "]");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled() called with: provider = [" + provider + "]");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled() called with: provider = [" + provider + "]");
    }
}
