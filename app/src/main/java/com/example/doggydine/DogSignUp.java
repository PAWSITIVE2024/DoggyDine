package com.example.doggydine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DogSignUp extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText  mName, mAge, mWeight, mActiveRate, mAllergy;
    private ImageView mImageview,mImageview2,mImageview3,mImageview4,mImageview5;
    private Button mBtnRegister;
    private TextView mTextview;

    private Uri selectedImageUri;
    private Uri selectedImageUrl_1;
    private Uri selectedImageUrl_2;
    private Uri selectedImageUrl_3;
    private Uri selectedImageUrl_4;
    private Uri selectedImageUrl_5;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine");

        mName = findViewById(R.id.Et_d_s_name);
        mAge = findViewById(R.id.Et_d_s_age);
        mWeight = findViewById(R.id.Et_d_s_weight);
        mActiveRate = findViewById(R.id.Et_d_s_allergy);

        mImageview = findViewById(R.id.imageView);
        mImageview2 = findViewById(R.id.imageView2);
        mImageview3 = findViewById(R.id.imageView3);
        mImageview4 = findViewById(R.id.imageView4);
        mImageview5 = findViewById(R.id.imageView5);

        mBtnRegister = findViewById(R.id.Btn_d_s_register);
        mTextview = findViewById(R.id.Tv_d_s_go);

        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogSignUp.this, ActivateRate.class);
                startActivity(intent);
            }
        });
        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        mImageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryForImageView2();
            }
        });
        mImageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryForImageView3();
            }
        });
        mImageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openGalleryForImageView4();}
        });
        mImageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryForImageView5();
            }
        });




    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openGalleryForImageView2() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST + 1);
    }

    private void openGalleryForImageView3() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST + 2);
    }

    private void openGalleryForImageView4() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST + 3);
    }

    private void openGalleryForImageView5() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST + 4);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // 각 이미지뷰에 대한 처리
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    mImageview.setImageURI(selectedImageUri);
                    selectedImageUrl_1 = selectedImageUri;
                    break;
                case PICK_IMAGE_REQUEST + 1:
                    mImageview2.setImageURI(selectedImageUri);
                    selectedImageUrl_2 = selectedImageUri;
                    break;
                case PICK_IMAGE_REQUEST + 2:
                    mImageview3.setImageURI(selectedImageUri);
                    selectedImageUrl_3 = selectedImageUri;
                    break;
                case PICK_IMAGE_REQUEST + 3:
                    mImageview4.setImageURI(selectedImageUri);
                    selectedImageUrl_4 = selectedImageUri;
                    break;
                case PICK_IMAGE_REQUEST + 4:
                    mImageview5.setImageURI(selectedImageUri);
                    selectedImageUrl_5 = selectedImageUri;
                    break;
            }
        }
    }


}