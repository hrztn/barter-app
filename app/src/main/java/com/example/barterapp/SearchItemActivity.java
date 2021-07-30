package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.modules.GridItemDecorationClass;
import com.example.barterapp.modules.ItemServerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {
    RecyclerView itemsRecyclerView;
    ItemServerAdapter adapter;
    Spinner filterSpinner;
    private ArrayList<TradeItemModelClass> ItemsList = new ArrayList<>();
    String[] catagorys = new String[]{"All", "Wearable", "Electrical", "Sports",
            "Kitchen", "Home Deco", "Consumable"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        itemsRecyclerView=findViewById(R.id.itemsRecyclerViewmain2);
        itemsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter=new ItemServerAdapter(this,ItemsList);

        filterSpinner = findViewById(R.id.catagory_filter2);
        itemsRecyclerView.setAdapter(adapter);
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter(this,
                R.layout.row_spinner,catagorys);
        filterSpinner.setAdapter(spinnerAdapter);
        getdata();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String s = filterSpinner.getSelectedItem().toString();
                Log.d("tester", "onItemSelected: " + s);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchiten=menu.findItem(R.id.search_option);
        SearchView searchView=(SearchView) searchiten.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter!=null) {

                    adapter.getFilter().filter(s);
                    return true;
                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_option:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        adapter = new ItemServerAdapter(this, ItemsList);

        itemsRecyclerView.setAdapter(adapter);

    }



}