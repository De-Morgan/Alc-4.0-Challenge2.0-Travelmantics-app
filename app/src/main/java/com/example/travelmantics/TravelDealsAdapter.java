package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.Resource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TravelDealsAdapter extends RecyclerView.Adapter<TravelDealsAdapter.TravelDealViewHolder> {

    private final FirebaseDatabase mFirebaseDatabase;
    private final DatabaseReference mDatabaseReference;
    private ArrayList<TravelDeal> mTravelDeal;
    private ChildEventListener mChildListerner;
    private ImageView imageView;

    public TravelDealsAdapter() {
        mFirebaseDatabase = FirebaseUtils.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtils.mDatabaseReference;
        this.mTravelDeal= FirebaseUtils.mDeals;
        mChildListerner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal",travelDeal.getTitle());
                travelDeal.setId(dataSnapshot.getKey());
                mTravelDeal.add(travelDeal);
                notifyItemInserted(mTravelDeal.size() - 1);
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
        mDatabaseReference.addChildEventListener(mChildListerner);

    }

    @NonNull
    @Override
    public TravelDealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.travel_deals_row,parent,false);
        return new  TravelDealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelDealViewHolder holder, int position) {
       TravelDeal travelDeal = mTravelDeal.get(position);
        holder.bind(travelDeal);
    }

    @Override
    public int getItemCount() {
        return mTravelDeal.size();
    }

    class  TravelDealViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvPrice;
        private TextView tvDescription;


        TravelDealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageView = itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }
        void bind(TravelDeal travelDeal){
           tvTitle.setText(travelDeal.getTitle());
           tvPrice.setText(travelDeal.getPrice());
           tvDescription.setText(travelDeal.getDescription());
           showImage(travelDeal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TravelDeal selectedTravelDeal = mTravelDeal.get(position);
            Intent intent = new Intent(view.getContext(),DealActivity.class);
            intent.putExtra("Deal",selectedTravelDeal);
            view.getContext().startActivity(intent);


        }

        void showImage(String url){
            if(url != null && !url.isEmpty()){
                Picasso.with(itemView.getContext()).load(url).resize(160,160).centerCrop().into(imageView);
            }
        }
    }
}
