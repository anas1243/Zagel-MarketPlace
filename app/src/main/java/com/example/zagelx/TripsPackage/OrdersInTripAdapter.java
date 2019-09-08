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
import com.example.zagelx.Models.OrdersInTrip;
import com.example.zagelx.Models.Trips;
import com.example.zagelx.OrdersPackage.OrderDetails;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersInTripAdapter extends ArrayAdapter<OrdersInTrip> {
    Context context;
    public OrdersInTripAdapter(Context context, int resource, List<OrdersInTrip> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public OrdersInTrip getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.orders_in_trip_item
                    , parent, false);
        }
        final OrdersInTrip currentOrderInTrip = getItem(position);

        View listItemView = convertView;

        CircleImageView orderImageIV = convertView.findViewById(R.id.order_image);
        TextView orderNameTV = convertView.findViewById(R.id.order_name);

        Glide.with(orderImageIV.getContext())
                .load(currentOrderInTrip.getOrderURL())
                .into(orderImageIV);
        orderNameTV.setText(currentOrderInTrip.getOrderName());


        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, OrderDetails.class);
                Log.e("zero one test test", "onClick: "+ currentOrderInTrip.getOrderName() );
                i.putExtra("orderId", currentOrderInTrip.getOrderId());
                i.putExtra("WhichActivity", "OrdersInTripAdapter");
                context.startActivity(i);
            }
        });


        return convertView;
    }
}
