package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    LatLng latlng;
    private MarkerOptions mMarker;
    int PROXIMITY_RADIUS = 10000;
    private ProgressDialog dialog;
    private boolean networkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dialog = new ProgressDialog(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPermission();
        getCurrentLocation();
    }

    public void getCurrentLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){

                    latlng = new LatLng(location.getLatitude(),location.getLongitude());
                    Object dataTransfer[] = new Object[3];
                    GetNearbyPlaces getNearbyPlacesData = new GetNearbyPlaces();
                    mMap.clear();
                    String hospital = "hospital";
                    String url = getUrl(location.getLatitude(), location.getLongitude(), hospital);
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = latlng;

                    getNearbyPlacesData.execute(dataTransfer);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDgdCkpn_Xzci1G_WM6LNk1ZPecg2lmbWc");

        return googlePlaceUrl.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences sharedPref   = getSharedPreferences("UserType", Context.MODE_PRIVATE);
        String usertype       = sharedPref.getString("usertype","");

        if(usertype.equals("donor")) {
            Intent intent = new Intent(MapsActivity.this, Menu_Activity.class);
            startActivity(intent);
            finish();
        }

        else if(usertype.equals("request")) {

           Intent intent = new Intent(MapsActivity.this, MenuForRequestActivity.class);
           startActivity(intent);
           finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

            dialog.setTitle("Please Wait");
            dialog.setMessage("Loading");
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }
            }).start();
    }

}
