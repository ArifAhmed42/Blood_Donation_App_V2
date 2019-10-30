package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RegisterActivity extends AppCompatActivity {

    private Button proceed;
    private EditText name , email , phoneNumber , bloodGroup , donationFrequency , disease , password , confirmPassword, state, country;
    private String Sname , Semail , SphoneNumber , SbloodGroup , SdonationFrequency , Sdisease , Spassword , SconfirmPassword , Sstate, Scountry;

    private ProgressDialog dialog;
    private DatabaseReference refdatabase;
    private FirebaseAuth mAuth;
    private Boolean dataInputStatus=false;
    private  double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        proceed              = (Button) findViewById(R.id.proceed);
        name                = (EditText) findViewById(R.id.name);
        email               = (EditText) findViewById(R.id.email);
        phoneNumber         = (EditText) findViewById(R.id.number);
        bloodGroup          = (EditText) findViewById(R.id.blood_group);
        donationFrequency   = (EditText) findViewById(R.id.donation_frequency);
        disease             = (EditText) findViewById(R.id.disease);
        password            = (EditText) findViewById(R.id.password);
        confirmPassword     = (EditText) findViewById(R.id.confirm_password);
        state               = (EditText) findViewById(R.id.state);
        country             = (EditText) findViewById(R.id.country);

        dialog          = new ProgressDialog(RegisterActivity.this);
        mAuth           = FirebaseAuth.getInstance();
        refdatabase     = FirebaseDatabase.getInstance().getReference();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(RegisterActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RegisterActivity.this,"We are facing some problem to retrieve your location. Please check location permission and try again",Toast.LENGTH_LONG).show();
                    //requestPermission();
                }
                else {
                    getCurrentLocation();
                    if (lat != 0 && lng != 0) {
                        getInputData();

                        if (Sname.length() != 0 && SphoneNumber.length() != 0 && SbloodGroup.length() != 0 && SdonationFrequency.length() != 0 && Sdisease.length() != 0 && Spassword.length() != 0 && Semail.length() != 0 && SconfirmPassword.length() != 0) {

                            if (Spassword.length() >= 8 && Spassword.equals(SconfirmPassword)) {

                                createAccountAndStudentInfo();

                                if (dataInputStatus == true) {
                                    saveToDevice();
                                    dialog.setTitle("Please Wait");
                                    dialog.setMessage("Loading");
                                    dialog.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(3000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            dialog.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, Menu_Activity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).start();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Please check your internet connection and enter email or password correctly",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Enter the password carefully",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        } else {

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "All the fields must be filled",
                                    Toast.LENGTH_SHORT);
                            toast.show();

                        }

                    }

                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "We are facing some problems getting your location, please check your Internet connection",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
        }
        });
    }


    private void getInputData(){

        Sname = name.getText().toString();
        Semail = email.getText().toString();
        SphoneNumber = phoneNumber.getText().toString();
        SbloodGroup = bloodGroup.getText().toString();
        SdonationFrequency = donationFrequency.getText().toString();
        Sdisease = disease.getText().toString();
        Spassword = password.getText().toString();
        SconfirmPassword = confirmPassword.getText().toString();
        Sstate = state.getText().toString();
        Scountry = country.getText().toString();
    }

    private void saveToDevice(){

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",Sname);
        editor.putString("email",Semail);
        editor.putString("phone",SphoneNumber);
        editor.putString("bloodGroup",SbloodGroup);
        editor.putString("donationFrequency",SdonationFrequency);
        editor.putString("disease",Sdisease);
        editor.putString("password",Spassword);
        editor.putString("state",Sstate);
        editor.putString("country",Scountry);
        editor.putString("loginStatus","loggedin");
        editor.apply();
    }

    private void createAccountAndStudentInfo(){
        mAuth.createUserWithEmailAndPassword(Semail,Spassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        InfoClass info = new InfoClass(Sname, Semail , SphoneNumber , SbloodGroup , SdonationFrequency , Sdisease , Spassword , Sstate , Scountry , lat,lng,0.00);
                        FirebaseUser user = mAuth.getCurrentUser();
                        refdatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
                        refdatabase.child(user.getUid()).setValue(info);
                        dataInputStatus=true;

                    }

                    }
                });

    }

    public void getCurrentLocation(){

           FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(RegisterActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                return;
            }
            client.getLastLocation().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {

                       LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                       lat = latlng.latitude;
                       lng = latlng.longitude;
                    }

                }
            });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }




    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }
}
