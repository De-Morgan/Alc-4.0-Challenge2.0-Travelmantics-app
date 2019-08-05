package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;
    private EditText txtTitle;
    private EditText txtPrice;
    private EditText txtDescription;
    private TravelDeal travelDeal;
    private Button uploadButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);
        FirebaseDatabase mFirebaseDatabase = FirebaseUtils.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtils.mDatabaseReference;
        txtTitle = findViewById(R.id.txtTitle);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);
        uploadButton = findViewById(R.id.btnImage);
        imageView = findViewById(R.id.image);
        final Intent intent = getIntent();
        TravelDeal retrievedTravelDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(retrievedTravelDeal == null){
            retrievedTravelDeal = new TravelDeal();
        }

        this.travelDeal = retrievedTravelDeal;
        txtTitle.setText(retrievedTravelDeal.getTitle());
        txtPrice.setText(retrievedTravelDeal.getPrice());
        txtDescription.setText(retrievedTravelDeal.getDescription());
        Log.d("UPLOAD_SUCCESS", "This is the TravelDeal we retrieve  " + travelDeal);
        showImage(retrievedTravelDeal.getImageUrl());

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT)  ;
              getImageIntent.setType("image/jpeg");
              getImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
              startActivityForResult(getImageIntent.createChooser(getImageIntent,"Choose Picture"),PICTURE_RESULT);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId() ) {

            case R.id.action_save:
            saveDeal();
            clean();
            backToList();
            return true;
            case R.id.action_delete:
                deleteTravelDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_SHORT).show();
                backToList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clean() {
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    }

    private void saveDeal() {
       travelDeal.setTitle(txtTitle.getText().toString());
        travelDeal.setPrice(txtPrice.getText().toString()) ;
        travelDeal.setDescription(txtDescription.getText().toString()) ;
        if(travelDeal.getId() == null){
            mDatabaseReference.push().setValue(travelDeal);
            Toast.makeText(this, "The Value has been set", Toast.LENGTH_SHORT).show();
        }else {
            mDatabaseReference.child(travelDeal.getId()).setValue(travelDeal);
        }
    }

    private void deleteTravelDeal(){
        if(travelDeal == null){
            Toast.makeText(this, "Please Save The Deal", Toast.LENGTH_SHORT).show();
        }else {
            mDatabaseReference.child(travelDeal.getId()).removeValue();
        }
    }

    private void backToList(){
        Intent intent = new Intent (this,TravelManticsList.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri pictureUri = data.getData();
            final StorageReference reference = FirebaseUtils.storageReference.child(pictureUri.getLastPathSegment());
            UploadTask uploadTask = reference.putFile(pictureUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();
                        travelDeal.setImageUrl(url);
                        saveDeal();
                        showImage(url);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }

    void showImage(String url){
        if(url != null){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this).load(url).resize(width,width * 2/3).centerCrop().into(imageView);
        }
    }
}
