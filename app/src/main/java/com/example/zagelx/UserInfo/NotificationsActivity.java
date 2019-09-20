package com.example.zagelx.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.MerchantsDashboardPackage.MerchantDashboardActivity;
import com.example.zagelx.Models.DelegatesNotification;
import com.example.zagelx.Models.MerchantsNotifications;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.Utilities.DrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {

    private ListView mListView;
    private MerchantsNotificationsAdapter mMerchantNotificationsAdapter;
    private DelegatesNotificationsAdapter mDelegateNotificationAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNotificationsDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;


    final List<MerchantsNotifications> MerchantNotificationsList = new ArrayList<>();
    final List<DelegatesNotification> delegateNotificationsList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.e("test notifications", "Key: " + key + " Value: " + value);
            }
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mListView = findViewById(R.id.notifications_list);




        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_main_layout), "Your Notifications", Snackbar.LENGTH_SHORT);
        snackbar.show();

        if (user != null) {
            mUserDatabaseReference.child(user.getUid()).child("numberOfNotifications")
                    .setValue(0);
            mNotificationsDatabaseReference = mFirebaseDatabase
                    .getReference().child("Users/"+user.getUid()+"/Notifications");

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(NotificationsActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(NotificationsActivity.this, toolbar);

                    if (currentUser.getMode().equals("Merchant")){
                        showNotificationsToMerchant();
                    }
                    else if (currentUser.getMode().equals("Delivery Delegate")){
                        showNotificationsToDelegate();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mUserDatabaseReference.child(user.getUid())
                    .addListenerForSingleValueEvent(mUserEventListener);





        } else {
            AuthUI.getInstance().signOut(this);

        }

    }

    private void showNotificationsToMerchant(){
        mMerchantNotificationsAdapter = new MerchantsNotificationsAdapter(NotificationsActivity.this
                , R.layout.notification_item_on_order, MerchantNotificationsList);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MerchantsNotifications merchantsNotifications = dataSnapshot.getValue(MerchantsNotifications.class);
                mListView.setAdapter(mMerchantNotificationsAdapter);
                mMerchantNotificationsAdapter.add(merchantsNotifications);

                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mNotificationsDatabaseReference.addChildEventListener(mChildEventListener);
    }


    private void showNotificationsToDelegate(){

        mDelegateNotificationAdapter = new DelegatesNotificationsAdapter(NotificationsActivity.this
                , R.layout.notification_item_on_trip, delegateNotificationsList);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DelegatesNotification delegatesNotifications = dataSnapshot.getValue(DelegatesNotification.class);
                mListView.setAdapter(mDelegateNotificationAdapter);
                mDelegateNotificationAdapter.add(delegatesNotifications);

                //progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mNotificationsDatabaseReference.addChildEventListener(mChildEventListener);
    }
    @Override
    public void onBackPressed() {
        if (currentUser.getMode().equals("Merchant")){
            Intent i = new Intent(NotificationsActivity.this, MerchantDashboardActivity.class);
            i.putExtra("Which_Activity", "SomethingElse");
            finish();
            startActivity(i);
        }
        else if (currentUser.getMode().equals("Delivery Delegate")){
            Intent i = new Intent(NotificationsActivity.this, DelegateDashboardActivity.class);
            i.putExtra("Which_Activity", "SomethingElse");
            finish();
            startActivity(i);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentUser.getMode().equals("Merchant")){
                Intent i = new Intent(NotificationsActivity.this, MerchantDashboardActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                finish();
                startActivity(i);
            }
            else if (currentUser.getMode().equals("Delivery Delegate")){
                Intent i = new Intent(NotificationsActivity.this, DelegateDashboardActivity.class);
                i.putExtra("Which_Activity", "SomethingElse");
                finish();
                startActivity(i);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
