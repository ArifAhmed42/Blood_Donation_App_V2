package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RequestBloodActivity extends AppCompatActivity {

    private EditText patient_name, hospital_name, hospital_address , bed_number, phone_number,blood_group;
    private Button request_blood;

    private String Spatient_name, Shospital_name, Shospital_address , Sbed_number, Sphone_number,Sblood_group,Sdate;
    private RequestBloodInfoClass info;

    private DatabaseReference refdatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);


        patient_name = (EditText)findViewById(R.id.name);
        hospital_name = (EditText)findViewById(R.id.hospital_name);
        hospital_address = (EditText)findViewById(R.id.hospital_address);
        bed_number = (EditText)findViewById(R.id.bed_number);
        phone_number = (EditText)findViewById(R.id.phone);
        blood_group = (EditText)findViewById(R.id.bloodgroup);

        request_blood = (Button)findViewById(R.id.request_blood);

        dialog = new ProgressDialog(RequestBloodActivity.this);

        mAuth       = FirebaseAuth.getInstance();
        refdatabase = FirebaseDatabase.getInstance().getReference();

        request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputData();
                if(Spatient_name.length()!=0 && Shospital_name.length()!=0 && Shospital_address.length()!=0 && Sbed_number.length()!=0 && Sphone_number.length()!=0){
                    info = new RequestBloodInfoClass(Spatient_name,Shospital_name,Shospital_address,Sbed_number,Sphone_number,Sblood_group,Sdate);
                    FirebaseUser user = mAuth.getCurrentUser();
                    refdatabase = FirebaseDatabase.getInstance().getReference("bloodRequest");
                    refdatabase.child(user.getUid()).setValue(info);

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
                            Intent intent = new Intent(RequestBloodActivity.this, Menu_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).start();
                }

                else {
                    Toast.makeText(RequestBloodActivity.this, "Any of above fields can not be empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getInputData(){
        Spatient_name = patient_name.getText().toString();
        Shospital_name = hospital_name.getText().toString();
        Shospital_address = hospital_address.getText().toString();
        Sbed_number = bed_number.getText().toString();
        Sphone_number = phone_number.getText().toString();
        Sblood_group = blood_group.getText().toString();
        Sdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RequestBloodActivity.this, Menu_Activity.class);
        startActivity(intent);
        finish();

    }

}


