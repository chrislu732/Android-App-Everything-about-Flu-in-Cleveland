package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MySQL.MySQLiteHelper;
import com.example.broadcastreceiver.GeofenceBroadReceiver;
import com.example.service.MyService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class running extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mMapFragment;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private Location mLastLocation;
    private Context mContext;
    private LocationRequest mLocationRequest;
    private ArrayList<Geofence> geofenceList = new ArrayList<>();
    private PendingIntent geofencePendingIntent = null;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView textView1,textView2;
    Marker mCurrLocationMarker;
    private GeofencingClient geofencingClient;
    private LocalBroadcastManager lbm;
    private Button stopButton;
    private SQLiteDatabase db;
    MySQLiteHelper helper;
    private int count = 0;
    int walk_step = 0;
    int run_step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        stopButton = findViewById(R.id.stopbutton);
        helper = new MySQLiteHelper(this,"diary",null,1);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = helper.getWritableDatabase();
                Insert();
            }
        });
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.walk_step")){
                String word = intent.getStringExtra("walking");
                textView1.setText("walking steps:  "+word);
                walk_step = Integer.valueOf(word);
            }else if(intent.getAction().equals("com.example.run_step")){
                String word = intent.getStringExtra("running");
                textView2.setText("running steps:  "+word);
                run_step = Integer.valueOf(word);
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent stop = new Intent(this,MyService.class);
        stopService(stop);
        if(mFusedLocationClient!=null){
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceList.add(new Geofence.Builder()
                .setRequestId("6")
                .setCircularRegion(41.507246,-81.590589,150)
                .setExpirationDuration(10000000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT|Geofence.GEOFENCE_TRANSITION_ENTER)
                .build());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(12000);
        mLocationRequest.setFastestInterval(6000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        map = googleMap;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            map.setMyLocationEnabled(true);
        } else {
            checkLocationPermission();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(41.507246,-81.590589))
                .radius(150)
                .fillColor(Color.CYAN)
                .strokeColor(Color.BLACK)
                .strokeWidth(20);
        addGeofence();
        map.addCircle(circleOptions);
        startService(new Intent(this, MyService.class));
        lbm = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();{
            filter.addAction("com.example.walk_step");
            filter.addAction("com.example.run_step");
        }
        lbm.registerReceiver(mMessageReceiver,filter);
    }

    private void addGeofence(){
        geofencingClient.addGeofences(getGeofencingRequest(),getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Geo", "add a geofence");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Geo","add failure");
                    }
                });
    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder= new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent(){
        if(geofencePendingIntent!=null){
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this,GeofenceBroadReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                if(++count==1){
                    mLastLocation = locationList.get(0);
                }
                Location location = locationList.get(locationList.size() - 1);
                Log.i("ABC",String.valueOf(location.getLatitude())+String.valueOf(location.getLongitude()));
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                // calculate duration,distance and speed
                // send message
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Intent intent = new Intent("com.example.location");
                intent.putExtra("com.example.location",new double[]{location.getLatitude(),location.getLongitude()});
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = map.addMarker(markerOptions);
                PolylineOptions polylineOptions = new PolylineOptions().width(50).color(Color.BLUE);
                polylineOptions.add(latLng);
                polylineOptions.add(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                map.addPolyline(polylineOptions);
                mLastLocation = location;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(running.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        map.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void Insert(){
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        values.put("title",String.valueOf(day));
        values.put("content", String.valueOf(walk_step));
        db.insertWithOnConflict("diary", null, values,SQLiteDatabase.CONFLICT_REPLACE);
    }
}
