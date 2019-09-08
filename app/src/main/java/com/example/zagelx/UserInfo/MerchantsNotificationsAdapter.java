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
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.OrdersPackage.OrderDetails;
import com.example.zagelx.OrdersPackage.OrdersRequestsActivity;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MerchantsNotificationsAdapter extends ArrayAdapter<MerchantsNotifications> {

    Context context;
    private String purpose;

    public MerchantsNotificationsAdapter(Context context, int resource, List<MerchantsNotifications> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public MerchantsNotifications getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MerchantsNotifications currentNotification = getItem(position);
        purpose = currentNotification.getPurpose();

        if (convertView == null|| !purpose.equals( convertView.getTag())) {
            if(purpose.equals("acceptance")){
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_acceptance_to_merchant
                        , parent, false);
            }else{
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_on_order
                        , parent, false);
            }


            convertView.setTag(purpose);
        }


        View listItemView = convertView;


        CircleImageView delegateImageIV = convertView.findViewById(R.id.delegate_image);
        TextView NotificationBodyTV = convertView.findViewById(R.id.notification_body);
        ImageButton verificationIcon = convertView.findViewById(R.id.user_verification);

        String DelegateName = currentNotification.getRequestInfo().getUserName();
        String notificationBody = NotificationBodyTV.getText().toString();

        if(purpose.equals("request")){
            notificationBody = notificationBody.replace("Name", Html.fromHtml("<b>"+DelegateName+"</b>"));
            notificationBody = notificationBody.replace("Order"
                    ,Html.fromHtml("<b>"+currentNotification.getOrderName()+"</b>"));
            notificationBody = notificationBody.replace("Offer"
                    ,Html.fromHtml("<b>"+currentNotification.getRequestInfo().getOfferPrice()+"</b>"));
        }else{

            notificationBody = notificationBody.replace("Name", Html.fromHtml("<b>"+DelegateName+"</b>"));
            notificationBody = notificationBody.replace("Order"
                    ,Html.fromHtml("<b>"+currentNotification.getOrderName()+"</b>"));
            notificationBody = notificationBody.replace("Offer"
                    ,Html.fromHtml("<b>"+currentNotification.getRequestInfo().getOfferPrice()+"</b>"));
            notificationBody = notificationBody.replace("TripDate"
                    ,Html.fromHtml("<b>"+currentNotification.getTripDate()+"</b>"));

        }




        if (!currentNotification.getRequestInfo().isVerified())
            verificationIcon.setVisibility(View.GONE);


        Glide.with(delegateImageIV.getContext())
                .load(currentNotification.getRequestInfo().getUserImageURL())
                .into(delegateImageIV);
        NotificationBodyTV.setText(notificationBody);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentNotification.getPurpose().equals("request")){
                    Intent i = new Intent(context, OrdersRequestsActivity.class);
                    i.putExtra("currentNotification", currentNotification);
                    context.startActivity(i);
                }else{

//                    Intent i = new Intent(context, OrderDetails.class);
//                    i.putExtra("Package_ID", currentNotification);
//                    i.putExtra("Purpose", currentNotification.getPurpose());
//                    context.startActivity(i);

                    Intent i = new Intent(context, OrderDetails.class);
                    i.putExtra("orderId", currentNotification.getOrderId());
                    i.putExtra("MerchantNotification", currentNotification);
                    i.putExtra("Purpose", currentNotification.getPurpose());
                    i.putExtra("WhichActivity", "MNotificationsAdapter");
                    context.startActivity(i);
                }

            }
        });

        return convertView;
    }
}