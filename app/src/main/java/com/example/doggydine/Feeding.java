package com.example.doggydine;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Feeding extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList;
    private FirebaseDatabase database;
    private Spinner mspinner;
    private DatabaseReference databaseReference;
    private ImageView mImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feeding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView =findViewById(R.id.recyclerView);
        mImageview = findViewById(R.id.Iv_go_home);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("DoggyDine").child("Food");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Food food = new Food();
                    food.setProfile(snapshot.child("profile").getValue(String.class));
                    food.setName(snapshot.child("name").getValue(String.class));
                    food.setScore(snapshot.child("score").getValue(String.class));
                    food.setPrice(snapshot.child("price").getValue(String.class));

                    Map<String, Boolean> materialMap = new HashMap<>();
                    DataSnapshot materialSnapshot = snapshot.child("material");
                    for (DataSnapshot materialChild : materialSnapshot.getChildren()) {
                        Boolean isTrue = materialChild.getValue(Boolean.class);
                        if (isTrue) {
                            materialMap.put(materialChild.getKey(), isTrue);
                        }
                    }
                    food.setMaterial(materialMap);

                    arrayList.add(food);
                    // 데이터 잘가져오는지 확인하기위해서 로그 확인용부분 나중에 없애도 됨!
                    Log.d("FirebaseData", food.toString());

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CustomAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);


        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feeding.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button recommend_btn = (Button) findViewById(R.id.recommend_btn);
        recommend_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Recommend.class);
                startActivity(intent);
            }
        });

        // Spinner에서 선택된 정렬 방법을 감지하는 리스너 구현
        mspinner = findViewById(R.id.spinner);
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1 ) { // 가격 순 선택
                    Collections.sort(arrayList, new Comparator<Food>() {
                        @Override
                        public int compare(Food food1, Food food2) {
                            // 가격이 null이면 두 음식은 동등하다고 간주
                            if (food1.getPrice() == null && food2.getPrice() == null) {
                                return 0;
                            } else if (food1.getPrice() == null) {
                                return 1;
                            } else if (food2.getPrice() == null) {
                                return -1;
                            }
                            // 문자열을 Double로
                            return Double.compare(Double.parseDouble(food1.getPrice()), Double.parseDouble(food2.getPrice()));
                        }
                    });
                } else if (position == 2) {

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때의 처리
            }
        });




        Button camera_btn = (Button) findViewById(R.id.barcode_btn);

        /*
        camera_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), GetCamera.class);
                startActivity(intent);
            }
        });*/
    }
}