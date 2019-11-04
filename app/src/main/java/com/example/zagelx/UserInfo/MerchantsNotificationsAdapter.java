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
            if(purpose.equals("OnWay")){
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_onway_tomerchant
                        , parent, false);
            }else if (purpose.equals("Picked") || purpose.equals("Delivered")){
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_pickedanddelivered_tomerchant
                        , parent, false);
            }


            convertView.setTag(purpose);
        }


        View listItemView = convertView;


        CircleImageView delegateImageIV = convertView.findViewById(R.id.delegate_image);
        TextView NotificationBodyTV = convertView.findViewById(R.id.notification_body);
        ImageButton verificationIcon = convertView.findViewById(R.id.user_verification);

        String DelegateName = currentNotification.getCourierInfo().getUserName();
        String notificationBody = NotificationBodyTV.getText().toString();

        switch (purpose) {
            case "OnWay":
                notificationBody = notificationBody.replace("المندوبب", Html.fromHtml("<b>" + DelegateName + "</b>"));
                notificationBody = notificationBody.replace("الشحنة"
                        , Html.fromHtml("<b>" + currentNotification.getOrderName() + "</b>"));
                break;
            case "Picked":

                notificationBody = notificationBody.replace("المندوبب", Html.fromHtml("<b>" + DelegateName + "</b>"));
                notificationBody = notificationBody.replace("الشحنة"
                        , Html.fromHtml("<b>" + currentNotification.getOrderName() + "</b>"));

                break;
            case "Delivered":

                notificationBody = notificationBody.replace("المندوبب", Html.fromHtml("<b>" + DelegateName + "</b>"));
                notificationBody = notificationBody.replace("الشحنة"
                        , Html.fromHtml("<b>" + currentNotification.getOrderName() + "</b>"));
                notificationBody = notificationBody.replace("استلم"
                        , Html.fromHtml("<b>" + "سلم" + "</b>"));
//

                break;
        }




        if (!currentNotification.getCourierInfo().isVerified())
            verificationIcon.setVisibility(View.GONE);


        Glide.with(delegateImageIV.getContext())
                .load(currentNotification.getCourierInfo().getUserImageURL())
                .into(delegateImageIV);
        NotificationBodyTV.setText(notificationBody);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, OrderDetails.class);
                i.putExtra("orderId", currentNotification.getOrderId());
                i.putExtra("WhichBranch", currentNotification.getWhichBranch());
                i.putExtra("WhichActivity", "MNotificationsAdapter");
                context.startActivity(i);
            }
        });

        return convertView;
    }
}