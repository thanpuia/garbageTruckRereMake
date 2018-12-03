package com.example.lalthanpuiachhangte.garbagetruckreremake;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView latTV,longTV;
    LocationManager locationManager;

    String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        latTV = findViewById(R.id.latitudeTextView);
        longTV = findViewById(R.id.longitudeTextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        DatabaseReference latRef = database.getReference("latitude");
        DatabaseReference longRef = database.getReference("longitude");

        myRef.setValue("Hello, World!");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



      /*  myRef.addValueEventListener(new ValueEventListener() {
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

        longRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                longTV.setText("Longitude: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




    }

}
