package com.example.zagelx.OrdersPackage;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.RequestInfo;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TestAddress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tes_adress);

        List<Address> addresses = getAddressListFromLatLong(31.20636670000001,29.966046800000004);

        Log.e("testttttttttttt", "onCreate: "+ addresses );


    }

    public List<Address> getAddressListFromLatLong(double lat, double lng) {

        Geocoder geocoder = new Geocoder(this);

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(lat, lng, 20);

            // 20 is no of address you want to fetch near by the given lat-long

            for (Address address : list) {
                Log.e("testAddress", "getAddressListFromLatLong: " + address);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return list;
    }
}