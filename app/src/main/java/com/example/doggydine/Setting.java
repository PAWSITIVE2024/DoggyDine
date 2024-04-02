package com.example.doggydine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    private CircleImageView circleImageView;
    private EditText etName, etAge, etWeight, etActiveRate, etAllergy;
    private TextView tvGo;
    private String imageUrl = null;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");
        circleImageView = findViewById(R.id.circle_profile);
        etName = findViewById(R.id.Et_setting_name);
        etAge = findViewById(R.id.Et_setting_age);
        etWeight = findViewById(R.id.Et_setting_weight);
        etActiveRate = findViewById(R.id.Et_setting_ar);
        etAllergy = findViewById(R.id.Et_setting_allergy);
        tvGo = findViewById(R.id.Tv_setting_go);

        String uid = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseRef.child("UserAccount").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // 캐시 사용 안 함
                            .skipMemoryCache(true); // 메모리에 저장된 이미지도 사용 안 함

                    Glide.with(Setting.this)
                            .load(userAccount.getProfile())
                            .apply(requestOptions)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(circleImageView);
                    etName.setText(userAccount.getDog_name());
                    etAge.setText(userAccount.getDog_age());
                    etWeight.setText(userAccount.getDog_weight());
                    etActiveRate.setText(userAccount.getActive_rate());
                    etAllergy.setText(userAccount.getAllergy());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = etName.getText().toString().trim();
                String strAge = etAge.getText().toString().trim();
                String strWeight = etWeight.getText().toString().trim();
                String strActiveRate = etActiveRate.getText().toString().trim();
                String strAllergy = etAllergy.getText().toString().trim();

                String uid = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference userRef = mDatabaseRef.child("UserAccount").child(uid);

                // 사용자 정보 업데이트
                userRef.child("dog_name").setValue(strName);
                userRef.child("dog_age").setValue(strAge);
                userRef.child("dog_weight").setValue(strWeight);
                userRef.child("active_rate").setValue(strActiveRate);
                userRef.child("allergy").setValue(strAllergy);
                if (imageUrl != null) {
                    userRef.child("profile").setValue(imageUrl);
                }
                Toast.makeText(Setting.this, "수정 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            circleImageView.setImageURI(selectedImageUri);
            uploadImageToFirebase(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference imageRef = storageRef.child(firebaseUser.getUid() + ".jpg");

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(this, new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        imageUrl = downloadUri.toString();

                        // 이미지 URL을 사용하여 프로필 이미지 업데이트
                        Glide.with(Setting.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(circleImageView);

                        // Glide 캐시 삭제
                        Glide.get(Setting.this).clearDiskCache();
                        Glide.get(Setting.this).clearMemory();
                    }
                } else {
                    Toast.makeText(Setting.this, "사진 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
