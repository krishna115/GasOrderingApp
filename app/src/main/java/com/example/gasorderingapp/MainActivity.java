package com.example.gasorderingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    //Initialize Variable
    SupportMapFragment supportMapFragment;
    ImageButton btn;
   GoogleMap map;
    Location currentLocation;
    TextView availibility,no_of_cylinders,no_of_half_cylinders,title,address;
    double latitude,longitude;
    BottomSheetBehavior bottomSheetBehavior;
    Button place_order;
    MarkerOptions markerOptions10;
    int cylinders=0,cylinders_half=0;
    private static final int REQUEST_CODE = 101;
    FusedLocationProviderClient fusedLocationProviderClient;
    View btm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        initialize();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        btn.setBackgroundResource(R.drawable.drop_up);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        btn.setBackgroundResource(R.drawable.drop_down);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        btn.setBackgroundResource(R.drawable.drop_up);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        btn.setBackgroundResource(R.drawable.drop_down);
                        break;
                }
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(no_of_cylinders.getText().toString()) > 0 || Integer.parseInt(no_of_half_cylinders.getText().toString())>0) {
                    Intent intent = new Intent(MainActivity.this, userDetail.class);
                    intent.putExtra("half_cylinders",cylinders_half);
                    intent.putExtra("cylinders",cylinders);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No cylinders available at this moment!!!", Toast.LENGTH_LONG).show();
                }
            } });



    }

    private void initialize() {
        btm = findViewById(R.id.btm2);
        bottomSheetBehavior = BottomSheetBehavior.from(btm);
        btn = findViewById(R.id.btn1);
        place_order = findViewById(R.id.place_order);
        availibility = findViewById(R.id.availibility);
        no_of_cylinders = findViewById(R.id.no_of_cylinders);
        no_of_half_cylinders = findViewById(R.id.no_of_halfcylinders);
        title = findViewById(R.id.title);
        address = findViewById(R.id.address);

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    supportMapFragment.getMapAsync(MainActivity.this);

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
       MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng).title("I am here");
       googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
       googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
       googleMap.addMarker(markerOptions);
       addPlacesOnMap();
//
       map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {
               if(marker.getTitle().equals("I am here")){
                   return false;
               }
               String [] str = marker.getTitle().split(",");
               btm.setVisibility(View.VISIBLE);
               title.setText(str[0]);
               cylinders = Integer.parseInt(str[1]);
               cylinders_half = Integer.parseInt(str[2]);
               if((cylinders + cylinders_half)>0){
                   availibility.setText(getString(R.string.available));
                   availibility.setTextColor(Color.GREEN);
               }else{
                   availibility.setTextColor(Color.RED);
                   availibility.setText(getString(R.string.outOfStock));
               }
               no_of_cylinders.setText(str[1]);
               no_of_half_cylinders.setText(str[2]);
               address.setText(marker.getSnippet());
               return false;
           }
       });
    }

    private void addPlacesOnMap() {
        CreateMarkerOnMap("Nishantha Supermarket,36,20","Hatharabage Rd, Balangoda, Sri Lanka",6.6746756,80.7259067,map);
        CreateMarkerOnMap("Golden Hardware,24,18","Mamalgaha junction,Amupitiya,Balangoda, Sri Lanka",6.67839,80.73674,map);
        CreateMarkerOnMap("Prethika Stores,0,0","Kahatapitiya Rd, Balangoda, Sri Lanka",6.6439855,80.7341122,map);
        CreateMarkerOnMap("Indrasiri Stores,6,0","Ratmalawinna, Sri Lanka",6.6751865,80.727067,map);
        CreateMarkerOnMap("Jayabima Supermarket,31,39","Aluthnuwara Dewalaya Rd, Aluthnuwara, Sri Lanka",6.6861457,80.7463852,map);
        CreateMarkerOnMap("Sanmack Hardware,11,17","Ratmalawinna, Sri Lanka",6.6745366,80.7242854,map);
    }

    private void CreateMarkerOnMap(String place_name,String address, double latitude, double longitude,GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude,longitude);
        markerOptions10 = new MarkerOptions().position(latLng).title(place_name).snippet(address);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        googleMap.addMarker(markerOptions10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

}

