package com.example.sagor.cab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginRegisteryActivity extends AppCompatActivity {

    private Button driverLoginButton;
    private Button driverRegisteryButton;
    private TextView driverRegisteryLink;
    private TextView driverStatus;

    private EditText driverEmail;
    private EditText driverPassword;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference driverDatabaseRef;
    private String onlineDriverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login_registery);

        mAuth = FirebaseAuth.getInstance();

        driverLoginButton     = findViewById(R.id.btn_driver_login);
        driverRegisteryButton = findViewById(R.id.btn_driver_registery);
        driverRegisteryLink   = findViewById(R.id.driver_registery_link);
        driverStatus          = findViewById(R.id.driver_status);

        driverEmail           = findViewById(R.id.driver_email);
        driverPassword        = findViewById(R.id.driver_password);

        loadingBar            = new ProgressDialog(this);

        driverRegisteryButton.setVisibility(View.INVISIBLE);
        driverRegisteryButton.setEnabled(false);

        driverRegisteryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverLoginButton.setVisibility(View.INVISIBLE);
                driverRegisteryLink.setVisibility(View.INVISIBLE);
                driverStatus.setText("Register Driver");

                driverRegisteryButton.setVisibility(View.VISIBLE);
                driverRegisteryButton.setEnabled(true);
            }
        });

        driverRegisteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = driverEmail.getText().toString();
                String password = driverPassword.getText().toString();
                registerDriver(email, password);
            }
        });

        driverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = driverEmail.getText().toString();
                String password = driverPassword.getText().toString();
                signInDriver(email, password);
            }
        });
    }

    private void registerDriver(String email, String password) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(DriverLoginRegisteryActivity.this,"Please write email...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(DriverLoginRegisteryActivity.this,"Please write password...",Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Driver Registration");
            loadingBar.setMessage("Please wait, while we are register your data...");
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        onlineDriverID    = mAuth.getCurrentUser().getUid();
                        driverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverID);
                        driverDatabaseRef.setValue(true);

                        Intent driverIntent = new Intent(DriverLoginRegisteryActivity.this, DriversMapActivity.class);
                        startActivity(driverIntent);
                        Toast.makeText(DriverLoginRegisteryActivity.this,"Driver Registration SuccessFully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else {
                        Toast.makeText(DriverLoginRegisteryActivity.this,"Driver Registration UnsuccessFul...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void signInDriver(String email, String password) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(DriverLoginRegisteryActivity.this,"Please write email...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(DriverLoginRegisteryActivity.this,"Please write password...",Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Driver Registration");
            loadingBar.setMessage("Please wait, while we are checking your credientials...");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent driverIntent = new Intent(DriverLoginRegisteryActivity.this, DriversMapActivity.class);
                        startActivity(driverIntent);
                        Toast.makeText(DriverLoginRegisteryActivity.this,"Driver Logged in SuccessFully...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else {
                        Toast.makeText(DriverLoginRegisteryActivity.this,"Login UnsuccessFul, Please try again...",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
