package com.example.barterapp.screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.R;
import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.TradeModelClass;
import com.example.barterapp.modules.OngoingTradesRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class OngoingFragment extends Fragment {
    RecyclerView currentRecyclerView;
    OngoingTradesRecyclerAdapter adapter;
    ArrayList<TradeModelClass> trades=new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ongoing, container, false);
        currentRecyclerView=root.findViewById(R.id.currentRecyclerView);
        currentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new OngoingTradesRecyclerAdapter(getContext(),trades);
        getTrades(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return root;
    }

    private void getTrades(String userID) {

        DatabaseReference fbdR= FirebaseDatabase.getInstance().getReference("trades");
        Query getpost =fbdR;
        getpost.addChildEventListener(new ChildEventListener() {
            ArrayList<TradeItemModelClass> mItems = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("test", "onChildAdded "+s);

//                mposts.clear();
//                for (DataSnapshot child : dataSnapshot.getChildren()){
                TradeModelClass mTradeModelClass = dataSnapshot.getValue(TradeModelClass.class);

                Log.d("test", ""+mTradeModelClass.getTime());
                if(!mTradeModelClass.isCanceled()){
                    if ((mTradeModelClass.getUserTwoId().equals(userID) || mTradeModelClass.getUserOneId().equals(userID) )&&
                            !(mTradeModelClass.isCompletedByuserTwo()&&mTradeModelClass.isCompletedByuserOne())){
                        if (trades.add(mTradeModelClass)) {
                            adapter.notifyDataSetChanged();
                            Log.d("test", "added");
                        }
                    }
                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("test", "onChildChanged");


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
        adapter=new OngoingTradesRecyclerAdapter(getContext(),trades);
        currentRecyclerView.setAdapter(adapter);

    }
    @Override
    public void onResume() {
        super.onResume();
        trades.clear();

    }
}
