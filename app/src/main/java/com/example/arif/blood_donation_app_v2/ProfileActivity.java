package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button logout, back;
    private TextView bloodGroup,email,phone,name;
    private ProgressDialog dialog;
    private String Sname,Semail,Sphone,SbloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPref   = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Sname       = sharedPref.getString("name","");
        Semail      = sharedPref.getString("email","");
        Sphone      = sharedPref.getString("phone","");
        SbloodGroup = sharedPref.getString("bloodGroup","");

        logout = (Button)findViewById(R.id.logout);
        back = (Button)findViewById(R.id.back);
        bloodGroup = (TextView)findViewById(R.id.txtview_blood_group);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);
        name = (TextView)findViewById(R.id.name);
        dialog = new ProgressDialog(ProfileActivity.this);

        name.setText(Sname);
        email.setText(Semail);
        phone.setText(Sphone);
        bloodGroup.setText(SbloodGroup);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Menu_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                sharedPref.edit().clear().commit();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, Menu_Activity.class);
        startActivity(intent);
        finish();

    }
}
