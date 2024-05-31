package com.example.doggydine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
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
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectDogFood extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList;
    private Button btn_scan;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_dog_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.d_f_recyclerView);
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

                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Food food = new Food();
                    food.setProfile(snapshot.child("profile").getValue(String.class));
                    food.setName(snapshot.child("name").getValue(String.class));
                    food.setScore(snapshot.child("score").getValue(String.class));
                    food.setPrice(snapshot.child("price").getValue(String.class));
                    food.setSales_Volume(snapshot.child("sales_Volume").getValue(String.class));

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
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new SelectDogFoodAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        btn_scan = findViewById(R.id.barcode_btn);
        btn_scan.setOnClickListener(v -> scanCode());
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String scannedBarcode = result.getContents();
            findFoodByBarcode(scannedBarcode);
        }
    });

    private void findFoodByBarcode(String barcode) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String foodBarcode = snapshot.child("number").getValue(String.class);
                    if (foodBarcode != null && foodBarcode.equals(barcode)) {
                        String foodName = snapshot.child("name").getValue(String.class);
                        saveFoodNameToPreferences(foodName);
                        Toast.makeText(SelectDogFood.this, "사료 찾기 완료", Toast.LENGTH_SHORT).show();
                        found = true;
                        finish();
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(SelectDogFood.this, "해당 바코드로 된 사료를 찾을수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SelectDogFood.this, "해당 바코드로 된 사료를 찾을수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFoodNameToPreferences(String foodName) {
        SharedPreferences sharedPreferences = getSharedPreferences("FromSelectDogFood", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("foodName", foodName);
        editor.apply();
    }
}
