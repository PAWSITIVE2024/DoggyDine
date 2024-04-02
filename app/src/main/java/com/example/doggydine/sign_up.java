package com.example.doggydine;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail,mEtPwd,mName,mAge,mWeight,mActiveRate,mAllergy;
    private ImageView mImageview;
    private TextView mtextview;
    private Button mBtnRegister;

    private static final int PICK_IMAGE_REQUEST = 1;
    private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");

        mEtEmail=findViewById(R.id.Et_s_id);
        mEtPwd = findViewById(R.id.Et_s_pw);
        mName = findViewById(R.id.Et_s_name);
        mAge = findViewById(R.id.Et_s_age);
        mWeight = findViewById(R.id.Et_s_weight);
        mActiveRate = findViewById(R.id.Et_s_ar);
        mAllergy = findViewById(R.id.Et_s_allergy);
        mtextview = findViewById(R.id.Tv_s_go);
        mBtnRegister = findViewById(R.id.Btn_s_register);
        mImageview = findViewById(R.id.imageView);

        mtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_up.this,ActivateRate.class);
                startActivity(intent);
            }
        });

        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //회원가입 시작

                //입력한 텍스트들 가져오기
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strName = mName.getText().toString();
                String strAge = mAge.getText().toString();
                String strWeight = mWeight.getText().toString();
                String strActiveRate = mActiveRate.getText().toString();
                String strAllergy = mAllergy.getText().toString();



                //인증절차 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            //여기서 유저 id , 비멀번호 , token 생성
                            //나중엔 여기에 다른 정보를 입력해줘야함
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);
                            account.setDog_name(strName);
                            account.setDog_age(strAge);
                            account.setDog_weight(strWeight);
                            account.setActive_rate(strActiveRate);
                            account.setAllergy(strAllergy);
                            account.setProfile(imageUrl);

                            //setvalue : database에 insert
                            //usermodel 에 담은 정보를 database에 set한다 (userid(token)을 key로)
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast toast = Toast.makeText(sign_up.this,"회원가입 성공!",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,0);
                            toast.show();
                            Intent intent = new Intent(sign_up.this, login.class);
                            startActivity(intent);
                            finish();


                        } else{
                            Toast.makeText(sign_up.this,"회원가입에 실패하셨습니다",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedImageUri = data.getData();
            mImageview.setImageURI(selectedImageUri);
            uploadImageToFirebase(selectedImageUri);
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference imageRef = storageRef.child(firebaseUser.getUid() + ".jpg");

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()){
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(sign_up.this, new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                }else{
                    Toast.makeText(sign_up.this,"사진저장실패",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
