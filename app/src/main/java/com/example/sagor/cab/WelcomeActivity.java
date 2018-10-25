package com.example.sagor.cab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button welcomeDriverButton;
    private Button welcomeCustomerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeDriverButton = findViewById(R.id.btn_driver);
        welcomeCustomerButton = findViewById(R.id.btn_customer);

        welcomeDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginDriverRegisteryIntent = new Intent(WelcomeActivity.this, DriverLoginRegisteryActivity.class);
                startActivity(loginDriverRegisteryIntent);
            }
        });

        welcomeCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginCustomerRegisteryIntent = new Intent(WelcomeActivity.this, CustomerLoginRegisteryActivity.class);
                startActivity(loginCustomerRegisteryIntent);
            }
        });

    }
}
