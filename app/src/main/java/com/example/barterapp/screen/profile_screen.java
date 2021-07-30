package com.example.barterapp.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.MyTradeActivity;
import com.example.barterapp.NewItemActivity;
import com.example.barterapp.R;
import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.UserModelClass;
import com.example.barterapp.modules.MyItemsRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class profile_screen extends Fragment {
    TextView numberOfReviewsTV, nameTV, emailTV, phoneTV, addressTV;
    ImageView userIV;
    ImageButton phoneEdit, addressEdit;
    RecyclerView myItemsRV;
    Button manageItems, tradeBtn;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String userID;
    RatingBar ratingBarUser;
    MyItemsRecyclerViewAdapter adapter;
    private ArrayList<TradeItemModelClass> ItemsList=new ArrayList<>();


    public profile_screen(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        numberOfReviewsTV=view.findViewById(R.id.numberOfReviews);
        nameTV=view.findViewById(R.id.myProfile_name);
        emailTV=view.findViewById(R.id.myProfile_email);
        phoneTV=view.findViewById(R.id.myProfile_phone);
        addressTV=view.findViewById(R.id.myProfile_address);
        userIV=view.findViewById(R.id.userImageView);
        phoneEdit=view.findViewById(R.id.myProfile_phoneEdit);
        addressEdit=view.findViewById(R.id.myProfile_addEdit);
        myItemsRV=view.findViewById(R.id.myItemsRecyclerView);
        manageItems=view.findViewById(R.id.Manage_StoreButton);
        tradeBtn=view.findViewById(R.id.MyTrade_Btn);
        ratingBarUser=view.findViewById(R.id.ratingBarUserSelf);
        nameTV.setText(user.getDisplayName());
        emailTV.setText(user.getEmail());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getUserInfo(user.getUid());
        Picasso.get().load(user.getPhotoUrl()).into(userIV);
        myItemsRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter=new MyItemsRecyclerViewAdapter(getContext(),ItemsList);
        myItemsRV.setAdapter(adapter);
        getdata();



        manageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(), NewItemActivity.class);
                startActivity(i);
            }
        });

        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getContext(), MyTradeActivity.class);
                startActivity(i);
            }
        });





        phoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBox = new AlertDialog.Builder(getContext());
                alertBox.setTitle("Edit Phone Number: ");

                final EditText changePhone = new EditText(getContext());
                changePhone.setInputType(InputType.TYPE_CLASS_PHONE);

                alertBox.setView(changePhone);

                alertBox.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String myPhone = changePhone.getText().toString();
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference myPhoneRef = firebaseDatabase.getReference("users").child(userID).child("userPhoneNo");
                        myPhoneRef.setValue(myPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Phone number updated", Toast.LENGTH_SHORT).show();
                                phoneTV.setText(myPhone);
                            }
                        });
                    }
                });

                alertBox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertBox.show();
            }
        });

        addressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBox = new AlertDialog.Builder(getContext());
                alertBox.setTitle("Edit Address: ");

                final EditText changeAddress = new EditText(getContext());
                changeAddress.setInputType(InputType.TYPE_CLASS_TEXT);

                alertBox.setView(changeAddress);

                alertBox.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String myAddress = changeAddress.getText().toString();
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference myPhoneRef = firebaseDatabase.getReference("users")
                                .child(userID).child("userAddress");
                        myPhoneRef.setValue(myAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Address updated", Toast.LENGTH_SHORT).show();
                                addressTV.setText(myAddress);
                            }
                        });
                    }
                });

                alertBox.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertBox.show();
            }
        });


        return view;
    }


    public void getdata(){
        Log.d("test","list created");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trade_items");
        Query getpost =reference.orderByChild("traderID").equalTo(user.getUid());
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
                Log.d("test", "onChildRemoved");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("test", "onChildMovied");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("test", "loadPost:onCancelled", databaseError.toException());
            }
        });
        adapter=new MyItemsRecyclerViewAdapter(getContext(),ItemsList);

        myItemsRV.setAdapter(adapter);

    }

    private void getUserInfo(String uID) {
        DatabaseReference rrr= FirebaseDatabase.getInstance().getReference().child("users").child(uID);
        rrr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModel =dataSnapshot.getValue(UserModelClass.class);
                nameTV.setText(StringUtils.capitalize(userModel.getUserName()));
                emailTV.setText(userModel.getUserEmail());
                phoneTV.setText(userModel.getUserPhoneNo());
                addressTV.setText(StringUtils.capitalize(userModel.getUserAddress()));
                Picasso.get().load(userModel.getUserImage()).into(userIV);
                

                float rating=(float)(userModel.getTotalStars()/userModel.getTotal_reviews());

                ratingBarUser.setRating(rating);
                numberOfReviewsTV.setText(""+" Reviews");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }








}
