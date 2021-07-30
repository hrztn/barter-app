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
import com.example.barterapp.modules.LoadSpinner;
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

public class RequestTradeActivity extends AppCompatActivity {
    TradeItemModelClass Requsting_itemModelClass=new TradeItemModelClass();
    TradeItemModelClass Offering_itemModelClass=new TradeItemModelClass();
    Spinner spinner;
    ArrayList<String> myItemsName=new ArrayList<>();
    ArrayList<TradeItemModelClass> myItems=new ArrayList<>();
    LinearLayout OItemView;
    ImageView RitemImage,OItemImage;
    TextView rName,oName,rCond,oCond,oQuantity,rQuantity;
    EditText messageET;
    Button request_send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_trade);
        getSupportActionBar().hide();

        Requsting_itemModelClass=(TradeItemModelClass) getIntent().getSerializableExtra("item");
        myItemsName.add("Select Your Item");
        spinner = findViewById(R.id.spinner);
        OItemView=findViewById(R.id.offeredItemView);

        initView();
        setTeamsList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    OItemView.setVisibility(View.VISIBLE);
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
    }

    private void initView() {
        request_send_button = findViewById(R.id.request_send_button);
        RitemImage = findViewById(R.id.requestItemImageView);
        OItemImage = findViewById(R.id.offeredItemImageView);
        rName = findViewById(R.id.requestItemName);
        oName = findViewById(R.id.offereditem_name);
        rCond = findViewById(R.id.requested_cond_textViewD);
        oCond = findViewById(R.id.offered_cond_textViewD);
        oQuantity = findViewById(R.id.offered_quantity_textView_D);
        rQuantity = findViewById(R.id.requested_quantity_textView_D);
        messageET = findViewById(R.id.requestMessage);

        rName.setText(Requsting_itemModelClass.getName());
        rCond.setText("Condition : "+Requsting_itemModelClass.getCondition());
        rQuantity.setText("Quantity : "+Requsting_itemModelClass.getQuantity());
        Picasso.get().load(Requsting_itemModelClass.getImageUrl()).into(RitemImage);

        request_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageET.getText().toString().isEmpty()){
                    messageET.setError("Please write a message. ");
                    return;
                }
                if (spinner.getSelectedItemPosition()==0){
                    Toast.makeText(RequestTradeActivity.this,"Choose Your Item To Trade. ",Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(RequestTradeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setSelectedItem(TradeItemModelClass offering_itemModelClass) {
        oName.setText(offering_itemModelClass.getName());
        oCond.setText("Condition : "+offering_itemModelClass.getCondition());
        oQuantity.setText("Quantity : "+offering_itemModelClass.getQuantity());
        Picasso.get().load(offering_itemModelClass.getImageUrl()).into(OItemImage);
    }

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

                        Log.d("TAG", "Item is: " + t.getName());
                    }
                }else{
                    Log.d("TAG", "Item is: null snapshot");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read item.", error.toException());
            }
        });
        ArrayAdapter<String> adapter1=new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item,myItemsName);
        spinner.setAdapter(adapter1);
    }

}