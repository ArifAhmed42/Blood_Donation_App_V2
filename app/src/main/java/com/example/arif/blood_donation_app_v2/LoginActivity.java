package com.example.arif.blood_donation_app_v2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private Button logIn;
    private EditText email , password;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private DatabaseReference mReference;
    private FirebaseUser user;
    private InfoClass infoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView)findViewById(R.id.register);
        logIn    = (Button) findViewById(R.id.login);
        email    = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        dialog = new ProgressDialog(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Semail = email.getText().toString();
                final String Spassword = password.getText().toString();
                if(Semail.length()!=0&&password.length()!=0) {

                    mAuth.signInWithEmailAndPassword(Semail, Spassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Please check your Internet connection and enter registered email and password correctly.", Toast.LENGTH_LONG).show();
                            } else {

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                mReference = FirebaseDatabase.getInstance().getReference("UserInfo");

                                mReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        infoClass = dataSnapshot.child(user.getUid()).getValue(InfoClass.class);
                                        saveToDevice();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                Intent intent = new Intent(LoginActivity.this, Menu_Activity.class);
                                startActivity(intent);
                                finish();

                                return;
                            }
                        }
                    });

                }

                else {
                    Toast.makeText(LoginActivity.this, "Those fields cannot be empty",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        }

    private void saveToDevice(){

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",infoClass.getName());
        editor.putString("email",infoClass.getEmail());
        editor.putString("phone",infoClass.getPhoneNumber());
        editor.putString("bloodGroup",infoClass.getBloodGroup());
        editor.putString("donationFrequency",infoClass.getDonateFrequency());
        editor.putString("disease",infoClass.getDisease());
        editor.putString("loginStatus","loggedin");
        editor.apply();
    }

    private void OneSignalLogIn(){
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, EntryCheckingActivity.class);
        startActivity(intent);
        finish();

    }


}
