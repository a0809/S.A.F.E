package com.thesis.safe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.location.*;
import android.widget.RadioButton;
import android.widget.Toast;
//import com.google.android.gms.;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ReportFireActivity extends AppCompatActivity {

    private Button btnSubmit;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    RadioButton radIn, radNear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_fire_incident);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm");
        String date = sdf.format(new Date());

        radIn = findViewById(R.id.radIN);
        radNear = findViewById(R.id.radNear);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            OnGPS();
                        }
                        else
                        {
                            getLocation();

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("FireIncidents");

                            HashMap<String, String> incidentInfo = new HashMap<>();
                            incidentInfo.put("Address", RegisterActivity.address);
                            incidentInfo.put("Latitude", latitude);
                            incidentInfo.put("Longitude", longitude);
                            incidentInfo.put("Reporter", RegisterActivity.first_name + " " + RegisterActivity.last_name);
                            incidentInfo.put("ReporterContact", RegisterActivity.phoneNumber);
                            incidentInfo.put("Status", "Active");
                            incidentInfo.put("AlertLevel", "1");
                            if (radIn.isChecked())
                            {
                                incidentInfo.put("Identifier", "In");
                            }
                            else
                            {
                                incidentInfo.put("Identifier", "Near");
                            }
                            incidentInfo.put("Date", date);

                            databaseReference.child(RegisterActivity.phoneNumber).setValue(incidentInfo);

                            Intent intent1 = new Intent(ReportFireActivity.this, TruckTracker.class);
                            startActivity(intent1);
                        }
                    }
                }
        );
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null)
        {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(longi);
        }
        else
        {
            Toast.makeText(this, "Unable to find your location!", Toast.LENGTH_SHORT).show();
        }

    }

}