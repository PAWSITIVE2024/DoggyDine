package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodCompare extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_compare);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.fc_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        databaseReference = database.getReference("DoggyDine").child("Food");


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userCheckRef = FirebaseDatabase.getInstance().getReference("DoggyDine")
                .child("UserAccount").child(userID).child("check");

        userCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot checkSnapshot : dataSnapshot.getChildren()) {

                    Boolean checkValue = checkSnapshot.getValue(Boolean.class);
                    if (checkValue != null && checkValue) {
                        String foodName = checkSnapshot.getKey();

                        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("DoggyDine")
                                .child("Food").child(foodName);
                        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                                if (foodSnapshot.exists()) {
                                    Food food = foodSnapshot.getValue(Food.class);
                                    if (food != null) {
                                        arrayList.add(food);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new FoodCompareAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FoodCompare.this, Feeding.class));
        finish();
    }
}
