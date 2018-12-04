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

    public GoogleMap mMap;
    TextView textView;
    TextView latTV, longTV;
    public DatabaseReference latRef, longRef;
    public double newLat = 0;
    public double newLong = 0;

    public double latValue, longValue;

    String TAG = "TAG";

    public LocationManager locationManager;

    public boolean countMarker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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

       // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    /*    final double longitude = location.getLongitude();
        double latitude = location.getLatitude();//*/
        latTV = findViewById(R.id.latitudeTextView);
        longTV = findViewById(R.id.longitudeTextView);
//
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        //THIS IS PERMISSION CHECK FOR LAST KNOWN LOCATION. NO NEED TO WORRY . AUTO GENERATE
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//
//        //CREATING FIREBASE REFERENCE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
//
//        //CREATING UNIQUE REFERENCE FOR LAT AND LONG
         latRef = database.getReference("latitude");
         longRef = database.getReference("longitude");
//
//
//        //SET VALUE FOR LATITUTDE AND LONGITUDE
//        latRef.setValue(latitude);
//        longRef.setValue(longitude);
//
//        //SET VALUE FOR MESSAGE
//        myRef.setValue("Hello, World!");
//
//        //CREATE LISTENER FOR MESSAGE
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                //        Log.d(TAG, "Value is: " + value);
//                //textView.setText(value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//        //CREATE LISTENER FOR LATITUDE
//        latRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                latValue = dataSnapshot.getValue(int.class);
//                latTV.setText("Latitude: "+latValue);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        //CREATE LISTENER FOR LONGITUDE
//        longRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                longValue = dataSnapshot.getValue(int.class);
//                longTV.setText("Longitude: "+longValue);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        //UPDATING THE MARKER POSITION
//
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                //marker.remove();
//
//                /*LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//*/
//                List<String> providerList = locationManager.getAllProviders();
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                String lol="";
//                try {
//                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    if(null!=listAddresses&&listAddresses.size()>0){
//                         lol = listAddresses.get(0).getAddressLine(0);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                if(!lol.equals("")) latTV.setText("Latitude: "+lol);
//
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 800, 1, locationListener);

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
    public Marker m;


    public void setMap() {

       // Marker marker;

     //   LatLng sydney = new LatLng(locat.getLatitude(), location.getLongitude());

        LatLng sydney = new LatLng (newLat, newLong);
       // LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
         a = new MarkerOptions().position(new LatLng(newLat, newLong));
         m = mMap.addMarker(a);

       // countMarker = false;

        //mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       // m.remove();
        //m.isDraggable();
        mMap.clear();

       // m.setPosition(sydney);
        mMap.addMarker(new MarkerOptions().position(sydney));
       // m.setPosition(sydney);


//        MarkerOptions a = new MarkerOptions().position(new LatLng(newLat, newLong));
//        Marker m = mMap.addMarker(a);
//        m.setPosition(new LatLng(newLat, newLong));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        m.remove();
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
       mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }

    public void GoToDrivePageClick(View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
