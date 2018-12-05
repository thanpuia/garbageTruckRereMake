package com.example.lalthanpuiachhangte.garbagetruckreremake;

/*

***
***         THIS ACTIVITY IS
***           FOR THE USER
***

 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.annotation.NonNull;

import android.util.Log;

import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap, lastKnowLocationOnGoogleMap;
    TextView textView;
    TextView latTV, longTV;
    public DatabaseReference latRef, longRef;
    public double newLat = 0;
    public double newLong = 0;

    public double latValue, longValue;

    String TAG = "TAG";

    public LocationManager locationManager;

    public boolean countMarker = true;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        latTV = findViewById(R.id.latitudeTextView);
        longTV = findViewById(R.id.longitudeTextView);

        //CREATING FIREBASE REFERENCE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("truck-1/message");

        //CREATING UNIQUE REFERENCE FOR LAT AND LONG
        // latRef = database.getReference("truck-1/latitude");
        // longRef = database.getReference("truck-1/longitude");

       // CREATE LISTENER FOR LATITUDE
        latRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latValue = dataSnapshot.getValue(double.class);
                latTV.setText("Latitude: "+latValue);

                newLat = latValue;
                if(newLong != 0) {
                    setMap();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //CREATE LISTENER FOR LONGITUDE
        longRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                longValue = dataSnapshot.getValue(double.class);
                longTV.setText("Longitude: "+longValue);

                newLong= longValue;
                if(newLat != 0) {
                    setMap();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public MarkerOptions a;
    public Marker m1,m2;

    public void setMap() {

       // Marker marker;

     //   LatLng sydney = new LatLng(locat.getLatitude(), location.getLongitude());

        LatLng sydney = new LatLng (newLat, newLong);
       // LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
         a = new MarkerOptions().position(new LatLng(newLat, newLong));
         m1 = mMap.addMarker(a);

       // countMarker = false;

        //mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    //ZOOM THE CAMERA VIEW   22: we can see only one street
                    //      18: we can see around 3 streets
        //                  16: this is very clear but the visible area is too less
        //                  15.5: we can see one locality and its neighbour  THIS IS GOOD FOR NOW!!
        //                  12 : we can see whole aizawl city and its surroundings
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(newLat, newLong), 7.0f));

        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(sydney));

        //GET LAST KNOWN LOCATION AND UPDATE THE FIREBASE
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location2 = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        // Add a marker in Sydney and move the camera
        LatLng lastKnowLocation = new LatLng(location2.getLatitude(), location2.getLongitude());
        lastKnowLocationOnGoogleMap.addMarker(new MarkerOptions().position(lastKnowLocation).title("You Are Here"));

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lastKnowLocationOnGoogleMap = googleMap;
           //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void GoToDrivePageClick(View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    public void lastKnownLocationClick(View view) {

        setMap();

    }
}
