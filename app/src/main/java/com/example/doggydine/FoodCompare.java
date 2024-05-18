package com.example.doggydine;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    // 'check' 값이 true인 경우에만 데이터를 가져옴
                    Boolean check = snapshot.child("check").getValue(Boolean.class);
                    if (check != null && check) {
                        Food food = new Food();
                        food.setProfile(snapshot.child("profile").getValue(String.class));
                        food.setName(snapshot.child("name").getValue(String.class));
                        food.setScore(snapshot.child("score").getValue(String.class));
                        food.setPrice(snapshot.child("price").getValue(String.class));
                        food.setSales_Volume(snapshot.child("sales_Volume").getValue(String.class));
                        food.setManu(snapshot.child("manu").getValue(String.class));

                        Map<String, Boolean> materialMap = new HashMap<>();
                        DataSnapshot materialSnapshot = snapshot.child("material");
                        for (DataSnapshot materialChild : materialSnapshot.getChildren()) {
                            Boolean isTrue = materialChild.getValue(Boolean.class);
                            if (isTrue != null && isTrue) {
                                materialMap.put(materialChild.getKey(), isTrue);
                            }
                        }
                        food.setMaterial(materialMap);

                        // 영양소 설정
                        Map<String, String> nutrientMap = new HashMap<>();
                        DataSnapshot nutrientSnapshot = snapshot.child("nutrient");
                        for (DataSnapshot nutrientChild : nutrientSnapshot.getChildren()) {
                            String nutrientValue = nutrientChild.getValue(String.class);
                            nutrientMap.put(nutrientChild.getKey(), nutrientValue);
                        }
                        food.setNutrient(nutrientMap);
                        arrayList.add(food);
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new FoodCompareAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);
    }
}