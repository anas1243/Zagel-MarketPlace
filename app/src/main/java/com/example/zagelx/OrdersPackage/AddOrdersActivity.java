package com.example.zagelx.OrdersPackage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zagelx.Models.Orders;
import com.example.zagelx.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddOrdersActivity extends AppCompatActivity {

    private static final String TAG = "AddOrdersActivity";
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private Button addPackageImage;
    private ImageView packageImage;

    private EditText packageNameET;
    private EditText deliveryDateET;
    private EditText deliveryPriceET;

    private EditText sourceET;
    private EditText destinationET;

    private Button AddOrderButton;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPackagePhotoStorageReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order_activity);

        packageImage = findViewById(R.id.package_image);
         packageNameET=findViewById(R.id.package_name);
         deliveryDateET=findViewById(R.id.delivery_date);
         deliveryPriceET=findViewById(R.id.delivery_price);

         sourceET=findViewById(R.id.source_txt_view);
         destinationET=findViewById(R.id.destination_txt_view);
        AddOrderButton =findViewById(R.id.orders_button);
        addPackageImage = findViewById(R.id.add_packageImage);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Orders");


        mFirebaseStorage = FirebaseStorage.getInstance();
        mPackagePhotoStorageReference = mFirebaseStorage.getReference().child("packages_photos");

        mFirebaseAuth = FirebaseAuth.getInstance();

        addPackageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        AddOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orders coolMessage = new Orders(00000,"Anas Hassan",54545454
                        ,packageNameET.getText().toString(),deliveryDateET.getText().toString()
                ,deliveryPriceET.getText().toString(),sourceET.getText().toString(),
                        destinationET.getText().toString(),00001);
                mMessagesDatabaseReference.push().setValue(coolMessage);



            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            final StorageReference photoRef =
                    mPackagePhotoStorageReference.child(selectedImageUri.getLastPathSegment());
            uploadTask = photoRef.putFile(selectedImageUri);


            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Glide.with(packageImage.getContext())
                                .load(downloadUri.toString())
                                .into(packageImage);
                    } else {
                        Log.e(TAG, "onComplete: ya satr ya raaaab" );

                    }
                }
            });


        }
    }
}
