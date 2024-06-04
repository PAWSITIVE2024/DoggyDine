package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd;
    public static LottieComposition mainAnimationComposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LottieAnimationView lottieAnimationView = findViewById(R.id.Lt_grass);
        lottieAnimationView.setAnimation(R.raw.grass); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();


        LottieAnimationView lottieAnimationView2 = findViewById(R.id.Lt_dog_1);
        lottieAnimationView2.setAnimation(R.raw.dog_sitting_1); // .json 파일을 로드
        lottieAnimationView2.loop(true);
        lottieAnimationView2.playAnimation();

        // 애니메이션을 미리 로드하여 캐시합니다.
        LottieCompositionFactory.fromRawRes(this, R.raw.background_main).addListener(composition -> {
            mainAnimationComposition = composition;
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");

        mEtEmail = findViewById(R.id.editTextText);
        mEtPwd = findViewById(R.id.editTextNumberPassword);

        Button btn_login = findViewById(R.id.go_to_login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatDialog dialog = new AppCompatDialog(Login.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(true);

                // 다이얼로그의 레이아웃에서 LottieAnimationView 가져오기
                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_loading_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();

                dialog.show();
                // 로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.go_to_register_btn);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입으로 이동
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });
    }
}
