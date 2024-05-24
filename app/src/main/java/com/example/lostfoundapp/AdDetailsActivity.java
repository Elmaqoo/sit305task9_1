package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdDetailsActivity extends AppCompatActivity {
    private TextView textViewType, textViewName, textViewPhone, textViewDescription, textViewDate, textViewLocation;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        textViewType = findViewById(R.id.textViewType);
        textViewName = findViewById(R.id.textViewName);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);
        btnRemove = findViewById(R.id.btnRemove);

        long adId = getIntent().getLongExtra("AD_ID", -1);
        CRUD crud = new CRUD(this);
        crud.open();
        Advertisement ad = crud.getAdvertisement(adId);
        crud.close();

        textViewType.setText(ad.getType());
        textViewName.setText(ad.getName());
        textViewPhone.setText(ad.getPhone());
        textViewDescription.setText(ad.getDescription());
        textViewDate.setText(ad.getDate());
        if (ad.getPlaceName() != null && !ad.getPlaceName().isEmpty()) {
            textViewLocation.setText(ad.getPlaceName()); // Show place name if available
        } else {
            textViewLocation.setText(ad.getLocation()); // Otherwise, show coordinates
        }

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crud.open();
                crud.deleteAdvertisement(adId);
                crud.close();
                finish();
            }
        });
    }
}
