package com.example.arif.blood_donation_app_v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;

public class MenuForRequestActivity extends AppCompatActivity {

    private CardView hospital, donors,other;
    private Boolean networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_for_request);

        networkState = Menu_Activity.isConnected();

        if(networkState!=true){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MenuForRequestActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MenuForRequestActivity.this);
            }
            builder.setTitle("No Internet Connection")
                    .setMessage("We need to have active Internet Connection to work properly. Make sure you have an active Internet Connection and try again.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            MenuForRequestActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


        hospital = (CardView)findViewById(R.id.hospital);
        donors = (CardView)findViewById(R.id.donors);
        other = (CardView)findViewById(R.id.other);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuForRequestActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuForRequestActivity.this, DonorListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuForRequestActivity.this, OtherInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
