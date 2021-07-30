package com.example.barterapp.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.barterapp.LoginActivity;
import com.example.barterapp.R;
import com.example.barterapp.SearchItemActivity;
import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.modules.ItemServerAdapter;
import com.example.barterapp.modules.MarketPlaceViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class marketplace_screen extends Fragment {
    RecyclerView itemsRecyclerView;
    ItemServerAdapter adapter;
    Spinner filterSpinner;
    private ArrayList<TradeItemModelClass> ItemsList = new ArrayList<>();
    String[] catagorys = new String[]{"All", "Wearable", "Electrical", "Sports",
            "Kitchen", "Home Deco", "Consumable"};

    public marketplace_screen() {

    }

    //private TabLayout tab;
    //private ViewPager pager;

    private EditText martket_search;
    private TextView market_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace, container, false);

        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerViewmain);
        filterSpinner = view.findViewById(R.id.catagory_filter);
        itemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ItemServerAdapter(getContext(), ItemsList);
        itemsRecyclerView.setAdapter(adapter);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(getContext(),
                R.layout.row_spinner, catagorys);
        filterSpinner.setAdapter(spinnerAdapter);
        getdata();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String s = filterSpinner.getSelectedItem().toString();
                Log.d("tester", "onItemSelected: " +s);
                if (s.equals("All")) {
                    s = "";
                }
                if (adapter != null) {

                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //cast view
        //tab = view.findViewById(R.id.market_tabLayout);
        //pager = view.findViewById(R.id.market_viewPager);
        martket_search = view.findViewById(R.id.market_searchBar);
        market_logout = view.findViewById(R.id.market_logout);

        //MarketPlaceViewPager adapter = new MarketPlaceViewPager(getFragmentManager(), 1);
        //adapter.addFragment(tab_kitchen_screen.getInstance(), "Kitchen");
        //adapter.addFragment(tab_consumable_screen.getInstance(), "Consumable");
        //adapter.addFragment(tab_sports_screen.getInstance(), "Sports");
        //adapter.addFragment(tab_wearable_screen.getInstance(), "Wearable");
        //adapter.addFragment(tab_homedeco_screen.getInstance(), "Deco");
        //adapter.addFragment(tab_electrical_screen.getInstance(), "Electrical");

        //pager.setAdapter(adapter);

        //tab.setupWithViewPager(pager);

        martket_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable arg0) {


            }
        });

        market_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });


        return view;
    }

    public void getdata() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trade_items");
        Query getpost = reference;
        getpost.addChildEventListener(new ChildEventListener() {
            ArrayList<TradeItemModelClass> mItems = new ArrayList<>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                TradeItemModelClass mTradeItemModelClass = dataSnapshot.getValue(TradeItemModelClass.class);

                if (!mTradeItemModelClass.getTraderID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {


                    if (ItemsList.add(mTradeItemModelClass)) {
                        adapter.notifyDataSetChanged();
                    }
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
        adapter = new ItemServerAdapter(getContext(), ItemsList);

        itemsRecyclerView.setAdapter(adapter);

    }

}
