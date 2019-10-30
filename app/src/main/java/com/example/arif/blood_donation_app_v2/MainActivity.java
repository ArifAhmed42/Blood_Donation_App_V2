package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.onesignal.OneSignal;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        SharedPreferences sharedPref   = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String loginStatus       = sharedPref.getString("loginStatus","");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();



                    if (loginStatus.equals("loggedin")) {
                        Intent intent = new Intent(MainActivity.this, Menu_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MainActivity.this, EntryCheckingActivity.class);
                        startActivity(intent);
                        finish();
                    }


            }
        }).start();

    }

}