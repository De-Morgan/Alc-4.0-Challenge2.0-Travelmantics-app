package com.example.travelmantics;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {
    private static final int RC_SIGN_IN = 124;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtils firebaseUtils;
    public static ArrayList<TravelDeal> mDeals;
    public static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseStorage firebaseStorage;
     static StorageReference storageReference;


    private FirebaseUtils(){}
    private static Activity callerActivity;
    public static void openReference(String ref, final Activity callerActivity){
        if(firebaseUtils == null){
            firebaseUtils = new FirebaseUtils();
            FirebaseUtils.callerActivity = callerActivity;
            mFirebaseDatabase =  FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){
                        signIn();
                    }

                }
            };
            connectStorage();
        }
        mDeals = new ArrayList<TravelDeal>();

        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);

    }


    public static void attachListener(){
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    public static void detachListener(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    public static void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

// Create and launch sign-in intent
        FirebaseUtils.callerActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void connectStorage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("deals_pictures");

    }
}

