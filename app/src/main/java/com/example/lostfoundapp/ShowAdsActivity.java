package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShowAdsActivity extends AppCompatActivity {

    private ListView listViewAds;
    private AdvertisementAdapter adapter;
    private List<Advertisement> advertisements;
    private Button btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);

        listViewAds = findViewById(R.id.listViewAds);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);

        listViewAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowAdsActivity.this, AdDetailsActivity.class);
                intent.putExtra("AD_ID", advertisements.get(position).getId());
                startActivity(intent);
            }
        });

        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAdsActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        loadData();
    }

    private void loadData() {
        CRUD crud = new CRUD(this);
        crud.open();
        advertisements = crud.getAllAdvertisements();
        crud.close();

        adapter = new AdvertisementAdapter(this, advertisements);
        listViewAds.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        CRUD crud = new CRUD(this);
        crud.open();
        advertisements = crud.getAllAdvertisements();
        crud.close();

        adapter.clear();
        adapter.addAll(advertisements);
        adapter.notifyDataSetChanged();
    }
}
