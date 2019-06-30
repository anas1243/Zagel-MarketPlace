package com.example.zagelx.TripsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripsAdapter extends ArrayAdapter<Trips> {
    Context context;
    public TripsAdapter(Context context, int resource, List<Trips> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public Trips getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.trip_item
                    , parent, false);
        }

        View listItemView = convertView;

        CircleImageView delegateImageIV = convertView.findViewById(R.id.delegate_image);
        TextView delegateNameTV = convertView.findViewById(R.id.delegate_name);
        TextView routeDateTV = convertView.findViewById(R.id.route_date);
        TextView routePriceTV = convertView.findViewById(R.id.route_price);

        TextView sourceTV = convertView.findViewById(R.id.source_txt_view);
        TextView destinationTV = convertView.findViewById(R.id.destination_txt_view);
        ImageView vehicleImageIV = convertView.findViewById(R.id.vehicle_image);

        final Trips currentTrip = getItem(position);
        BirthDate rDate = currentTrip.getRouteDate();

        String tripDate = rDate.getYear() + "-" + rDate.getMonth() + "-" + rDate.getDay();

        Glide.with(delegateImageIV.getContext())
                .load(currentTrip.getDelegateImageURL())
                .into(delegateImageIV);
        delegateNameTV.setText(currentTrip.getDelegateName());
        routeDateTV.setText(tripDate);
        String price = currentTrip.getRoutePrice() + " egp";
        routePriceTV.setText(price);

        sourceTV.setText(currentTrip.getCurrentOrderLocationInfo().getsAdminArea());
        destinationTV.setText(currentTrip.getCurrentOrderLocationInfo().getdAdminArea());

        switch (currentTrip.getVehicle()) {
            case "Car":
                vehicleImageIV.setImageResource(R.drawable.vehicle_car_yellow);
                break;
            case "Train":
                vehicleImageIV.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                vehicleImageIV.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Metro":
                vehicleImageIV.setImageResource(R.drawable.vehicle_metro_yellow);
                break;
            case "Nos Na2l":
                vehicleImageIV.setImageResource(R.drawable.vehicle_nos_na2l_yellow);
                break;
            case "Bus":
                vehicleImageIV.setImageResource(R.drawable.vehicle_bus_yellow);
                break;
        }

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, TripsDetails.class);
                Log.e("zero one test test", "onClick: "+ currentTrip.isPrePaid() );
                i.putExtra("Route_ID", currentTrip);
                context.startActivity(i);
            }
        });


        return convertView;
    }
}
