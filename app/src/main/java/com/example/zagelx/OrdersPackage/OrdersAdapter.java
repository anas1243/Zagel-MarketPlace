package com.example.zagelx.OrdersPackage;

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
import com.example.zagelx.Models.Orders;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends ArrayAdapter<Orders> {
    Context context ;
    public OrdersAdapter(Context context, int resource, List<Orders> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public Orders getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.order_item
                    , parent, false);
        }

        View listItemView = convertView;

        CircleImageView userImageIV = convertView.findViewById(R.id.user_image);
        TextView userNameTV = convertView.findViewById(R.id.user_name);

        CircleImageView packageImageIV = convertView.findViewById(R.id.package_image);
        TextView packageNameTV = convertView.findViewById(R.id.package_name);
        TextView deliveryDateTV = convertView.findViewById(R.id.delivery_date);
        TextView deliveryPriceTV = convertView.findViewById(R.id.delivery_price);

        TextView sourceTV = convertView.findViewById(R.id.source_txt_view);
        TextView destinationTV = convertView.findViewById(R.id.destination_txt_view);
        ImageView vehicleImageIV = convertView.findViewById(R.id.vehicle_image);
        ImageView verificationIcon = convertView.findViewById(R.id.verification_icon);


        final Orders currentOrder = getItem(position);
        BirthDate oDate = currentOrder.getDeliveryDate();


        Glide.with(userImageIV.getContext())
                .load(currentOrder.getMerchantImageURL())
                .into(userImageIV);
        userNameTV.setText(currentOrder.getMerchantName());

        Glide.with(packageImageIV.getContext())
                .load(currentOrder.getPackageImageURL())
                .into(packageImageIV);
        if (!currentOrder.isVerifiedUser())
        verificationIcon.setVisibility(View.GONE);

        Log.e("OrdersAdapter", "getView: " + currentOrder.getPackageImageURL());
        packageNameTV.setText(currentOrder.getPackageName());
        final String orderDate = oDate.getYear() + "-" + oDate.getMonth() + "-" + oDate.getDay();
        deliveryDateTV.setText(orderDate);
        String price = currentOrder.getDeliveryPrice() + " egp";
        deliveryPriceTV.setText(price);

        sourceTV.setText(currentOrder.getLocationInfoForPackage().getsAdminArea());
        destinationTV.setText(currentOrder.getLocationInfoForPackage().getdAdminArea());
        switch (currentOrder.getVehicle()) {
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

                Intent i = new Intent(context, OrderDetails.class);
                Log.e("zero one test test", "onClick: "+ currentOrder.isPrePaid() );
                i.putExtra("Package_ID", currentOrder);
                context.startActivity(i);
            }
        });


        return convertView;
    }
}
