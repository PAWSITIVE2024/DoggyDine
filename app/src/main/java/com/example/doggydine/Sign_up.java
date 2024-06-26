package com.example.doggydine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.airbnb.lottie.LottieAnimationView;
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

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd, mName;
    private Button mBtnRegister;
    private TextView alertText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");

        mEtEmail = findViewById(R.id.Et_s_id);
        mEtPwd = findViewById(R.id.Et_s_pw);
        mName = findViewById(R.id.Et_s_name);
        mBtnRegister = findViewById(R.id.Btn_s_register);
        alertText = findViewById(R.id.tv_alert_email);

        LottieAnimationView lottieAnimationView = findViewById(R.id.Lt_grass);
        lottieAnimationView.setAnimation(R.raw.grass); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        mEtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    checkEmailDuplicate();
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialog dialog = new AppCompatDialog(Sign_up.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(true); // 다이얼로그를 취소할 수 있도록 설정

                // 다이얼로그의 레이아웃에서 LottieAnimationView 가져오기
                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_loading_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();

                dialog.show();

                // 입력한 텍스트들 가져오기
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strName = mName.getText().toString();

                // 인증 절차 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            UserAccount account = new UserAccount();
                            // 여기서 유저 id, 비밀번호, token 생성
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            account.setUsername(strName);

                            // setValue: database에 insert
                            // UserModel에 담은 정보를 database에 set한다 (userid(token)을 key로)
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast toast = Toast.makeText(Sign_up.this, "회원가입 성공!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                            Intent intent = new Intent(Sign_up.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Sign_up.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    private void checkEmailDuplicate() {
        String email = mEtEmail.getText().toString().trim().toLowerCase(); // 이메일을 모두 소문자로 변환하여 비교합니다.
        mDatabaseRef.child("UserAccount").orderByChild("emailId").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 이미 사용 중인 이메일인 경우
                    updateUiEmailExists();
                } else {
                    // 사용 가능한 이메일인 경우
                    updateUiEmailAvailable();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 쿼리 취소 시 처리할 내용
                Toast.makeText(Sign_up.this, "이메일 중복 체크에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUiEmailExists() {
        alertText.setVisibility(View.VISIBLE);
        alertText.setText("이미 사용 중인 이메일입니다.");
    }

    private void updateUiEmailAvailable() {
        alertText.setVisibility(View.VISIBLE);
        alertText.setText("사용 가능한 이메일입니다.");
        alertText.setTextColor(Color.parseColor("#008000"));
    }
}
