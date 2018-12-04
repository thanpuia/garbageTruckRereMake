package com.example.lalthanpuiachhangte.garbagetruckreremake;

/*

 ***
 ***         THIS ACTIVITY IS
 ***              FOR THE
 ***            TRUCK DRIVE
 ***

 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView latTV, longTV;
    LocationManager locationManager;
    public DatabaseReference latRef, longRef;
    public Spinner minuteSpinner, secondSpinner;

    Button liveButton;
    Boolean liveButtonStatus = false;

    public static int minTime;
    public int second_user,minute_user;

    public LocationListener locationListener;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        latTV = findViewById(R.id.latitudeTextView);
        longTV = findViewById(R.id.longitudeTextView);

        minuteSpinner = findViewById(R.id.minuteSpinner);
        secondSpinner = findViewById(R.id.secondSpinner);

        liveButton = findViewById(R.id.liveButton);
        ArrayAdapter<CharSequence> adapterSecond =
                ArrayAdapter.createFromResource(this,R.array.second,android.R.layout.simple_dropdown_item_1line);
        ArrayAdapter<CharSequence> adapterMinute =
                ArrayAdapter.createFromResource(this,R.array.minute,android.R.layout.simple_dropdown_item_1line);

        secondSpinner.setAdapter(adapterSecond);
        minuteSpinner.setAdapter(adapterMinute);

        //THIS WILL TAKE THE SECOND/S FROM THE USER
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                second_user = spinnerSecondPositionChecker(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }});


        //THIS WILL TAKE THE MINUTE/S FROM THE USER
        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minute_user = spinnerMinutePositionChecker(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }});


        //converting secs to millSecs (sec * 1000)
        //converting minute to millSecs (minute * 60 * 1000)


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        latRef = database.getReference("latitude");
        longRef = database.getReference("longitude");

        myRef.setValue("Fighting!");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

 /*       //THIS LISTEN TO ANY CHANGEs IN THE GPS COORDINATES
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latRef.setValue(location.getLatitude());
                longRef.setValue(location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 5, locationListener);
*/
        //THIS IS NOT VERY IMPORTANT
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
        //        Log.d(TAG, "Value is: " + value);
                textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //THIS LISTEN TO THE VALUE CHANGE FROM THE DATABASE WHICH IS BEING UPDATED BY THE TRUCK DRIVER
        latRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                latTV.setText("Latitude: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //THIS LISTEN TO THE VALUE CHANGE FROM THE DATABASE WHICH IS BEING UPDATED BY THE TRUCK DRIVER
        longRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                longTV.setText("Longitude: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void BackClick(View view) {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    public int spinnerSecondPositionChecker(int userSelect){
        int [] seconds = new int[]{0,5,10,20,35,48,55};
        return seconds[userSelect];
    }

    public int spinnerMinutePositionChecker(int userSelect){
        int [] minute = new int[]{0,1,2,3,4,5,7,10,15,20,30,60};
        return minute[userSelect] ;
    }

    public void liveButtonClick(View view) {

        minTime = second_user * 1000 + minute_user * 60 * 1000;

        if (liveButtonStatus){
            liveButton.setTextColor(R.color.colorGrey);
            liveButtonStatus = false;
            Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
            liveButton.setText("Status: Off");
            locationManager.removeUpdates(locationListener
            );
        }
        else{
            liveButton.setTextColor(R.color.colorAccent);
            liveButtonStatus = true;
            Toast.makeText(this,"true",Toast.LENGTH_SHORT).show();
            liveButton.setText("Status: On");
            listenForLocation();
        }
    }

    private void listenForLocation() {
        //THIS LISTEN TO ANY CHANGEs IN THE GPS COORDINATES
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latRef.setValue(location.getLatitude());
                longRef.setValue(location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 5, locationListener);

    }
}

