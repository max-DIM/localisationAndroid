package com.maximedim.localisation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final String ACTION_LOCATION = "com.maximedim.location.LOCATION";

    private final String TAG = MainActivity.class.getSimpleName();

    private TextView textViewLatitudeLongitude;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String provider = intent.getStringExtra("provider");
            Double lat = intent.getDoubleExtra("lat", 0.0);
            Double lng = intent.getDoubleExtra("lng", 0.0);
            textViewLatitudeLongitude.setText("lat: " + lat + " et long: " + lng + " et provider: " + provider);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLatitudeLongitude = findViewById(R.id.textViewLatituteLongitude);
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"resume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(TAG,"start");
        super.onStart();
        // Acquire a reference to the system Location Manager
        /*LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i(TAG, "lat: " + Double.toString(location.getLatitude()) + " et long: " + Double.toString(location.getLongitude()));
                textViewLatitudeLongitude.setText("lat: " + Double.toString(location.getLatitude()) + " et long: " + Double.toString(location.getLongitude()) + " et provider: " + location.getProvider());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };*/

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        /*try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }*/
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ACTION_LOCATION));
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"stop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"destroy");
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
}
