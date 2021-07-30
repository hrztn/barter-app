package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.TradeModelClass;
import com.example.barterapp.model.UserModelClass;
import com.example.barterapp.modules.ItemServerAdapter;
import com.example.barterapp.modules.PublicTradeViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PublicUserViewActivity extends AppCompatActivity {
    ImageView puserIV;
    TextView puserName, puserEmail, puserPhone, puserAddress, puserNoReview;
    RecyclerView recyclerView;
    ItemServerAdapter adapter;
    PublicTradeViewAdapter tradeAdapter;
    String userID;
    RatingBar ratingBarUser;
    Button products,trades;
    private ArrayList<TradeItemModelClass> ItemsList=new ArrayList<>();
    private ArrayList<TradeModelClass> tradeList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_user_view);
        getSupportActionBar().hide();
        userID=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        puserIV=findViewById(R.id.pb_userImageView);
        puserName=findViewById(R.id.p_Profile_name);
        puserEmail=findViewById(R.id.p_Profile_email);
        puserPhone=findViewById(R.id.p_Profile_phone);
        puserAddress=findViewById(R.id.p_Profile_address);
        ratingBarUser=findViewById(R.id.p_ratingBarUser);
        puserNoReview=findViewById(R.id.p_numberOfReviews);
        products=findViewById(R.id.p_product_btn);
        trades=findViewById(R.id.p_trade_btn);
        recyclerView=findViewById(R.id.p_ItemsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter=new ItemServerAdapter(this,ItemsList);
        tradeAdapter=new PublicTradeViewAdapter(this,tradeList);
        recyclerView.setAdapter(adapter);
        getdata();
        getUserInfo(userID);
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.setEnabled(false);
                trades.setEnabled(true);
                recyclerView.setLayoutManager(new GridLayoutManager(PublicUserViewActivity.this, 3));
                recyclerView.setAdapter(adapter);
            }
        });
        trades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.setEnabled(true);
                trades.setEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(PublicUserViewActivity.this));
                recyclerView.setAdapter(tradeAdapter);

            }
        });

    }

    private void getdata() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trade_items");
        Query getpost =reference.orderByChild("traderID").equalTo(userID);
        getpost.addChildEventListener(new ChildEventListener() {
            ArrayList<TradeItemModelClass> mItems = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                TradeItemModelClass mTradeItemModelClass = dataSnapshot.getValue(TradeItemModelClass.class);
                if (ItemsList.add(mTradeItemModelClass)) {
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                }

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
        });
        adapter=new ItemServerAdapter(this,ItemsList);

        recyclerView.setAdapter(adapter);
        getTradesData();

    }

    private void getTradesData() {
        DatabaseReference fbdR= FirebaseDatabase.getInstance().getReference("trades");
        Query getpost =fbdR;
        getpost.addChildEventListener(new ChildEventListener() {
            ArrayList<TradeItemModelClass> mItems = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("hariz", "onChildAdded "+s);

                TradeModelClass mTradeModelClass = dataSnapshot.getValue(TradeModelClass.class);

                Log.d("ExpertFinder", ""+mTradeModelClass.getTime());

                if ((mTradeModelClass.getUserTwoId().equals(userID) || mTradeModelClass.getUserOneId().equals(userID) )&&
                        (mTradeModelClass.isCompletedByuserTwo()&&mTradeModelClass.isCompletedByuserOne())){
                    if (tradeList.add(mTradeModelClass)) {
                        tradeAdapter.notifyDataSetChanged();
                        Log.d("ExpertFinder", "added");
                    }
                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("ExpertFinder", "onChildChanged");


                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ExpertFinder", "onChildRemoved");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("ExpertFinder", "onChildMovied");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("EXpertFinder", "loadPost:onCancelled", databaseError.toException());
            }
        });
        tradeAdapter=new PublicTradeViewAdapter(this,tradeList);
    }

    private void getUserInfo(String uID) {
        DatabaseReference rrr= FirebaseDatabase.getInstance().getReference().child("users").child(uID);
        rrr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModel =dataSnapshot.getValue(UserModelClass.class);
                puserName.setText(userModel.getUserName());
                puserEmail.setText(userModel.getUserEmail());
                puserPhone.setText(userModel.getUserPhoneNo());
                puserAddress.setText(userModel.getUserAddress());
                Picasso.get().load(userModel.getUserImage()).into(puserIV);

                float rating=(float)(userModel.getTotalStars()/userModel.getTotal_reviews());

                ratingBarUser.setRating(rating);
                puserNoReview.setText(""+userModel.getTotal_reviews()+" Reviews");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}