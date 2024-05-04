package com.example.doggydine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd, mName;

    private ImageView mImageview;
    private Button mBtnRegister;
    private TextView mTextview;
    private TextView alertText;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;


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
        mImageview = findViewById(R.id.imageView);
        mTextview = findViewById(R.id.Tv_s_go);
        alertText = findViewById(R.id.tv_alert_email);

        LottieAnimationView lottieAnimationView = findViewById(R.id.Lt_grass);
        lottieAnimationView.setAnimation(R.raw.grass); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();



        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_up.this, ActivateRate.class);
                startActivity(intent);
            }
        });

        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mEtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    checkEmailDuplicate();
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                            // 이미지 업로드 후 URL 가져오기
                            uploadImageToFirebase(selectedImageUri).addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (task.isSuccessful()) {
                                        String imageUrl = task.getResult();
                                        UserAccount account = new UserAccount();
                                        // 여기서 유저 id, 비밀번호, token 생성
                                        account.setIdToken(firebaseUser.getUid());
                                        account.setEmailId(firebaseUser.getEmail());
                                        account.setPassword(strPwd);
                                        account.setUsername(strName);
                                        account.setProfile(imageUrl);

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
                                        Toast.makeText(Sign_up.this, "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Sign_up.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
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


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            mImageview.setImageURI(selectedImageUri);
        }
    }

    private Task<String> uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // 사용자의 UID를 기반으로 이미지 파일 이름 생성
        String imageFileName = firebaseUser.getUid() + ".jpg";

        // 이미지 파일 이름으로 새로운 참조 생성
        StorageReference imageRef = storageRef.child(imageFileName);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // 이미지 업로드가 성공하면 URL을 가져와서 문자열로 반환
            return imageRef.getDownloadUrl().continueWith(taskUri -> {
                if (!taskUri.isSuccessful()) {
                    throw taskUri.getException();
                }
                return taskUri.getResult().toString();
            });
        });
    }
}
