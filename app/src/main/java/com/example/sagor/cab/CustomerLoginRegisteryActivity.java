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

public class CustomerLoginRegisteryActivity extends AppCompatActivity {

    private Button customerLoginButton;
    private Button customerRegisteryButton;
    private TextView customerRegisteryLink;
    private TextView customerStatus;

    private EditText customerEmail;
    private EditText customerPassword;

    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference customerDatabaseRef;
    private String onlineCustomerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_registery);

        mAuth               = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        customerLoginButton     = findViewById(R.id.btn_customer_login);
        customerRegisteryButton = findViewById(R.id.btn_customer_registery);
        customerRegisteryLink   = findViewById(R.id.customer_registery_link);
        customerStatus          = findViewById(R.id.customer_status);

        customerEmail           = findViewById(R.id.customer_email);
        customerPassword        = findViewById(R.id.customer_password);

        customerRegisteryButton.setVisibility(View.INVISIBLE);
        customerRegisteryButton.setEnabled(false);

        customerRegisteryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerLoginButton.setVisibility(View.INVISIBLE);
                customerRegisteryLink.setVisibility(View.INVISIBLE);
                customerStatus.setText("Register Customer");

                customerRegisteryButton.setVisibility(View.VISIBLE);
                customerRegisteryButton.setEnabled(true);
            }
        });

        customerRegisteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = customerEmail.getText().toString();
                String password = customerPassword.getText().toString();
                registerCustomer(email, password);
            }
        });

        customerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = customerEmail.getText().toString();
                String password = customerPassword.getText().toString();
                signInCustomer(email, password);
            }
        });

    }

    private void registerCustomer(String email, String password) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(CustomerLoginRegisteryActivity.this,"Please write email...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(CustomerLoginRegisteryActivity.this,"Please write password...",Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Customer registration");
            loadingBar.setMessage("Please wait, while we are register your data...");
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        onlineCustomerID    = mAuth.getCurrentUser().getUid();
                        customerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerID);
                        customerDatabaseRef.setValue(true);

                        Intent customerIntent = new Intent(CustomerLoginRegisteryActivity.this, CustomersMapActivity.class);
                        startActivity(customerIntent);
                        Toast.makeText(CustomerLoginRegisteryActivity.this,"Customer Registration Successfully....",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else {
                        Toast.makeText(CustomerLoginRegisteryActivity.this,"Customer Registration Unsuccessful....",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void signInCustomer(String email, String password) {
        if (TextUtils.isEmpty(email)){
            Toast.makeText(CustomerLoginRegisteryActivity.this,"Please write email...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(CustomerLoginRegisteryActivity.this,"Please write password...",Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Customer registration");
            loadingBar.setMessage("Please wait, while we are register your crediantials...");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent customerIntent = new Intent(CustomerLoginRegisteryActivity.this, CustomersMapActivity.class);
                        startActivity(customerIntent);
                        Toast.makeText(CustomerLoginRegisteryActivity.this,"Customer Logged in Successfully....",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else {
                        Toast.makeText(CustomerLoginRegisteryActivity.this,"Login Unsuccessful, Please try again....",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
