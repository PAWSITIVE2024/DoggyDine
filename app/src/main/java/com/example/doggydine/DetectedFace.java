package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetectedFace extends AppCompatActivity {
    TextView detectedName;
    TextView metabolic_rate, one_day_cal, once_cal, one_day_food, once_food, left_food, going_food;
    Button no_btn, ok_btn;
    CircleImageView dog_image;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,mDatabaseRef;
    private String activeRate;
    private String dogWeight;
    private String feedingNum;
    private String dogFood;
    private String foodKcal;
    private double kcal;
    private double Target_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detected_face);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dog_image = findViewById(R.id.detected_face);
        detectedName = findViewById(R.id.detected_name);
        metabolic_rate = findViewById(R.id.tx_metabolic_rate);
        one_day_cal = findViewById(R.id.tx_one_day_cal);
        once_cal = findViewById(R.id.tx_once_cal);
        one_day_food = findViewById(R.id.tx_one_day_food);
        once_food = findViewById(R.id.tx_once_food);
        left_food = findViewById(R.id.tx_left_food);
        going_food = findViewById(R.id.tx_going_food);
        ok_btn = findViewById(R.id.btn_ok);
        no_btn = findViewById(R.id.btn_no);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        fetchDetectedName();
        fetchProfileImage();

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialog dialog = new AppCompatDialog(DetectedFace.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.detecting);
                dialog.setCancelable(true);

                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_detecting_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();

                dialog.show();

                mFirebaseAuth = FirebaseAuth.getInstance();
                String uid = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("Detected");

                Map<String, Object> updates = new HashMap<>();
                updates.put("start", true);
                updates.put("Detected_name", "");

                databaseReference.updateChildren(updates);
                databaseReference.child("Detected_name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String detectedName = snapshot.getValue(String.class);
                        if (detectedName != null && !detectedName.isEmpty()) {
                            Intent intent = new Intent(DetectedFace.this, DetectedFace.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리 로직
                        Toast.makeText(DetectedFace.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth = FirebaseAuth.getInstance();
                String uid = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("Detected");

                Map<String, Object> updates = new HashMap<>();
                updates.put("Target_weight", Target_weight);

                databaseReference.updateChildren(updates);
                Intent intent = new Intent(DetectedFace.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void fetchDetectedName() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        databaseReference.child("DoggyDine").child("UserAccount").child(uid).child("Detected").child("Detected_name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 데이터를 가져와서 TextView에 설정
                String name = dataSnapshot.getValue(String.class);
                detectedName.setText(name);
                fetchPetDetails(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
                detectedName.setText("");
            }
        });
        databaseReference.child("DoggyDine").child("UserAccount").child(uid).child("Detected").child("Current_weight").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String current_weight = dataSnapshot.getValue(String.class);
                left_food.setText(current_weight);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
                left_food.setText("");
            }
        });
    }
    private void fetchPetDetails(String detectedName) {
        if (detectedName != null) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            String uid = mFirebaseAuth.getCurrentUser().getUid();
            databaseReference.child("DoggyDine").child("UserAccount").child(uid).child("pet").child(detectedName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        activeRate = dataSnapshot.child("active_rate").getValue(String.class);
                        dogWeight = dataSnapshot.child("dog_weight").getValue(String.class);
                        feedingNum = dataSnapshot.child("feeding_num").getValue(String.class);
                        dogFood = dataSnapshot.child("dog_food").getValue(String.class);

                        fetchFoodDetails(dogFood);
                        calculateMetabolicRateAndCalories();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    activeRate = "";
                    dogWeight = "";
                    feedingNum = "";
                }
            });
        }
    }
    private void fetchFoodDetails(String dogFood) {
        if (dogFood != null) {
            databaseReference.child("DoggyDine").child("Food").child(dogFood).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        foodKcal = dataSnapshot.child("kcal").getValue(String.class);
                        kcal = Double.parseDouble(foodKcal) / 100.0;
                        calculateMetabolicRateAndCalories();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                    foodKcal = "";
                }
            });
        }
    }
    private void fetchProfileImage() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        databaseReference.child("DoggyDine").child("UserAccount").child(uid).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Profile 이미지 URL 데이터를 가져와서 CircleImageView에 설정
                String profileImageUrl = dataSnapshot.getValue(String.class);
                if (profileImageUrl != null) {
                    Glide.with(DetectedFace.this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_launcher_background) // 로딩 중 보여줄 기본 이미지
                            .error(R.drawable.ic_launcher_background) // 에러 발생 시 보여줄 기본 이미지
                            .into(dog_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
                dog_image.setImageResource(R.drawable.ic_launcher_background);
            }
        });
    }
    private void calculateMetabolicRateAndCalories() {
        if (dogWeight != null && activeRate != null) {
            try {
                double weight = Double.parseDouble(dogWeight);
                double activityRate = Double.parseDouble(activeRate);

                double metabolicRate;
                if (weight >= 2 && weight <= 45) {
                    metabolicRate = 30 * weight + 70;
                } else {
                    metabolicRate = 70 * Math.pow(weight, 0.75);
                }
                double oneDayCal = metabolicRate * activityRate;
                double onceCal = oneDayCal / Double.parseDouble(feedingNum);
                double oneDayFood = oneDayCal / kcal;
                double onceFood = oneDayFood / Double.parseDouble(feedingNum);
                double current_weight = Double.parseDouble(left_food.getText().toString());
                Target_weight = onceFood - current_weight;

                metabolic_rate.setText(String.format("%.2f", metabolicRate));
                one_day_cal.setText(String.format("%.2f", oneDayCal));
                once_cal.setText(String.format("%.2f", onceCal));
                one_day_food.setText(String.format("%.2f", oneDayFood));
                once_food.setText(String.format("%.2f", onceFood));
                going_food.setText(String.format("%.2f", Target_weight));
            } catch (NumberFormatException e) {
                metabolic_rate.setText("");
                one_day_cal.setText("");
            }
        }
    }
}