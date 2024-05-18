package com.example.doggydine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mName, mPrice, mScore, mManu, mKcal, mMaterial,mMoisture,mOmega3,mOmega6,mPhosphorus,mProtein,mFiber,mFat,mAsh,mCalcium;
    private ImageView mProfile;
    private FirebaseDatabase database;
    private ImageButton mBack,mCompare_btn;
    private DatabaseReference databaseReference;
    private String foodName;
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
        mMoisture=findViewById(R.id.fd_moisture);
        mOmega3 = findViewById(R.id.fd_omega3);
        mOmega6 = findViewById(R.id.fd_omega6);
        mPhosphorus = findViewById(R.id.fd_phosphorus);
        mProtein = findViewById(R.id.fd_protein);
        mFiber = findViewById(R.id.fd_fiber);
        mAsh = findViewById(R.id.fd_ash);
        mFat = findViewById(R.id.fd_fat);
        mCalcium = findViewById(R.id.fd_calcium);
        mBack = findViewById(R.id.btn_back);
        mCompare_btn = findViewById(R.id.compare_btn);


        mCompare_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodName != null) {
                    database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference("DoggyDine").child("Food").child(foodName).child("check");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean checked = dataSnapshot.getValue(Boolean.class);
                            boolean newChecked = !checked;
                            databaseReference.setValue(newChecked);
                            if (newChecked == true) {
                                mCompare_btn.setImageResource(R.drawable.fullheart);
                                Toast.makeText(FoodDetailsActivity.this, "사료 선택 완료", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FoodDetailsActivity.this, "사료 선택 취소", Toast.LENGTH_SHORT).show();
                                mCompare_btn.setImageResource(R.drawable.emptyheart);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // 에러 처리
                        }
                    });
                }
            }
        });



        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodDetailsActivity.this, Feeding.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Intent를 통해 넘겨받은 음식 이름 가져옴
        Intent intent = getIntent();
        if (intent != null) {
            foodName = intent.getStringExtra("foodName");

            if (foodName != null) {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("DoggyDine").child("Food").child(foodName);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        Food food = datasnapshot.getValue(Food.class);
                        if (food != null) {
                            // mName의 텍스트 색상을 변경하는 코드
                            if (food != null) {
                                // 음식 이름 설정
                                mName.setText(food.getName());
                                if (food.getCheck()) {
                                    mName.setTextColor(Color.parseColor("#FEA443"));
                                } else {
                                    mName.setTextColor(Color.parseColor("#000000"));
                                }
                            }

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
                            // Nutrient 정보 가져와서 설정
                            Map<String, String> nutrientMap = food.getNutrient();
                            if (nutrientMap != null) {
                                mMoisture.setText("수분 : " + nutrientMap.get("moisture")+"%");
                                mOmega3.setText("오메가3 : " + nutrientMap.get("omega3")+"%");
                                mOmega6.setText("오메가6 : " + nutrientMap.get("omega6")+"%");
                                mPhosphorus.setText("인 : " + nutrientMap.get("phosphorus")+"%");
                                mProtein.setText("조단백질 : " + nutrientMap.get("protein")+"%");
                                mFiber.setText("조섬유 : " + nutrientMap.get("fiber")+"%");
                                mFat.setText("조지방 : " + nutrientMap.get("fat")+"%");
                                mAsh.setText("조회분 : " + nutrientMap.get("ash")+"%");
                                mCalcium.setText("칼슘 : " + nutrientMap.get("calcium")+"%");
                            }

                            // 프로필 이미지 로드
                            String profileImageUrl = food.getProfile();
                            if (profileImageUrl != null) {
                                if(!isDestroyed()){
                                    Glide.with(FoodDetailsActivity.this)
                                            .load(profileImageUrl)
                                            .fitCenter() // 이미지를 이미지 뷰에 맞추기 위해 중앙 잘라내기 사용
                                            .into(mProfile);
                                }
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
