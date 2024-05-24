package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private List<Advertisement> advertisements;
    private Map<Marker, Advertisement> markerAdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadAdvertisements();

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // End the activity and return to the previous screen
            }
        });
    }

    private void loadAdvertisements() {
        CRUD crud = new CRUD(this);
        crud.open();
        advertisements = crud.getAllAdvertisements();
        crud.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true); // Enable zoom controls

        Log.d(TAG, "Number of advertisements: " + advertisements.size());
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidLocations = false;

        for (Advertisement ad : advertisements) {
            String location = ad.getLocation();
            Log.d(TAG, "Processing ad: " + ad.getName() + ", Location: " + location);
            if (location != null && !location.isEmpty()) {
                String[] locationParts = location.split(",");
                if (locationParts.length == 2) {
                    try {
                        double lat = Double.parseDouble(locationParts[0].trim());
                        double lng = Double.parseDouble(locationParts[1].trim());
                        LatLng latLng = new LatLng(lat, lng);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(ad.getName()));
                        markerAdMap.put(marker, ad); // Associate marker with ad
                        boundsBuilder.include(latLng);
                        hasValidLocations = true;
                        Log.d(TAG, "Marker added for " + ad.getName() + " at location " + latLng);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid location format for ad: " + ad.getName(), e);
                    }
                } else {
                    Log.e(TAG, "Location format is incorrect for ad: " + ad.getName() + ", Location: " + location);
                }
            }
        }

        if (hasValidLocations) {
            LatLngBounds bounds = boundsBuilder.build();
            int padding = 100; // offset from edges of the map in pixels
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            Log.d(TAG, "Camera moved to bounds: " + bounds);
        } else {
            Log.d(TAG, "No valid locations found to set camera bounds.");
        }

        // Set marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Advertisement ad = markerAdMap.get(marker);
                if (ad != null) {
                    Intent intent = new Intent(MapActivity.this, AdDetailsActivity.class);
                    intent.putExtra("AD_ID", ad.getId());
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
