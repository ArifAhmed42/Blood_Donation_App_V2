package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arif.blood_donation_app_v2.ProfileActivity;

public class ProggressBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proggress_bar);

        final ProgressDialog progressDialog = new ProgressDialog(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                Intent intent = new Intent(ProggressBarActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
