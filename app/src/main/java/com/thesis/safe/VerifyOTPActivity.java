package com.thesis.safe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyOTPActivity extends AppCompatActivity {
    private PinView pin_view;
    private Button btnVerify;
    private TextView phoneNum;
    private FirebaseAuth mAuth;
    private String verificationId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        try {
            mAuth = FirebaseAuth.getInstance();
            sendVerificationCode(RegisterActivity.phoneNumber);
        } catch (Exception e)
        {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pin_view = findViewById(R.id.pin_view);
        btnVerify = findViewById(R.id.btnVerify);
        phoneNum = findViewById(R.id.otp_description_text);

        phoneNum.setText("We sent a 6 digit verification code to " + RegisterActivity.phoneNumber.toString());

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyVerificationCode(pin_view.getText().toString());

            }
        });


    }

    private void sendVerificationCode(String mobile) {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mobile,
                    60,
                    TimeUnit.SECONDS,
                    TaskExecutors.MAIN_THREAD,
                    mCallbacks);
        }

        catch (Exception e)
        {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            verificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        }

        catch (Exception e)
        {


            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    boolean success;
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        success = false;
        try {


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            success = true;

                        } else {
                            success = false;
                                Toast.makeText(VerifyOTPActivity.this, "You have entered an invalid code!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Users");

            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("LastName", RegisterActivity.last_name);
            usersInfo.put("FirstName", RegisterActivity.first_name);
            usersInfo.put("MiddleName", RegisterActivity.mid_name);
            usersInfo.put("PhoneNumber", RegisterActivity.phoneNumber);
            usersInfo.put("Gender", RegisterActivity.gender);
            usersInfo.put("Address", RegisterActivity.address);
            usersInfo.put("Birthday", RegisterActivity.bday);
            usersInfo.put("Email", RegisterActivity.email);
            usersInfo.put("Password", RegisterActivity.pass);

            databaseReference.child(RegisterActivity.phoneNumber).setValue(usersInfo);


        }

        catch (Exception e)
        {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /*if (success)
        {
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            mAuth
                    .createUserWithEmailAndPassword(RegisterActivity.email, RegisterActivity.pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Registration successful!",
                                        Toast.LENGTH_LONG)
                                        .show();
                                Intent intent = new Intent(VerifyOTPActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration failed!!"
                                                + " Please try again later",
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                        }
                    });
        }*/
    }
}
