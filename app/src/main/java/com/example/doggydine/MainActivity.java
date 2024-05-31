package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

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

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");
        CircleImageView settingBtn = findViewById(R.id.setting_btn);

        ImageButton feed_btn = findViewById(R.id.feed_btn);
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        Button detecting_btn = findViewById(R.id.btn_detecting);

        detecting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetectedFace.class);
                startActivity(intent);
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
