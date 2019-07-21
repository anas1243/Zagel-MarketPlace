package com.example.zagelx.UserInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.OrdersPackage.OrderDetails;
import com.example.zagelx.OrdersPackage.OrdersRequestsActivity;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DelegatesNotificationsAdapter extends ArrayAdapter<DelegatesNotification> {

    Context context;

    public DelegatesNotificationsAdapter(Context context, int resource, List<DelegatesNotification> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public DelegatesNotification getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final DelegatesNotification currentNotification = getItem(position);

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_on_trip
                    , parent, false);
        }

        View listItemView = convertView;


        CircleImageView delegateImageIV = convertView.findViewById(R.id.merchant_image);
        TextView NotificationBodyTV = convertView.findViewById(R.id.notification_body);
        ImageButton verificationIcon = convertView.findViewById(R.id.user_verification);




        String DelegateName = currentNotification.getRequestInfo().getUserName();

        String notificationBody = NotificationBodyTV.getText().toString();
        notificationBody = notificationBody.replace("Name", Html.fromHtml("<b>"+DelegateName+"</b>"));
        notificationBody = notificationBody.replace("Order"
                ,Html.fromHtml("<b>"+currentNotification.getOrderName()+"</b>"));
        notificationBody = notificationBody.replace("Offer"
                ,Html.fromHtml("<b>"+currentNotification.getRequestInfo().getOfferPrice()+"</b>"));
        notificationBody = notificationBody.replace("TripDate"
                ,Html.fromHtml("<b>"+currentNotification.getTripDate()+"</b>"));
        if (!currentNotification.getRequestInfo().isVerified())
            verificationIcon.setVisibility(View.GONE);


        Glide.with(delegateImageIV.getContext())
                .load(currentNotification.getRequestInfo().getUserImageURL())
                .into(delegateImageIV);
        NotificationBodyTV.setText(notificationBody);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, OrderDetails.class);
                i.putExtra("Package_ID", currentNotification);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}