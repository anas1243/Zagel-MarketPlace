package com.example.zagelx.UserInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.OrdersPackage.OrderDetails;
import com.example.zagelx.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DelegatesNotificationsAdapter extends ArrayAdapter<DelegatesNotification> {

    Context context;
    private String purpose;

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
        purpose = currentNotification.getPurpose();


        if (convertView == null || !purpose.equals( convertView.getTag())) {
            if(purpose.equals("Picked")){
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_picked_to_delegate
                        , parent, false);
            }else{
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.notification_item_delivered_to_delegate
                        , parent, false);
            }


            convertView.setTag(purpose);
        }

        View listItemView = convertView;


        CircleImageView delegateImageIV = convertView.findViewById(R.id.merchant_image);
        TextView NotificationBodyTV = convertView.findViewById(R.id.notification_body);
        ImageButton verificationIcon = convertView.findViewById(R.id.user_verification);

        String userName = currentNotification.getCourierInfo().getUserName();
        String notificationBody = NotificationBodyTV.getText().toString();

        switch (purpose) {
            case "Picked":

                notificationBody = notificationBody.replace("التاجرر", Html.fromHtml("<b>" + userName + "</b>"));
                notificationBody = notificationBody.replace("الشحنةة"
                        , Html.fromHtml("<b>" + currentNotification.getOrderName() + "</b>"));

                break;
            case "Delivered":

                notificationBody = notificationBody.replace("التاجرر", Html.fromHtml("<b>" + userName + "</b>"));
                notificationBody = notificationBody.replace("الشحنةة"
                        , Html.fromHtml("<b>" + currentNotification.getOrderName() + "</b>"));

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

//                Intent i = new Intent(context, OrderDetails.class);
//                i.putExtra("Package_ID", currentNotification);
//                i.putExtra("Purpose", currentNotification.getPurpose());
//                Log.e("test1st purpose", "onClick: "+ currentNotification.getPurpose());
//                context.startActivity(i);

                Intent i = new Intent(context, OrderDetails.class);
                i.putExtra("orderId", currentNotification.getOrderId());
                i.putExtra("WhichBranch", currentNotification.getWhichBranch());
                i.putExtra("WhichActivity", "DNotificationsAdapter");
                context.startActivity(i);
            }
        });

        return convertView;
    }
}