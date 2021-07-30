package com.example.barterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.UserModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;

public class ItemInfoActivity extends AppCompatActivity {
    TradeItemModelClass tradeItemModelClass = new TradeItemModelClass();
    UserModelClass Trader = new UserModelClass();
    ImageView itemDIV, userDIV;
    TextView userNameD, itemNameD, itemQualityD, itemDescD, itemPrefD, itemCondD, reviewsItemD;
    Button req_trade_btn, req_chat_btn;
    RatingBar ratingBarItemD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        getSupportActionBar().hide();
        tradeItemModelClass = (TradeItemModelClass) getIntent().getSerializableExtra("item");
        init_Views();

    }

    private void init_Views() {
        userNameD = findViewById(R.id.D_textView_username);
        itemNameD = findViewById(R.id.item_nameD);
        ratingBarItemD = findViewById(R.id.ratingBarItemDetails);
        reviewsItemD = findViewById(R.id.no_of_reviews);
        itemCondD = findViewById(R.id.D_item_condition);
        itemQualityD = findViewById(R.id.D_item_quantity);
        itemDescD = findViewById(R.id.D_decriptiontextView);
        itemPrefD = findViewById(R.id.trade_with);
        itemDIV = findViewById(R.id.D_imageView);
        userDIV = findViewById(R.id.D_user_imageView);
        req_trade_btn = findViewById(R.id.req_itemD_btn);
        req_chat_btn = findViewById(R.id.chat_itemD_btn);
        reviewsItemD.setText(tradeItemModelClass.getTotal_reviews() + " Reviews");
        float rating = (float) (tradeItemModelClass.getTotalStars() / tradeItemModelClass.getTotal_reviews());
        ratingBarItemD.setRating(rating);

        req_trade_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemInfoActivity.this, RequestTradeActivity.class);
                intent.putExtra("item", (Serializable) tradeItemModelClass);
                startActivity(intent);
                finish();
            }
        });

        req_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatid=tradeItemModelClass.getItemId()+ FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent i=new Intent(ItemInfoActivity.this, ChatActivity.class);
                i.putExtra("chatid",chatid);
                i.putExtra("otherId",tradeItemModelClass.getTraderID());
                startActivity(i);
            }
        });

        getUserInfo(tradeItemModelClass.getTraderID());
        itemNameD.setText(tradeItemModelClass.getName());
        itemCondD.setText(tradeItemModelClass.getCondition());
        itemQualityD.setText("Quantity : "+tradeItemModelClass.getQuantity());
        itemDescD.setText(tradeItemModelClass.getDescription());
        itemPrefD.setText(tradeItemModelClass.getTradeWith());
        Picasso.get().load(tradeItemModelClass.getImageUrl()).into(itemDIV);
        userNameD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ItemInfoActivity.this, PublicUserViewActivity.class);
                i.putExtra("id",tradeItemModelClass.getTraderID());
                startActivity(i);
            }
        });

    }

    private void setData() {

    }

    private void getUserInfo(String uID) {
        DatabaseReference rrr= FirebaseDatabase.getInstance().getReference().child("users").child(uID);
        rrr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                String imageUrl=""+map.get("userImage");
                String uname=""+map.get("userName");
                userNameD.setText(uname);
                Picasso.get().load(imageUrl).into(userDIV);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Unable to identify value.", error.toException());
            }
        });
    }


}