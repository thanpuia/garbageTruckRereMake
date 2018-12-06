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
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap, lastKnowLocationOnGoogleMap;
    TextView textView;
    TextView latTV, longTV;

//    public long truckLat = 0;
//    public long truckLng = 0;

    public long truckLat = 0;
    public long truckLng = 0;

    public MarkerOptions a;
    public Marker m1,m2;

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

        //CHECKING PERMISSION FOR LOCATION MANAGER TO ANDROID FOR SECURITY ISSUE
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
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("trucks/truck-1/");

        //THIS TRIGGERS AS SOON AS A NEW CHILD IS ADDED
        myRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("TAG/kl",""+dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //GET THE LATEST LATLNG FROM FIREBASE
               // if(truckLng != 0 && truckLat != 0){

//                    truckLat = (long) dataSnapshot.child("latitude").getValue();
//                    truckLng = (long) dataSnapshot.child("longitude").getValue();
                truckLat = (long) dataSnapshot.child("latitude").getValue();
                truckLng = (long) dataSnapshot.child("longitude").getValue();

                    setMap(truckLat, truckLng);

               // }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)      { }
            @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)                          { }
            @Override public void onChildMoved  (@NonNull DataSnapshot dataSnapshot, @Nullable String s)      { }
            @Override public void onCancelled   (@NonNull DatabaseError databaseError)                        { }
        });


    }

    public void setMap(long lat1, long lng1) {

        LatLng sydney = new LatLng (lat1, lng1);
        a = new MarkerOptions().position(new LatLng(lat1, lng1));
        m1 = mMap.addMarker(a);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    /*                             ZOOM THE CAMERA VIEW
    * *                          ---------------------------
    * *                       22     : we can see only one street
    * *                       18     : we can see around 3 streets
    * *                       16     : this is very clear but the visible area is too less
    * *                       15.5   : we can see one locality and its neighbour  THIS IS GOOD FOR NOW!!
    * *                       12     : we can see whole aizawl city and its surroundings
    */

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(lat1, lng1), 7.0f));
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
        LocationManager locationManager1 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location location2 = locationManager1.getLastKnownLocation(locationManager.GPS_PROVIDER);

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

  /*  public void lastKnownLocationClick(View view) {

       // setMap();

    }*/
}
