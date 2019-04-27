package com.example.zagelx.OrdersPackage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Authentication.MainActivity;
import com.example.zagelx.Models.BirthDate;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends ArrayAdapter<Orders> {
    public OrdersAdapter(Context context, int resource, List<Orders> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.order_item, parent, false);
        }


        CircleImageView userImageIV = convertView.findViewById(R.id.user_image);
        TextView userNameTV = convertView.findViewById(R.id.user_name);

        CircleImageView packageImageIV = convertView.findViewById(R.id.package_image);
        TextView packageNameTV = convertView.findViewById(R.id.package_name);
        TextView deliveryDateTV = convertView.findViewById(R.id.delivery_date);
        TextView deliveryPriceTV = convertView.findViewById(R.id.delivery_price);

        TextView sourceTV = convertView.findViewById(R.id.source_txt_view);
        TextView destinationTV = convertView.findViewById(R.id.destination_txt_view);
        ImageView vehicleImageIV = convertView.findViewById(R.id.vehicle_image);



        Orders CurrentOrder = getItem(position);
        BirthDate oDate = CurrentOrder.getDeliveryDate();


        Glide.with(userImageIV.getContext())
                .load(CurrentOrder.getMerchantImageURL())
                .into(userImageIV);
        userNameTV.setText(CurrentOrder.getMerchantName());

        Glide.with(packageImageIV.getContext())
                .load(CurrentOrder.getPackageImageURL())
                .into(packageImageIV);

        Log.e("OrdersAdapter", "getView: "+ CurrentOrder.getPackageImageURL());
        packageNameTV.setText(CurrentOrder.getPackageName());
        String orderDate = oDate.getYear()+"-"+oDate.getMonth()+"-"+ oDate.getDay();
        deliveryDateTV.setText(orderDate);
        String price = CurrentOrder.getDeliveryPrice()+" egp";
        deliveryPriceTV.setText(price);

        sourceTV.setText(CurrentOrder.getSource());
        destinationTV.setText(CurrentOrder.getDestination());
        vehicleImageIV.setImageResource(R.drawable.vehicle_car);



        return convertView;
    }
}
