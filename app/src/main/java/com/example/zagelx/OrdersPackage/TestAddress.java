package com.example.zagelx.OrdersPackage;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.zagelx.R;

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
