package com.example.barterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.modules.ItemServerAdapter;
import com.example.barterapp.screen.marketplace_screen;
import com.example.barterapp.screen.profile_screen;
import com.example.barterapp.screen.trades_screen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();


        BottomNavigationView navView = findViewById(R.id.db_btmNav);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        marketplace_screen marketScreen = new marketplace_screen();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.db_frame, marketScreen).commit();



    }

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){

                case R.id.btm_marketplace:

                    marketplace_screen marketScreen = new marketplace_screen();
                    FragmentManager marketmanager = getSupportFragmentManager();
                    marketmanager.beginTransaction().replace(R.id.db_frame, marketScreen).commit();


                    return true;




                case R.id.btm_userProfile:

                    profile_screen profileScreen = new profile_screen();
                    FragmentManager profileManager = getSupportFragmentManager();
                    profileManager.beginTransaction().replace(R.id.db_frame, profileScreen).commit();
                    return true;

            }

            return false;
        }
    };



}