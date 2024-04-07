package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity {
    private EditText mName, mPrice, mScore, mManu, mKcal, mMaterial;
    private ImageView mProfile;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        mName = findViewById(R.id.Et_d_name);
        mPrice = findViewById(R.id.Et_d_price);
        mScore = findViewById(R.id.Et_d_score);
        mManu = findViewById(R.id.Et_d_manu);
        mKcal = findViewById(R.id.Et_d_kcal);
        mMaterial = findViewById(R.id.Et_d_material);
        mProfile = findViewById(R.id.iv_d_profile);

        // Intent를 통해 넘겨받은 음식 이름 가져옴
        Intent intent = getIntent();
        if (intent != null) {
            String foodName = intent.getStringExtra("foodName");

            if (foodName != null) {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("DoggyDine").child("Food").child(foodName);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        Food food = datasnapshot.getValue(Food.class);
                        if (food != null) {
                            mName.setText(food.getName());
                            mPrice.setText("(100g당) "+food.getPrice()+"원");
                            mScore.setText(food.getScore());
                            mManu.setText(food.getManu());
                            mKcal.setText("(250기준) "+food.getKcal() + "Kcal");

                            // Material 정보 가져오기
                            StringBuilder materialText = new StringBuilder();
                            for (Map.Entry<String, Boolean> entry : food.getMaterial().entrySet()) {
                                if (entry.getValue()) {
                                    if (materialText.length() > 0) {
                                        materialText.append(", ");
                                    }
                                    materialText.append(entry.getKey());
                                }
                            }
                            mMaterial.setText(materialText.toString());

                            // 프로필 이미지 로드
                            String profileImageUrl = food.getProfile();
                            if (profileImageUrl != null) {
                                Glide.with(FoodDetailsActivity.this)
                                        .load(profileImageUrl)
                                        .fitCenter() // 이미지를 이미지 뷰에 맞추기 위해 중앙 잘라내기 사용
                                        .into(mProfile);
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
}
