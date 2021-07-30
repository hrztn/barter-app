package com.example.barterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.TradeModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;

public class TradeDetailsActivity extends AppCompatActivity {
    private ImageView RitemImage, Offered_ItemImage, userImage;
    private TextView rName, oName, userName, ConfirmationtextView;
    private Button chatButton, markCompletedButton, CancelButton;
    private TextView datetv;
    private TradeModelClass mTradeModelClass = new TradeModelClass();
    private String otherUserID, otherUserItemID;
    private LinearLayout requestedItemView, offeredItemView;
    private TradeItemModelClass itemofOtherUser = new TradeItemModelClass();
    Dialog mDialog;
    Double ratingTotal;
    int reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_details);
        getSupportActionBar().hide();
        mTradeModelClass = (TradeModelClass) getIntent().getSerializableExtra("trade");
        initView();
    }

    private void initView() {
        offeredItemView = findViewById(R.id.DtofferedItemView);
        requestedItemView = findViewById(R.id.DtrequestedItemView);
        RitemImage = findViewById(R.id.DtrequestItemImageView);
        userImage = findViewById(R.id.Dt_user_imageView);
        userName = findViewById(R.id.Dt_textView_username);
        Offered_ItemImage = findViewById(R.id.DtofferedItemImageView);
        chatButton = findViewById(R.id.DtchatButton);
        CancelButton = findViewById(R.id.dtCancelButton);
        markCompletedButton = findViewById(R.id.DtMarkCompletedButton);
        rName = findViewById(R.id.DtrequestItemName);
        oName = findViewById(R.id.Dtoffereditem_name);
        datetv = findViewById(R.id.Dtdate_textView_D);
        ConfirmationtextView = findViewById(R.id.ConfirmationtextView);
        rName.setText(mTradeModelClass.getItemOne().getName());
        oName.setText(mTradeModelClass.getItemTwo().getName());
        mDialog = new Dialog(this);

        if (mTradeModelClass.isCanceled()) {
            CancelButton.setVisibility(GONE);
            markCompletedButton.setVisibility(GONE);
            ConfirmationtextView.setText("TRADE FORFEITED.");
            ConfirmationtextView.setVisibility(View.VISIBLE);
        }

        setDate();

        // if(!(mTradeModelClass.isCompletedByuserOne() && mTradeModelClass.isCompletedByuserTwo())) {}
        if (mTradeModelClass.getUserOneId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            if (mTradeModelClass.isCompletedByuserOne()) {
                markCompletedButton.setVisibility(GONE);
                ConfirmationtextView.setVisibility(View.VISIBLE);
            }
            otherUserID = mTradeModelClass.getUserTwoId();
            otherUserItemID = mTradeModelClass.getItemTwo().getItemId();
            itemofOtherUser = mTradeModelClass.getItemTwo();
        } else {
            if (mTradeModelClass.isCompletedByuserTwo()) {
                markCompletedButton.setVisibility(GONE);
                ConfirmationtextView.setVisibility(View.VISIBLE);
            }
            otherUserID = mTradeModelClass.getUserOneId();
            otherUserItemID = mTradeModelClass.getItemOne().getItemId();
            itemofOtherUser = mTradeModelClass.getItemOne();
        }

        Picasso.get().load(mTradeModelClass.getItemOne().getImageUrl()).into(RitemImage);
        Picasso.get().load(mTradeModelClass.getItemTwo().getImageUrl()).into(Offered_ItemImage);

        getUserByID(otherUserID);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TradeDetailsActivity.this, ChatActivity.class);
                i.putExtra("chatid", mTradeModelClass.getChat_id());
                i.putExtra("otherId", otherUserID);
                startActivity(i);
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTradeModelClass.setCanceled(true);
                markCompletedButton.setVisibility(GONE);
                ConfirmationtextView.setText("TRADE FORFEITED.");
                ConfirmationtextView.setVisibility(View.VISIBLE);
                CancelButton.setVisibility(View.GONE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference().child("trades");

                databaseReference.child(mTradeModelClass.getTradeID()).setValue(mTradeModelClass);
            }
        });

        markCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTradeModelClass.getUserOneId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    mTradeModelClass.setCompletedByuserOne(true);
                    getReView();
                    if (mTradeModelClass.isCompletedByuserTwo()) {
                        mTradeModelClass.setTime(Calendar.getInstance().getTime().toString());
                        deleteItemById(mTradeModelClass.getItemOne().getItemId());
                        deleteItemById(mTradeModelClass.getItemTwo().getItemId());
                        setDate();
                    }
                } else {

                    mTradeModelClass.setCompletedByuserTwo(true);
                    getReView();
                    if (mTradeModelClass.isCompletedByuserOne()) {
                        mTradeModelClass.setTime(Calendar.getInstance().getTime().toString());
                        deleteItemById(mTradeModelClass.getItemOne().getItemId());
                        deleteItemById(mTradeModelClass.getItemTwo().getItemId());
                        setDate();
                    }

                }

            }
        });

        offeredItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TradeDetailsActivity.this, ItemInfoActivity.class);
                intent.putExtra("item", (Serializable) mTradeModelClass.getItemTwo());
                startActivity(intent);
            }
        });
        requestedItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TradeDetailsActivity.this, ItemInfoActivity.class);
                intent.putExtra("item", (Serializable) mTradeModelClass.getItemOne());
                startActivity(intent);
            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TradeDetailsActivity.this, PublicUserViewActivity.class);
                i.putExtra("id", otherUserID);
                startActivity(i);
            }
        });

    }

    private String setDate() {
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(mTradeModelClass.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            Date d = cal.getTime();
            date.setTimeZone(TimeZone.getTimeZone("GMT"));
            String localTime = date.format(d);

            if (mTradeModelClass.isCompletedByuserOne() && mTradeModelClass.isCompletedByuserTwo()) {
                datetv.setText("Completed on " + localTime);
                ConfirmationtextView.setVisibility(GONE);
//            date.setText("Completed");
//            holder.stutesTv.setTextColor(GREEN);
            } else {
                datetv.setText("Started on " + localTime);
//            holder.stutesTv.setText("InProgress");
            }
        }
        return null;
    }

    private void getUserByID(String s) {
        if (mTradeModelClass.getUserOneId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            s = mTradeModelClass.getUserTwoId();
        } else {
            s = mTradeModelClass.getUserOneId();
        }
        DatabaseReference rrr = FirebaseDatabase.getInstance().getReference().child("users").child(s);
        rrr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                String imageUrl = "" + map.get("userImage");
                String uname = "" + map.get("userName");
                ratingTotal = 0.0;//(Double) map.get("totalStars");
                reviews = 0;//(int) map.get("total_reviews");
                userName.setText(uname);
                Picasso.get().load(imageUrl).into(userImage);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    private void deleteItemById(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("trade_items").orderByChild("itemId").equalTo(s);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }


    public void getReView() {


        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.rating_popup);
        final Button b = (Button) mDialog.getWindow().findViewById(R.id.okbuttonraating);
        final RatingBar rate = mDialog.getWindow().findViewById(R.id.ratingBargetting);
        final RatingBar rateitem = mDialog.getWindow().findViewById(R.id.ratingBargettingItem);
        final RatingBar rateuser = mDialog.getWindow().findViewById(R.id.ratingBargettingTrader);

        b.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ((HomeActivity)getActivity()).setNextItem(R.id.navigation_test);
                Double rating = Double.parseDouble(String.valueOf(rate.getRating()));
                Double ratingitem = Double.parseDouble(String.valueOf(rate.getRating()));
                Double ratingUser = Double.parseDouble(String.valueOf(rate.getRating()));
                Log.d("TAG", "onClick: " + rating + " " + rate.getRating());
                //mTradeModelClass.setRating(rating);
                //Toast.makeText(TradeDetailsActivity.this,""+mTradeModelClass.getRating(),Toast.LENGTH_LONG).show();
                markCompletedButton.setVisibility(GONE);
                ConfirmationtextView.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRating = database.getReference("users").child(otherUserID);
                DatabaseReference itemRating = database.getReference("trade_items").child(itemofOtherUser.getItemId());
                userRating.child("totalStars").setValue(ratingTotal + ratingUser);
                userRating.child("total_reviews").setValue(reviews + 1);
                //itemRating.child("totalStars").setValue(itemofOtherUser.getTotalStars() + ratingitem);
                //itemRating.child("total_reviews").setValue(itemofOtherUser.getTotal_reviews() + 1);
                DatabaseReference databaseReference = database.getReference().child("trades");

                databaseReference.child(mTradeModelClass.getTradeID()).setValue(mTradeModelClass);
                mDialog.dismiss();

            }
        });


        mDialog.show();


    }

}