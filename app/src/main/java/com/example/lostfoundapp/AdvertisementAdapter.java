package com.example.lostfoundapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdvertisementAdapter extends ArrayAdapter<Advertisement> {
    public AdvertisementAdapter(Context context, List<Advertisement> advertisements) {
        super(context, 0, advertisements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Advertisement ad = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ad, parent, false);
        }
        // Lookup view for data population
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvType = convertView.findViewById(R.id.tvType);
        // Populate the data into the template view using the data object
        tvDate.setText(ad.getDate());
        tvDescription.setText(ad.getDescription().substring(0, Math.min(ad.getDescription().length(), 50)) + "...");
        tvType.setText(ad.getType());
        // Return the completed view to render on screen
        return convertView;
    }
}