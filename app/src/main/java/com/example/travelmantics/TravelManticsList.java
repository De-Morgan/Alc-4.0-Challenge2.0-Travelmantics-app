package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class TravelManticsList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TravelDealsAdapter mTravelDealsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_mantics_list);
        }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtils.openReference("travelDeals",this);
        mRecyclerView = findViewById(R.id.travelDealsRecyclerView);
        mTravelDealsAdapter = new TravelDealsAdapter();
        mRecyclerView.setAdapter(mTravelDealsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        FirebaseUtils.attachListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);

        return true;
    }

    void logout(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUtils.attachListener();
                     }
                });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert:
                Intent intent = new Intent(TravelManticsList.this, DealActivity.class);
                startActivity(intent);
                return true;
            case  R.id.action_logout:
                logout();
                FirebaseUtils.detachListener();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public  void showMenu(){
        invalidateOptionsMenu();
    }
}
