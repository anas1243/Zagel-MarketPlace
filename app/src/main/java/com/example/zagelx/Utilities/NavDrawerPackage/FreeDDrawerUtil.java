package com.example.zagelx.Utilities.NavDrawerPackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zagelx.EWalletPackage.MerchantsEWalletActivity;
import com.example.zagelx.FreeBirdsDashboardPackage.FreesDashboardActivity;
import com.example.zagelx.OrdersPackage.OrdersActivity;
import com.example.zagelx.R;
import com.example.zagelx.UserInfo.AboutUsActivity;
import com.example.zagelx.UserInfo.NotificationsActivity;
import com.example.zagelx.UserInfo.ProfileActivity;
import com.example.zagelx.Utilities.TermsAndCondActivity;
import com.firebase.ui.auth.AuthUI;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

public class FreeDDrawerUtil extends Activity {
    String userId, photoUrl, name, phone, uId, mode;

    public FreeDDrawerUtil(String name, String phone, String photoUrl, String mode) {
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
        MerchantDrawerUtil drawer = new MerchantDrawerUtil(name, phone,photoUrl, mode);

        PrimaryDrawerItem drawerItemOrders = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.nav_orders).withIcon(R.drawable.ic_home);


        PrimaryDrawerItem drawerItemDashboard = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.nav_dashboard_orders).withIcon(R.drawable.ic_dash_board);

        PrimaryDrawerItem drawerItemEWallet = new PrimaryDrawerItem().withIdentifier(3)
                .withName(R.string.nav_eWallet).withIcon(R.drawable.ic_cash);

        PrimaryDrawerItem drawerItemNotify = new PrimaryDrawerItem().withIdentifier(4)
                .withName(R.string.nav_notify).withIcon(R.drawable.ic_notification);

        PrimaryDrawerItem drawerItemProfile = new PrimaryDrawerItem().withIdentifier(5)
                .withName(R.string.nav_profile).withIcon(R.drawable.ic_profile);

        PrimaryDrawerItem drawerItemAboutUs = new PrimaryDrawerItem().withIdentifier(6)
                .withName(R.string.nav_about_us).withIcon(R.drawable.ic_about_us);

        PrimaryDrawerItem drawerItemTerms = new PrimaryDrawerItem().withIdentifier(7)
                .withName(R.string.nav_terms).withIcon(R.drawable.ic_terms);

        PrimaryDrawerItem drawerItemLogOut = new PrimaryDrawerItem().withIdentifier(8)
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
                        drawerItemOrders,
                        drawerItemDashboard,
                        new DividerDrawerItem(),
                        drawerItemEWallet,
                        new DividerDrawerItem(),
                        drawerItemNotify,
                        drawerItemProfile,
                        new DividerDrawerItem(),
                        drawerItemAboutUs,
                        drawerItemTerms,
                        drawerItemLogOut
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1 && (!(activity instanceof OrdersActivity))) {
                            // load home screen.
                            Intent intent = new Intent(activity, OrdersActivity.class);
                            view.getContext().startActivity(intent);
                        } else if (drawerItem.getIdentifier() == 2 && !(activity instanceof FreesDashboardActivity)) {
                            // load profile/user screen.
                            Intent intent = new Intent(activity, FreesDashboardActivity.class);
                            intent.putExtra("Which_Activity", "AnyThingElse");
                            view.getContext().startActivity(intent);
                        }else if (drawerItem.getIdentifier() == 3 && (!(activity instanceof MerchantsEWalletActivity))){

                            Intent intent = new Intent(activity, MerchantsEWalletActivity.class);
                            view.getContext().startActivity(intent);

                        } else if (drawerItem.getIdentifier() == 4 && !(activity instanceof NotificationsActivity)) {
                            Intent intent = new Intent(activity, NotificationsActivity.class);
                            view.getContext().startActivity(intent);

                        } else if (drawerItem.getIdentifier() == 5 && !(activity instanceof ProfileActivity)) {
                            Intent intent = new Intent(activity, ProfileActivity.class);
                            view.getContext().startActivity(intent);

                        }else if (drawerItem.getIdentifier() == 6 && (!(activity instanceof AboutUsActivity))){

                            Intent intent = new Intent(activity, AboutUsActivity.class);
                            view.getContext().startActivity(intent);

                        }else if (drawerItem.getIdentifier() == 7 && (!(activity instanceof TermsAndCondActivity))){

                            Intent intent = new Intent(activity, TermsAndCondActivity.class);
                            view.getContext().startActivity(intent);

                        }else if (drawerItem.getIdentifier() == 8) {
                            AuthUI.getInstance().signOut(activity);
                        }
                        return true;
                    }
                })
                .build();
    }

}