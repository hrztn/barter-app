package com.example.barterapp.screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.R;
import com.example.barterapp.model.TradeReqModelClass;
import com.example.barterapp.modules.ManageRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ManageRequestsFragment extends Fragment {
    RecyclerView manageRequestsRV;
    ManageRequestAdapter adapter;
    ManageRequestAdapter Sentadapter;
    Button Receivedsbutton,send_button;
    ArrayList<TradeReqModelClass> requestList=new ArrayList<>();
    ArrayList<TradeReqModelClass> requestsentList=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_request, container, false);
        manageRequestsRV=root.findViewById(R.id.manageR_recyclerView);
        Receivedsbutton=root.findViewById(R.id.Receivedsbutton);
        send_button=root.findViewById(R.id.send_button);
        manageRequestsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false ));
        adapter=new ManageRequestAdapter(getContext(),requestList);
        Sentadapter=new ManageRequestAdapter(getContext(),requestsentList);
        manageRequestsRV.setAdapter(adapter);
        Receivedsbutton.setEnabled(false);
        getData();
        Receivedsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageRequestsRV.setAdapter(adapter);
                Receivedsbutton.setEnabled(false);
                send_button.setEnabled(true);

            }
        });
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageRequestsRV.setAdapter(Sentadapter);
                Receivedsbutton.setEnabled(true);
                send_button.setEnabled(false);

            }
        });
        return root;
    }

    public void getData(){

        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("requests").child(userId);
        Query getpost = reference;
        getpost.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TradeReqModelClass mTradeRequestModelClass = dataSnapshot.getValue(TradeReqModelClass.class);
                if(mTradeRequestModelClass.getOffered_Item().getTraderID().equals(userId)){
                    requestsentList.add(mTradeRequestModelClass);
                    Sentadapter.notifyDataSetChanged();
                }else {
                    if (requestList.add(mTradeRequestModelClass)) {
                        adapter.notifyDataSetChanged();
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


    }

}
