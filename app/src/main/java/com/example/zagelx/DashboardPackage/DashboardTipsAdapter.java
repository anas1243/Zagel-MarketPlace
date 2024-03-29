package com.example.zagelx.DashboardPackage;

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
import com.example.zagelx.TripsPackage.TripsDetails;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardTipsAdapter extends ArrayAdapter<Trips> {

    Context context ;

    public DashboardTipsAdapter( Context context, int resource,  List<Trips> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public Trips getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.trip_item
                    , parent, false);
        }

        View listItemView = convertView;


        CircleImageView packageImageIV = convertView.findViewById(R.id.delegate_image);
        TextView packageNameTV = convertView.findViewById(R.id.user_name);
        TextView deliveryDateTV = convertView.findViewById(R.id.route_date);
        TextView deliveryPriceTV = convertView.findViewById(R.id.route_price);

        TextView sourceTV = convertView.findViewById(R.id.source_txt_view);
        TextView destinationTV = convertView.findViewById(R.id.destination_txt_view);
        ImageView vehicleImageIV = convertView.findViewById(R.id.vehicle_image);


        final Trips CurrentTrip = getItem(position);
        BirthDate oDate = CurrentTrip.getRouteDate();




        Glide.with(packageImageIV.getContext())
                .load(CurrentTrip.getDelegateImageURL())
                .into(packageImageIV);

        Log.e("OrdersAdapter", "getView: " + CurrentTrip.getDelegateImageURL());
        packageNameTV.setText(CurrentTrip.getDelegateName());
        final String orderDate = oDate.getYear() + "-" + oDate.getMonth() + "-" + oDate.getDay();
        deliveryDateTV.setText(orderDate);
        String price = CurrentTrip.getRoutePrice() + " egp";
        deliveryPriceTV.setText(price);

        sourceTV.setText(CurrentTrip.getLocationInfoForTrip().getsAdminArea());
        destinationTV.setText(CurrentTrip.getLocationInfoForTrip().getdAdminArea());
        switch (CurrentTrip.getVehicle()) {
            case "Any":
                vehicleImageIV.setImageResource(R.drawable.vehicle_any_yellow);
                break;
            case "Train":
                vehicleImageIV.setImageResource(R.drawable.vehicle_train_yellow);
                break;
            case "MotorCycle":
                vehicleImageIV.setImageResource(R.drawable.vehicle_motorcycle_yellow);
                break;
            case "Car":
                vehicleImageIV.setImageResource(R.drawable.vehicle_car_yellow);
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
                Log.e("zero one test test", "onClick: "+ CurrentTrip.isPrePaid() );
                i.putExtra("Route_ID", CurrentTrip);
                context.startActivity(i);
            }
        });



        return convertView;
    }
}

