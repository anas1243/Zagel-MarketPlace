package com.example.zagelx.Utilities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.zagelx.DashboardPackage.DelegateDashboardActivity;
import com.example.zagelx.DashboardPackage.MerchantDashboardActivity;
import com.example.zagelx.UserInfo.AboutUsActivity;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.DashboardPackage.DashboardActivity;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.UserInfo.ProfileActivity;
import com.example.zagelx.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;




public class DrawerUtil extends Activity {
    String userId, photoUrl, name, phone, uId, mode;

    public DrawerUtil(String name, String phone, String photoUrl, String mode) {
        this.name = name;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.mode = mode;
    }

    public void getDrawer(final Activity activity, Toolbar toolbar) {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(activity).load(photoUrl).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
            }
        });
        // Create the AccountHeader
        DrawerUtil drawer = new DrawerUtil(name, phone,photoUrl, mode);

        PrimaryDrawerItem drawerItemHome = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.nav_home).withIcon(R.drawable.ic_home);

        PrimaryDrawerItem drawerItemProfile = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.nav_profile).withIcon(R.drawable.ic_profile);

        SecondaryDrawerItem drawerItemDashboard = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.nav_dashboard).withIcon(R.drawable.ic_dash_board);

        SecondaryDrawerItem drawerItemNotify = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.nav_listen).withIcon(R.drawable.ic_notification);

        SecondaryDrawerItem drawerItemAboutUs = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.nav_about_us).withIcon(R.drawable.ic_about_us);

        SecondaryDrawerItem drawerItemLogOut = new SecondaryDrawerItem().withIdentifier(6)
                .withName(R.string.nav_logout).withIcon(R.drawable.ic_logout);


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.clouds_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(phone)
                                .withIcon(photoUrl)
                        //.withTextColor(R.color.colorPrimaryDark)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })//.withTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .build();


        //create the drawer and remember the `Drawer` result object
        final Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerItemHome,
                        drawerItemProfile,
                        new DividerDrawerItem(),
                        drawerItemDashboard,
                        drawerItemNotify,
                        new DividerDrawerItem(),
                        drawerItemAboutUs,
                        drawerItemLogOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1 && (!(activity instanceof OrdersActivity))) {
                            // load home screen.
                            Intent intent;
                                intent = new Intent(activity, OrdersActivity.class);
                            view.getContext().startActivity(intent);
                        } else if (drawerItem.getIdentifier() == 2 && !(activity instanceof ProfileActivity)) {
                            // load profile/user screen.
                            Intent intent = new Intent(activity, ProfileActivity.class);
                            view.getContext().startActivity(intent);
                        }else if (drawerItem.getIdentifier() == 3 && (!(activity instanceof MerchantDashboardActivity)
                                && !(activity instanceof DelegateDashboardActivity))){
                            if (mode.equals("Merchant")){
                                Intent intent = new Intent(activity, MerchantDashboardActivity.class);
                                intent.putExtra("Which_Activity", "SomethingElse");
                                view.getContext().startActivity(intent);
                            }else{
                                Intent intent = new Intent(activity, DelegateDashboardActivity.class);
                                intent.putExtra("Which_Activity", "SomethingElse");
                                view.getContext().startActivity(intent);
                            }

                        } else if (drawerItem.getIdentifier() == 4 && !(activity instanceof NotificationsActivity)) {
                            Intent intent = new Intent(activity, NotificationsActivity.class);
                            view.getContext().startActivity(intent);

                        } else if (drawerItem.getIdentifier() == 5 && !(activity instanceof AboutUsActivity)) {
                            Intent intent = new Intent(activity, AboutUsActivity.class);
                            view.getContext().startActivity(intent);

                        }else if (drawerItem.getIdentifier() == 6) {
                            AuthUI.getInstance().signOut(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }
                            });

                        }
                        return true;
                    }
                })
                .build();


    }

}