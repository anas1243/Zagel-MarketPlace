package com.example.zagelx.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.zagelx.FreeBirdsDashboardPackage.FreesDashboardActivity;
import com.example.zagelx.MerchantsDashboardPackage.MerchantsOrdersInside.MerchantDashboardInsideActivity;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.Models.Users;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.PMsDashboardPackage.PMInSideOrdersPackage.InsideOrdersDashboardActivity;
import com.example.zagelx.R;
import com.example.zagelx.StaticBirdsDashboardPackage.StaticsOrdersPackage.StaticsOrdersDashboardActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class SpalshActivity extends AppCompatActivity {


    //the request code for
    public static final int RC_SIGN_IN = 1;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabaseReference;
    private ValueEventListener mUserValueEventListener;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static Users currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //is signed in
                    mUserDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(user.getUid());
                    mUserValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            currentUser = dataSnapshot.getValue(Users.class);
                            if (currentUser != null)
                                AnExistingUserLogin();
                            else
                                addNewUser();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };
                    mUserDatabaseReference.addListenerForSingleValueEvent(mUserValueEventListener);

                } else {
                    //is signed out
                    registerUser();

                }

            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void addNewUser() {
        //Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SpalshActivity.this, AfterRegisterUserInfo.class);
        startActivity(i);

    }

    public void AnExistingUserLogin() {
        Intent i;
        Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
        switch (currentUser.getGroup()){
            case "AlexMerchants":
            case "CairoMerchants":
                if(currentUser.isVerified()){
                    i = new Intent(SpalshActivity.this, MerchantDashboardInsideActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }else{
                    i = new Intent(SpalshActivity.this, NotVerifiedUser.class);
                    startActivity(i);
                }
                break;
            case "AlexFreeBirds":
            case "CairoFreeBirds":
                if(currentUser.isVerified()){
                    i = new Intent(SpalshActivity.this, FreesDashboardActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }else{
                    i = new Intent(SpalshActivity.this, NotVerifiedUser.class);
                    startActivity(i);
                }
                break;
            case "AlexStaticBirds":
            case "CairoStaticBirds":
                if(currentUser.isVerified()){
                    i = new Intent(SpalshActivity.this, StaticsOrdersDashboardActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }else{
                    i = new Intent(SpalshActivity.this, NotVerifiedUser.class);
                    startActivity(i);
                }
                break;
            case "AlexPM":
            case "CairoPM":
                if(currentUser.isVerified()){
                    i = new Intent(SpalshActivity.this, InsideOrdersDashboardActivity.class);
                    i.putExtra("Which_Activity", "SomethingElse");
                    startActivity(i);
                }else{
                    i = new Intent(SpalshActivity.this, NotVerifiedUser.class);
                    startActivity(i);
                }
                break;
        }

    }

    private void registerUser() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

