package com.example.barterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterapp.model.MessageModelClass;
import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.TradeReqModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomRequestActivity extends AppCompatActivity {
    TradeItemModelClass Requsting_itemModelClass=new TradeItemModelClass();
    TradeItemModelClass Offering_itemModelClass=new TradeItemModelClass();
    Spinner requestedSpinner,spinner;
    ArrayList<String> myItemsName=new ArrayList<>();
    ArrayList<String> otherUsersItemsName=new ArrayList<>();
    ArrayList<TradeItemModelClass> myItems=new ArrayList<>();
    ArrayList<TradeItemModelClass> otherUsersItems=new ArrayList<>();
    LinearLayout offeredItemView,CustomRequestedLayout;
    ImageView RitemImage,Offered_ItemImage;
    TextView rName,oName,rCond,oCond,oQuantity,rQuantity;
    EditText messageET;
    Button request_send_button;
    String user_twoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_request);

        getSupportActionBar().hide();
        //Requsting_itemModelClass=(TradeItemModelClass) getIntent().getSerializableExtra("item");
        user_twoID=getIntent().getStringExtra("user_twoID");
        myItemsName.add("CHOOSE YOUR ITEM.");
        otherUsersItemsName.add("CHOOSE TRADER ITEM.");
        initView();
        requestedSpinner=findViewById(R.id.Customspinner2);
        spinner=findViewById(R.id.Customspinner);
        offeredItemView=findViewById(R.id.CustomofferedItemView);
        CustomRequestedLayout=findViewById(R.id.CustomRequestedLayout);
        setTeamsList();
        setOtherUsersItemsName();

        //spinner item selection handling
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    offeredItemView.setVisibility(View.VISIBLE);
                    for (TradeItemModelClass t:myItems){
                        if(t.getName().equals(spinner.getSelectedItem().toString())){
                            Offering_itemModelClass=t;
                            setSelectedItem(Offering_itemModelClass);
                            return;
                        }
                    }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        requestedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    CustomRequestedLayout.setVisibility(View.VISIBLE);
                    for (TradeItemModelClass t:otherUsersItems){
                        if(t.getName().equals(requestedSpinner.getSelectedItem().toString())){
                            Requsting_itemModelClass=t;
                            setSelectedRequestedItem(Requsting_itemModelClass);
                            return;
                        }
                    }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //initializing views to display data
    private void initView() {
        request_send_button =findViewById(R.id.Customrequest_send_button);
        RitemImage=findViewById(R.id.CustomrequestItemImageView);
        Offered_ItemImage=findViewById(R.id.CustomofferedItemImageView);
        rName=findViewById(R.id.CustomrequestItemName);
        oName=findViewById(R.id.Customoffereditem_name);
        rCond=findViewById(R.id.Customrequested_value_textViewD);
        oCond=findViewById(R.id.Customoffered_value_textViewD);
        oQuantity=findViewById(R.id.Customoffered_quantity_textView_D);
        rQuantity=findViewById(R.id.Customrequested_quantity_textView_D);
        messageET=findViewById(R.id.CustomrequestMessage);


        request_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageET.getText().toString().isEmpty()){
                    messageET.setError("message can't be Empty");
                    return;
                }
                if (spinner.getSelectedItemPosition()==0){
                    Toast.makeText(CustomRequestActivity.this,"Select Your Item",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (requestedSpinner.getSelectedItemPosition()==0){
                    Toast.makeText(CustomRequestActivity.this,"Select Your Item",Toast.LENGTH_SHORT).show();
                    return;
                }
                TradeReqModelClass request=new TradeReqModelClass();
                request.setRequestedItem(Requsting_itemModelClass);
                request.setOffered_Item(Offering_itemModelClass);
                request.setMessage(messageET.getText().toString());
                request.setRequestId(""+System.currentTimeMillis());
                request.setChat_id(Requsting_itemModelClass.getItemId()+Offering_itemModelClass.getTraderID());
                DatabaseReference fDBR= FirebaseDatabase.getInstance().getReference("requests");
                DatabaseReference ChatfDBR=FirebaseDatabase.getInstance().getReference("chat");
                fDBR.child(Requsting_itemModelClass.getTraderID()).child(request.getRequestId()).setValue(request);
                fDBR.child(Offering_itemModelClass.getTraderID()).child(request.getRequestId()).setValue(request);
                String messageKey=ChatfDBR.child(request.getChat_id()).push().getKey();
                ChatfDBR.child(request.getChat_id()).child(messageKey).setValue(new MessageModelClass(messageKey,request.getMessage(),Offering_itemModelClass.getTraderID(), Calendar.getInstance().getTime().toString()));
                Intent intent=new Intent(CustomRequestActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // showing item ofter being selected in spinner
    private void setSelectedItem(TradeItemModelClass offering_itemModelClass) {
        oName.setText(offering_itemModelClass.getName());
        oCond.setText("Condition: "+offering_itemModelClass.getCondition());
        oQuantity.setText("Quantity: "+offering_itemModelClass.getQuantity());
        Picasso.get().load(offering_itemModelClass.getImageUrl()).into(Offered_ItemImage);
    }
    // showing item ofter being selected in spinner
    private void setSelectedRequestedItem(TradeItemModelClass offering_itemModelClass) {
        rName.setText(Requsting_itemModelClass.getName());
        rCond.setText("Condition: "+Requsting_itemModelClass.getCondition());
        rQuantity.setText("Quantity: "+Requsting_itemModelClass.getQuantity());
        Picasso.get().load(Requsting_itemModelClass.getImageUrl()).into(RitemImage);
    }

    //setting data of items names in spinner and list of items
    private void setTeamsList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query=database.getReference("trade_items").orderByChild("traderID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot .exists()){
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        TradeItemModelClass t=d.getValue(TradeItemModelClass.class);
                        if(myItems.add(t)) {
                            myItemsName.add(t.getName());
                        }

                        Log.d("TAG", "Condition is: " + t.getName());
                    }
                }else{
                    Log.d("TAG", "Condition is: null snapshot");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Unable to read. ", error.toException());
            }
        });
        ArrayAdapter<String> adapter1=new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,myItemsName);
        spinner.setAdapter(adapter1);
    }

    //setting data of items names in spinner and list of items
    private void setOtherUsersItemsName() {
        Log.d("TAG", "setOtherUsersItemsName: "+user_twoID);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query=database.getReference("trade_items").orderByChild("traderID").equalTo(user_twoID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot .exists()){
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        TradeItemModelClass t=d.getValue(TradeItemModelClass.class);
                        if(otherUsersItems.add(t)) {
                            otherUsersItemsName.add(t.getName());
                        }

                        Log.d("TAG", "Condition is: " + t.getName());
                    }
                }else{
                    Log.d("TAG", "Condition is: null snapshot");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Unable to read.", error.toException());
            }
        });
        ArrayAdapter<String> adapter2=new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,otherUsersItemsName);
        requestedSpinner.setAdapter(adapter2);
    }

}