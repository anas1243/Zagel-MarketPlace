package com.example.zagelx.UserInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zagelx.Models.LocationInfoForUsers;
import com.example.zagelx.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class SetAcurrateLocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "AddOrdersMapActivity";
    private static final int LOCATION_PERMESSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 14;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private com.example.anas.zagel.Adaptors.PlaceAutocompleteAdapter mPlaceAutoCompleteAdapter;
    //private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168),
            new LatLng(71, 136)
    );


    private AutoCompleteTextView sourceAC;
    private ImageButton goCurrentLocationButton;
    private Button currentLocationIsSource;
    private Button nextButton;
    private boolean isCurrentisSource = false;

    double sourceLatlng[] = new double[2];
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_accurate_location_user);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Log.e(TAG, "onCreate: user iddddddd issss " + user.getUid() );

        progressBar = findViewById(R.id.progressBar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(user.getUid());

        sourceAC = findViewById(R.id.source_autoComplete);
        currentLocationIsSource = findViewById(R.id.currentLocation_is_source);
        goCurrentLocationButton = findViewById(R.id.go_current_location_button);
        nextButton = findViewById(R.id.btn_next);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mGeoDataClient = Places.getGeoDataClient(this);

        getLocationPermission();

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.e("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.e("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
                sourceLatlng[0] = arg0.getPosition().latitude;
                sourceLatlng[1] = arg0.getPosition().longitude;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.e("System out", "onMarkerDrag...");
            }
        });

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation(isCurrentisSource);

        init();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMESSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMESSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation(final boolean isLocationisSource) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null)
                                if (!isLocationisSource) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    Log.e("MapsActivity", "onComplete: lat is " + mLastKnownLocation.getLatitude() +
                                            "lng is " + mLastKnownLocation.getLongitude());
                                } else {
                                    String LatLongToAddress = getCompleteAddressString(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                    sourceAC.setText(LatLongToAddress);
                                    hideSoftKeyboard();

                                    sourceLatlng[0] = mLastKnownLocation.getLatitude();
                                    sourceLatlng[1] = mLastKnownLocation.getLongitude();

                                    moveCamera(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                            DEFAULT_ZOOM, LatLongToAddress);
                                    sourceAC.dismissDropDown();
                                    isCurrentisSource = false;

                                }
                        } else {
                            Log.d("MapActivity", "Current location is null. Using defaults.");
                            Log.e("MapActivity", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void init() {
        goCurrentLocationButton.setOnClickListener(this);
        currentLocationIsSource.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        sourceAC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideSoftKeyboard();
                final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(i);

                final String placeId = item.getPlaceId();

                Log.e(TAG, "onItemClick: you have clicked on this location" + placeId);

                geoLocateById(placeId, "S");
            }
        });

        mPlaceAutoCompleteAdapter = new com.example.anas.zagel.Adaptors.PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);

        sourceAC.setAdapter(mPlaceAutoCompleteAdapter);

        sourceAC.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                ) {
                    //start searching ^^

                    Log.e(TAG, "onEditorAction: the Selected item is " + mPlaceAutoCompleteAdapter.getmResultList().get(0).toString());
                    sourceAC.dismissDropDown();
                    sourceAC.setText(mPlaceAutoCompleteAdapter.getmResultList().get(0).getFullText(null));
                    hideSoftKeyboard();
                    geoLocateById(mPlaceAutoCompleteAdapter.getmResultList().get(0).getPlaceId(), "S");
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentLocation_is_source:
                progressBar.setVisibility(View.VISIBLE);
                isCurrentisSource = true;
                getDeviceLocation(isCurrentisSource);
                break;
            case R.id.go_current_location_button:
                getDeviceLocation(isCurrentisSource);
                break;
            case R.id.btn_next:
                validateLocationInfo();
                break;

        }
    }

    private void validateLocationInfo() {
        String sourceAcString = sourceAC.getText().toString().trim();
        if (sourceAcString.equals("")) {
            sourceAC.setError("Write your source");
            sourceAC.requestFocus();
        } else {

            Log.e(TAG, "sourceLatlngArray: " +sourceLatlng.toString());
            mUsersDatabaseReference.child("locationInfoForUser").child("uLat").setValue(sourceLatlng[0]+"");
            mUsersDatabaseReference.child("locationInfoForUser").child("uLng").setValue(sourceLatlng[1]+"");
            mUsersDatabaseReference.child("accurateLocation").setValue(true);
            Intent i = new Intent(SetAcurrateLocationActivity.this, ProfileActivity.class);
            finish();
            startActivity(i);
        }
    }

//    public void SetLocationInfo(double lat, double lng) {
//
//        Geocoder geocoder = new Geocoder(this);
//        List<Address> list;
//        try {
//            list = geocoder.getFromLocation(lat, lng, 20);
//
//            // 20 is no of address you want to fetch near by the given lat-long
//
//            for (Address address : list) {
//                if (address.getAdminArea() != null && address.getSubAdminArea() != null
//                        && address.getLocality() != null) {
//                        locationInfoForUser.setSLocationInfo(lat + "", lng + ""
//                                , address.getAdminArea().replace("Governorate", "").trim()
//                                , address.getSubAdminArea(), address.getLocality());
//                        Log.e(TAG, "SetLocationInfo: " + address);
//                    mUsersDatabaseReference.child("LocationInfo").setValue(locationInfoForUser);
//                    mUsersDatabaseReference.child("accurateLocation").setValue(true);
//                    Intent i = new Intent(SetAcurrateLocationActivity.this, ProfileActivity.class);
//                    startActivity(i);
//                        return;
//                }
//            }
//
//            for (Address address : list) {
//                if (address.getAdminArea() != null && address.getSubAdminArea() != null) {
//                        locationInfoForUser.setSLocationInfo(lat + "", lng + ""
//                                , address.getAdminArea().replace("Governorate", "").trim()
//                                , address.getSubAdminArea(), "");
//                        Log.e(TAG, "SetLocationInfo: " + address);
//                    mUsersDatabaseReference.child("LocationInfo").setValue(locationInfoForUser);
//                    mUsersDatabaseReference.child("accurateLocation").setValue(true);
//                    Intent i = new Intent(SetAcurrateLocationActivity.this, ProfileActivity.class);
//                    startActivity(i);
//                        return;
//                }
//            }
//            for (Address address : list) {
//                if (address.getAdminArea() != null) {
//                        locationInfoForUser.setSLocationInfo(lat + "", lng + ""
//                                , address.getAdminArea().replace("Governorate", "").trim()
//                                , "", "");
//                        Log.e(TAG, "SetLocationInfo: " + address);
//                    mUsersDatabaseReference.child("LocationInfo").setValue(locationInfoForUser);
//                    mUsersDatabaseReference.child("accurateLocation").setValue(true);
//                    Intent i = new Intent(SetAcurrateLocationActivity.this, ProfileActivity.class);
//                    startActivity(i);
//                        return;
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.e(TAG, "moveCamera: moving the camera to Lat" + latlng.latitude + " ,Lng" + latlng.longitude);
        hideSoftKeyboard();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        if (!title.equals("my location")) {
            mMap.clear();
            MarkerOptions options = new MarkerOptions().position(latlng).title(title).draggable(true);
            mMap.addMarker(options);
            progressBar.setVisibility(View.GONE);
        }
        hideSoftKeyboard();

    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //return full address by knowing Latlong "when we clicked on Mylocation is the source"

    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(SetAcurrateLocationActivity.this, Locale.getDefault());
        Log.e(TAG, "getCompleteAddressString: " + LONGITUDE + " " + LATITUDE);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                Log.e(TAG, "getCompleteAddressString: Is this an ID ????????????? " + returnedAddress);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My Current location address", strReturnedAddress.toString());
            } else {
                Log.e("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current location address", "Cannot get Address!" + e.getMessage());
        }
        return strAdd;
    }

    private void geoLocateById(String placeID, final String LocationToBeSavedInto) {
        mGeoDataClient.getPlaceById(placeID).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    @SuppressLint("RestrictedApi") Place myPlace = places.get(0);
                    Log.e(TAG, "Place found: " + myPlace.getName());
                    hideSoftKeyboard();

                    if (LocationToBeSavedInto.equals("S")) {
                        sourceLatlng[0] = myPlace.getLatLng().latitude;
                        sourceLatlng[1] = myPlace.getLatLng().longitude;
                    }
                    moveCamera(myPlace.getLatLng(), DEFAULT_ZOOM, myPlace.getName().toString());
                    places.release();
                } else {
                    Log.e(TAG, "Place not found.");
                }
            }
        });
    }
}