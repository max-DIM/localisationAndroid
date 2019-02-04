package com.maximedim.localisation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LocationService extends Service {

    public final String ACTION_LOCATION = "com.maximedim.location.LOCATION";
    private final String TAG = LocationService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i(TAG, "lat: " + Double.toString(location.getLatitude()) + " et long: " + Double.toString(location.getLongitude()));
                notifyLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return START_STICKY;

    }

    public void notifyLocation(Location location){
        Intent intent = new Intent(ACTION_LOCATION);
        intent.putExtra("provider", location.getProvider());
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
