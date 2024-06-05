package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    public static LottieComposition mainAnimationComposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LottieAnimationView lottieAnimationView_back = findViewById(R.id.lottieAnimationView_back);

        // 캐시된 애니메이션을 설정합니다.
        if (Login.mainAnimationComposition != null) {
            lottieAnimationView_back.setComposition(Login.mainAnimationComposition);
        } else {
            // 만약 캐시된 애니메이션이 없으면 다시 로드합니다.
            lottieAnimationView_back.setAnimation(R.raw.background_main);
        }

        lottieAnimationView_back.loop(true);
        lottieAnimationView_back.playAnimation();

        CircleImageView settingBtn = findViewById(R.id.setting_btn);

        ImageButton feed_btn = findViewById(R.id.feed_btn);
        Button detecting_btn = findViewById(R.id.btn_detecting);

        detecting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialog dialog = new AppCompatDialog(MainActivity.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(true);

                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_loading_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();

                dialog.show();

                mFirebaseAuth = FirebaseAuth.getInstance();
                String uid = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("Detected");

                // 업데이트할 데이터 설정
                Map<String, Object> updates = new HashMap<>();
                updates.put("start", true);
                updates.put("Detected_name", null);

                // 해당 항목만 업데이트
                databaseReference.updateChildren(updates);
                databaseReference.child("Detected_name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String detectedName = snapshot.getValue(String.class);
                        if (detectedName != null && !detectedName.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, DetectedFace.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리 로직
                        Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });

        feed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Feeding.class);
                startActivity(intent);
            }
        });

        ImageButton chatbot_btn = findViewById(R.id.chatbot_btn);
        chatbot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatBot.class);
                startActivity(intent);
            }
        });

        ImageButton calender_btn = findViewById(R.id.calender_btn);
        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calender.class);
                startActivity(intent);
            }
        });
    }
}
