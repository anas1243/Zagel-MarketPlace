package com.example.zagelx.Utilities;

import android.content.Intent;
import android.util.Log;
import com.example.zagelx.UserInfo.DashboardActivity;
import com.example.zagelx.UserInfo.ProfileActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public  class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

}
