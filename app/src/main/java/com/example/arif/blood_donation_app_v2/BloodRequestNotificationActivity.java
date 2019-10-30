package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BloodRequestNotificationActivity extends AppCompatActivity {

    private RecyclerView mRequestView;
    private DatabaseReference mDatabase;
    private ProgressDialog dialog;
    private boolean networkStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_notification);

        mRequestView = (RecyclerView) findViewById(R.id.myrecycleview);
        mRequestView.setHasFixedSize(true);
        mRequestView.setLayoutManager(new LinearLayoutManager(this));


        networkStatus = Menu_Activity.isConnected();
        if (networkStatus != false) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("bloodRequest");
            mDatabase.keepSynced(true);

        FirebaseRecyclerOptions<RequestBloodInfoClass> options =
                new FirebaseRecyclerOptions.Builder<RequestBloodInfoClass>()
                        .setQuery(mDatabase, RequestBloodInfoClass.class)
                        .build();

        FirebaseRecyclerAdapter<RequestBloodInfoClass, RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestBloodInfoClass, RequestViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull RequestBloodInfoClass model) {

                holder.setBloodGroup(model.getBlood_group());
                holder.setPatientName(model.getPatient_name());
                String wardNumber = model.getBed_number();
                String hospitalName = model.getHospital_name();
                String hospitalAddress = model.getHospital_address();
                String patientInfo = wardNumber + " , " + hospitalName + " , " + hospitalAddress + " , ";
                holder.setPatientInfo(patientInfo);
                String date = "Request posted : " + model.getDate();
                holder.setPostingDate(date);
                final String phoneNumber = "tel:" + model.getPhone_number();
                holder.setPatientNumber(phoneNumber);
                holder.callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(BloodRequestNotificationActivity.this, phoneNumber ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(phoneNumber));
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_card_layout, parent, false);

                return new RequestViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        mRequestView.setAdapter(firebaseRecyclerAdapter);
    }

    else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(BloodRequestNotificationActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(BloodRequestNotificationActivity.this);
            }
            builder.setTitle("No Internet Connection")
                    .setMessage("We need to have active Internet Connection to work properly. Make sure you have an active Internet Connection and try again.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            BloodRequestNotificationActivity.this.finish();
                            //System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Button callButton;

        public RequestViewHolder(View itemView){

            super(itemView);
            mView=itemView;
            callButton = (Button)itemView.findViewById(R.id.view_button_call);
        }

        public void setBloodGroup(String sbloodGroup){
            TextView bloodGroup = (TextView)mView.findViewById(R.id.card_blood_group);
            bloodGroup.setText(sbloodGroup);
        }

        public void setPatientName(String sPatientName){
            TextView patientName = (TextView)mView.findViewById(R.id.view_patient_name);
            patientName.setText(sPatientName);
        }

        public void setPatientInfo(String sPatientInfo){
            TextView patientInfo = (TextView)mView.findViewById(R.id.view_patient_info);
            patientInfo.setText(sPatientInfo);
        }

        public void setPatientNumber(String sPatientNumber){
            TextView patientNumber = (TextView)mView.findViewById(R.id.view_patient_phone);
            patientNumber.setText(sPatientNumber);
        }

        public void setPostingDate(String sDate){
            TextView date = (TextView)mView.findViewById(R.id.view_date);
            date.setText(sDate);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BloodRequestNotificationActivity.this, Menu_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dialog = new ProgressDialog(BloodRequestNotificationActivity.this);
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
