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

        LottieAnimationView lottieAnimationView = findViewById(R.id.LT_s_dog_sleep);
        lottieAnimationView.setAnimation(R.raw.dog_sleep); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");
        CircleImageView settingBtn = (CircleImageView) findViewById(R.id.setting_btn);

        ImageButton feed_btn = (ImageButton)findViewById(R.id.feed_btn);
        Button mTest = (Button)findViewById(R.id.button_test) ;
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();


        //잠깐 test용으로 하나 만들었습니다!!
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, FoodCompare.class);
                startActivity(intent);
            }
        });


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Setting.class);
                startActivity(intent);

            }
        });
        feed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,Feeding.class);
                startActivity(intent);
            }
        });
        ImageButton chatbot_btn = (ImageButton) findViewById(R.id.chatbot_btn);
        chatbot_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ChatBot.class);
                startActivity(intent);
            }
        });
        ImageButton calender_btn = (ImageButton) findViewById(R.id.calender_btn);
        calender_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Calender.class);
                startActivity(intent);
            }
        });




    }
}