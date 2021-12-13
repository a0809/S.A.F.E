package com.thesis.safe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private TextView btnBack, txtFN, txtMN, txtLN, txtBday, txtEmail, txtPhone, txtStreet, txtBrgy, txtPass, txtPass1;
    private Spinner txtDistrict;
    private Button btnReg;
    private RadioButton male, female;
    static String address, phoneNumber, gender, first_name, last_name, mid_name, bday, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btnBack = findViewById(R.id.btnBack);
        txtDistrict = findViewById(R.id.txtDistrict);
        btnReg = findViewById(R.id.btnSignup);
        male = findViewById(R.id.radMale);
        female = findViewById(R.id.radFemale);
        txtFN = findViewById(R.id.txtFN);
        txtLN = findViewById(R.id.txtLN);
        txtMN = findViewById(R.id.txtMN);
        txtBday = findViewById(R.id.txtBday);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtStreet = findViewById(R.id.txtStreet);
        txtBrgy = findViewById(R.id.txtBrgy);
        txtPass = findViewById(R.id.txtPass);
        txtPass1 = findViewById(R.id.txtPass2);


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select District");
        arrayList.add("Sta. Mesa");
        arrayList.add("Sampaloc");
        arrayList.add("Paco");
        arrayList.add("Pandacan");
        arrayList.add("San Miguel");
        arrayList.add("Sta. Ana");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                         android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtDistrict.setAdapter(arrayAdapter);
        txtDistrict.setSelection(0);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean go = true;

                //address = "3305 Ramon Magsaysay Blvd. Brgy. 427";
                //phoneNumber = txtPhone.getText().toString();

                //Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                //startActivity(intent);

                if (txtLN.getText().toString().isEmpty()) {
                    txtLN.setError("Required field!");
                    go = false;
                }

                if (txtFN.getText().toString().isEmpty()) {
                    txtFN.setError("Required field!");
                    go = false;
                }

                if (txtBday.getText().toString().isEmpty()) {
                    txtBday.setError("Required field!");
                    go = false;
                }

                if (!male.isChecked() && !female.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Select your gender!", Toast.LENGTH_SHORT).show();
                    go = false;
                } else if (male.isChecked()) {
                    gender = "Male";
                } else if (female.isChecked())
                {
                    gender = "Female";
                }

                if(txtEmail.getText().toString().isEmpty())
                {
                    txtEmail.setError("Required field!");
                    go = false;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                {
                    txtEmail.setError("Invalid email!");
                    go = false;
                }

                if(txtPhone.getText().toString().isEmpty())
                {
                    txtPhone.setError("Required field!");
                    go = false;
                }

                if(txtDistrict.getSelectedItem().toString().equals("Select District"))
                {
                    Toast.makeText(RegisterActivity.this, "Select your district!", Toast.LENGTH_SHORT).show();
                    go = false;
                }

                if(txtStreet.getText().toString().isEmpty())
                {
                    txtStreet.setError("Required field!");
                    go = false;
                }

                if(txtBrgy.getText().toString().isEmpty())
                {
                    txtBrgy.setError("Required field!");
                    go = false;
                }

                if(txtPass.getText().toString().isEmpty())
                {
                    txtPass.setError("Required field!");
                    go = false;
                }

                if(txtPass1.getText().toString().isEmpty())
                {
                    txtPass1.setError("Retype your password!");
                    go = false;
                }



                if (go)
                {
                    if (txtPass1.getText().toString().equals(txtPass.getText().toString()))
                    {
                        address = txtStreet.getText().toString() + " Brgy. " + txtBrgy.getText().toString() + " " + txtDistrict.getSelectedItem().toString();
                        phoneNumber = txtPhone.getText().toString();
                        first_name = txtFN.getText().toString();
                        if (!txtMN.getText().toString().isEmpty())
                            mid_name = txtMN.getText().toString();
                        last_name = txtLN.getText().toString();
                        bday = txtBday.getText().toString();
                        email = txtEmail.getText().toString();
                        pass = txtPass.getText().toString();



                        Intent intent1 = new Intent(RegisterActivity.this, MapsActivity.class);
                        startActivity(intent1);

                    }
                    else
                    {
                        txtPass.setError("Password doesn't match!");
                        txtPass1.setError("Password doesn't match!");
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Fill up all the required fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}