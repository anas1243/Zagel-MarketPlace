package com.example.zagelx.DashboardPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.example.zagelx.MainPackage.MainActivity;
import com.example.zagelx.Models.Users;
import com.example.zagelx.R;
import com.example.zagelx.Utilities.DrawerUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelegateDashboardActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserEventListener;

    private FirebaseUser user;
    private Users currentUser;

    private String whichActivity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerUtil drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_view_pager_main_dashboard);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        Intent i = getIntent();
        whichActivity = (String) i.getSerializableExtra("Which_Activity");
        if (whichActivity.equals("OrderDetails")) {
            String pickedOrDelivered = (String) i.getSerializableExtra("PickedORDelivered");
            if (pickedOrDelivered.equals("Picked")) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_main_layout), "الرجاء السراع في توصيل الشحنة لتعزيز الثقة بينك و بين التطبيق", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (pickedOrDelivered.equals("Delivered")) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_main_layout), "نتمنا ان تكون قد استمتعت باستخدام التطبيق", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_main_layout), "الرحلات و الشحنات الخاصة بك !", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        if (user != null) {

            mUserEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(Users.class);

                    ButterKnife.bind(DelegateDashboardActivity.this);
                    setSupportActionBar(toolbar);

                    drawer = new DrawerUtil(currentUser.getName()
                            , currentUser.getMobileNumber(), currentUser.getProfilePictureURL(), currentUser.getMode());
                    drawer.getDrawer(DelegateDashboardActivity.this, toolbar);
                    // Find the view pager that will allow the user to swipe between fragments
                    ViewPager viewPager = findViewById(R.id.viewpager);

                    // Create an adapter that knows which fragment should be shown on each page
                    SimpleFragmentPagerAdapterForDelegate adapter
                            = new SimpleFragmentPagerAdapterForDelegate(getSupportFragmentManager()
                            , DelegateDashboardActivity.this, user.getUid());

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = findViewById(R.id.tabMode);
                    tabLayout.setupWithViewPager(viewPager);
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(DelegateDashboardActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent i = new Intent(DelegateDashboardActivity.this, MainActivity.class);
        startActivity(i);

        return super.onKeyDown(keyCode, event);
    }
}
