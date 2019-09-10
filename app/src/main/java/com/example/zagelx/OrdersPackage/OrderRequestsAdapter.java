package com.example.zagelx.OrdersPackage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderRequestsAdapter extends ArrayAdapter<RequestInfo> {
    private Context context ;
    private String currentOrderName;
    private DatabaseReference mNotificationDatabaseReference, mRequestsDatabaseReference, mOrdersDatabaseReference;
    private DelegatesNotification delegatesNotification;
    private Orders currentOrder;
    private Users currentUser;
    private String delegateId, notificationId, requestId;

    public OrderRequestsAdapter(Context context, int resource, List<RequestInfo> objects
            , DatabaseReference mOrdersDatabaseReference, Orders currentOrder, Users currentUser) {
        super(context, resource, objects);
        this.context = context;
        this.mOrdersDatabaseReference = mOrdersDatabaseReference;
        this.currentOrder = currentOrder;
        this.currentUser = currentUser;
    }


    @Override
    public RequestInfo getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.request_item
                    , parent, false);

        }

        View listItemView = convertView;


        CircleImageView delegateImageIV = convertView.findViewById(R.id.delegate_image);
        ImageView delegateVerificationIcon = convertView.findViewById(R.id.verification_icon);
        SimpleRatingBar delegateRating = convertView.findViewById(R.id.rb_rating_profile);
        TextView delegateNameTV = convertView.findViewById(R.id.user_name);
        TextView deliveryFeesTV = convertView.findViewById(R.id.delivery_offer);
        currentOrderName = currentOrder.getPackageName();



        final RequestInfo currentRequestInfo = getItem(position);



        Button acceptButton = convertView.findViewById(R.id.accept_requests);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test button pos.", "onClick: "+ currentRequestInfo.getOfferPrice() );

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Accept Delivery Request Confirmation!");
                alertDialog.setMessage("Are you sure you want "+currentRequestInfo.getUserName()
                        +" to deliver "+currentOrderName+" for "+ currentRequestInfo.getOfferPrice()+" EGP");
                alertDialog.setIcon(R.drawable.ic_delegates_requested);

                alertDialog.setPositiveButton("Yes I agree",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

                                delegateId = currentRequestInfo.getUserID();
                                notificationId = System.currentTimeMillis() + delegateId;
                                requestId = System.currentTimeMillis() + currentOrder.getMerchantId();

                                mNotificationDatabaseReference = mFirebaseDatabase.getReference("Users/"
                                        + delegateId + "/Notifications/" + notificationId);


                                delegatesNotification = new DelegatesNotification(
                                        notificationId,"toDelegate", "acceptance", delegateId, "", "", currentOrder.getOrderId()
                                        , currentOrderName, new RequestInfo(requestId, currentOrder.getMerchantId()
                                        , currentUser.getName(), currentUser.getProfilePictureURL(), currentUser.getMobileNumber(), currentUser.getRate()
                                        , currentRequestInfo.getOfferPrice(), currentUser.isVerified(), "accepted" )
                                );

                                mRequestsDatabaseReference = mOrdersDatabaseReference.child("currentRequestInfo");
                                mRequestsDatabaseReference.child(currentRequestInfo.getRequestId()).child("status").setValue("accepted");

                                mNotificationDatabaseReference.setValue(delegatesNotification);

                                mOrdersDatabaseReference.child("acceptedDelegateID")
                                        .setValue(currentRequestInfo.getUserID());
                                mOrdersDatabaseReference.child("acceptedDelegateName")
                                        .setValue(currentRequestInfo.getUserName());
                                mOrdersDatabaseReference.child("acceptedDelegateMobile")
                                        .setValue(currentRequestInfo.getUserMobile());
                                mOrdersDatabaseReference.child("packageState").setValue("Reserved");
                                mOrdersDatabaseReference.child("acceptedDeliveryPrice").setValue(currentRequestInfo.getOfferPrice());


//                                Intent i = new Intent(context, OrderDetails.class);
//                                i.putExtra("Package_ID", currentNotification);
//                                ((OrdersRequestsActivity)context).finish();
//                                context.startActivity(i);

                                Intent i = new Intent(context, OrderDetails.class);
                                i.putExtra("orderId", currentOrder.getOrderId());
                                i.putExtra("WhichActivity", "MOrderRequestsAdapter");
                                ((OrdersRequestsActivity)context).finish();
                                context.startActivity(i);

                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        Button refuseButton = convertView.findViewById(R.id.refuse_request);
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test button neg.", "onClick: "+ v.getId() );

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("refuse Delivery Request Confirmation!");
                alertDialog.setMessage("Are you sure you want to refuse delivering "+currentOrderName+" for "+ currentRequestInfo.getOfferPrice()+" EGP");
                alertDialog.setIcon(R.drawable.ic_delegates_requested);

                alertDialog.setPositiveButton("Yes I refuse",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                                mRequestsDatabaseReference = mOrdersDatabaseReference.child("currentRequestInfo");
                                mRequestsDatabaseReference.child(currentRequestInfo.getRequestId()).child("status").setValue("rejected");

                                Snackbar snackbar = Snackbar
                                        .make(parent.findViewById(R.id.requests_list), "مازال بامكانك قبول طلبات التوصيل الاخري", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        if (!currentRequestInfo.isVerified())
            delegateVerificationIcon.setVisibility(View.GONE);


        Glide.with(delegateImageIV.getContext())
                .load(currentRequestInfo.getUserImageURL())
                .into(delegateImageIV);

        deliveryFeesTV.setText(currentRequestInfo.getOfferPrice()+" EGP");
        delegateNameTV.setText(currentRequestInfo.getUserName());
        delegateRating.setRating(currentRequestInfo.getRating());



        return convertView;
    }
}
