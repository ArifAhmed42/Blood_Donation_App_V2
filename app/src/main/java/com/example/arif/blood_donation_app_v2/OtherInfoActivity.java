package com.example.arif.blood_donation_app_v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OtherInfoActivity extends AppCompatActivity {
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);

        login = (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();
                Intent intent = new Intent(OtherInfoActivity.this, EntryCheckingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
