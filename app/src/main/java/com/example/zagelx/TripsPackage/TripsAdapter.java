package com.example.zagelx.TripsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripsAdapter extends ArrayAdapter<Trips> {
    public TripsAdapter(Context context, int resource, List<Trips> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.trip_item, parent, false);
        }


        CircleImageView delegateImageIV = convertView.findViewById(R.id.delegate_image);
        TextView delegateNameTV = convertView.findViewById(R.id.delegate_name);
        TextView routeDateTV = convertView.findViewById(R.id.route_date);
        TextView routePriceTV = convertView.findViewById(R.id.route_price);

        TextView sourceTV = convertView.findViewById(R.id.source_txt_view);
        TextView destinationTV = convertView.findViewById(R.id.destination_txt_view);
        ImageView vehicleImageIV = convertView.findViewById(R.id.vehicle_image);

        Trips trip = getItem(position);

        Glide.with(delegateImageIV.getContext())
                .load(trip.getDelegateImage())
                .into(delegateImageIV);
        delegateNameTV.setText(trip.getDelegateID());
        routeDateTV.setText(trip.getRouteDate());
        routePriceTV.setText(trip.getRoutePrice());

        sourceTV.setText(trip.getSource());
        destinationTV.setText(trip.getDestination());
        vehicleImageIV.setImageResource(R.drawable.vehicle_car);


        return convertView;
    }
}
