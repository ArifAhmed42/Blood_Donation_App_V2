package com.example.arif.blood_donation_app_v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntryCheckingActivity extends AppCompatActivity {

   private Button donor , blood_bank , request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_checking);


        donor = (Button)findViewById(R.id.donor);
        blood_bank = (Button)findViewById(R.id.blood_bank);
        request = (Button)findViewById(R.id.request);

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("usertype","donor");
                editor.apply();

                Intent intent = new Intent(EntryCheckingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        blood_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryCheckingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("usertype","request");
                editor.apply();

                Intent intent = new Intent(EntryCheckingActivity.this, MenuForRequestActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
