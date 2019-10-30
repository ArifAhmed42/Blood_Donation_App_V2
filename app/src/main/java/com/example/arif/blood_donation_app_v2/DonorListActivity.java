package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import java.util.ArrayList;
public class DonorListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ProgressDialog dialog;
    private double currLatitude,currLongitude;
    private ArrayList<InfoClass>list;
    String Semail;
    private boolean networkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        final ListView listView = (ListView) findViewById(R.id.listview);

        networkStatus = Menu_Activity.isConnected();

        if (networkStatus != false)
        {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserInfo");
            mDatabase.keepSynced(true);

        list = new ArrayList<InfoClass>();

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Semail = sharedPref.getString("email", "");


        getCurrentLocation();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot val : dataSnapshot.getChildren()) {
                    InfoClass object = val.getValue(InfoClass.class);
                    if (!Semail.equals(object.getEmail())) {
                        double distance = calculateDistance(object.getLatitute(), object.getLongitute());
                        object.setDistance(distance);
                        list.add(object);
                    }
                    // Toast.makeText(DonorListActivity.this," "+list.size(),Toast.LENGTH_LONG).show();
                }

                Collections.sort(list, new Comparator<InfoClass>() {
                    @Override
                    public int compare(InfoClass t0, InfoClass t1) {
                        return t0.getDistance().compareTo(t1.getDistance());
                    }
                });

                CustomAdapter c = new CustomAdapter(DonorListActivity.this, list);
                listView.setAdapter(c);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(DonorListActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(DonorListActivity.this);
            }
            builder.setTitle("No Internet Connection")
                    .setMessage("We need to have active Internet Connection to work properly. Make sure you have an active Internet Connection and try again.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            DonorListActivity.this.finish();
                            //System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    public double calculateDistance(double lat, double lng){
       /* double theta = currLongitude - lng;
        double dist = Math.sin(deg2rad(currLatitude))
                * Math.sin(deg2rad(lat))
                + Math.cos(deg2rad(currLatitude))
                * Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        */

        double earthRadius = 6371.00;

        double dLat = Math.toRadians(currLatitude-lat);
        double dLng = Math.toRadians(currLatitude-lng);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(currLatitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void getCurrentLocation(){

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(DonorListActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(DonorListActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    currLatitude = latlng.latitude;
                    currLongitude = latlng.longitude;
                    //Toast.makeText(DonorListActivity.this, "" + currLatitude+","+currLongitude , Toast.LENGTH_SHORT).show();
                    //calculateDistance();
                }

            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPref   = getSharedPreferences("UserType", Context.MODE_PRIVATE);
        String usertype       = sharedPref.getString("usertype","");

        if(usertype.equals("donor")) {
            Intent intent = new Intent(DonorListActivity.this, Menu_Activity.class);
            startActivity(intent);
            finish();
       }

        else if(usertype.equals("request")) {

            Intent intent = new Intent(DonorListActivity.this, MenuForRequestActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
   protected void onStart() {
        super.onStart();
        requestPermission();
        getCurrentLocation();
        dialog = new ProgressDialog(DonorListActivity.this);
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
