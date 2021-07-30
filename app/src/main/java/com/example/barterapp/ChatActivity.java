package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.barterapp.model.MessageModelClass;
import com.example.barterapp.modules.ChatRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatR_view;
    EditText messageTypingET;
    ImageButton sendMessageButton;
    ChatRecyclerViewAdapter chatAdapter;
    ArrayList<MessageModelClass> messages=new ArrayList<>();
    String chatID;
    String user_twoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        chatR_view=findViewById(R.id.chatrecyclerView);
        messageTypingET=findViewById(R.id.mssageeTChatmessage);
        sendMessageButton=findViewById(R.id.send_Chatmessagebutton);
        chatR_view.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter =new ChatRecyclerViewAdapter(this,messages);
        chatR_view.setAdapter(chatAdapter);
        chatID=getIntent().getStringExtra("chatid");
        user_twoID=getIntent().getStringExtra("otherId");

        getMessages(chatID);

        // send button click handling and sending data to firebase and adding item in the same chat through chat id
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageTypingET.getText().toString().isEmpty()){
                    messageTypingET.setHint("ENTER YOUR MESSAGE HERE.");
                    return;
                }
                String message=messageTypingET.getText().toString();
                String time= Calendar.getInstance().getTime().toString();
                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference fbdR= FirebaseDatabase.getInstance().getReference("chat").child(chatID);
                String messageKey=fbdR.push().getKey();
                fbdR.child(messageKey).setValue(new MessageModelClass(messageKey,message,userId,time));
                messageTypingET.setText("");
                messageTypingET.setHint("ENTER YOUR MESSAGE HERE.");
            }
        });

    }

    // this will load all messages of that chat with the help of the chat_it
    private void getMessages(String chatID) {

        DatabaseReference fbdR=FirebaseDatabase.getInstance().getReference("chat").child(chatID);
        Query getpost =fbdR;
        getpost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Hariz", "onChildAdded "+s);

//                mposts.clear();
//                for (DataSnapshot child : dataSnapshot.getChildren()){
                MessageModelClass mMessageModelClass = dataSnapshot.getValue(MessageModelClass.class);
                Log.d("ExpertFinder", ""+mMessageModelClass.getMessage());


                if (messages.add(mMessageModelClass)) {
                    chatAdapter.notifyDataSetChanged();
                    Log.d("ExpertFinder", "added");
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
        chatAdapter=new ChatRecyclerViewAdapter(this,messages);

        chatR_view.setAdapter(chatAdapter);

    }

    public void createCustomRequest(View view) {
        Intent intent =new Intent(this, CustomRequestActivity.class);
        intent.putExtra("user_twoID",user_twoID);
        startActivity(intent);
    }
}