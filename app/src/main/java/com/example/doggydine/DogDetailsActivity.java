package com.example.doggydine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DogDetailsActivity extends AppCompatActivity {
    private EditText mName,mWeight,mAllergy;
    private ImageView mImageview,mImageview2,mImageview3,mImageview4,mImageview5,dog_food;
    private Button mBtnRegister,mBtnDelete;
    private ImageButton activeButton,calendarButton;
    private TextView selectedDateTextView,dog_food_text,mActiveRate;
    private Calendar calendar;
    private String imageuri_1,imageuri_2,imageuri_3,imageuri_4,imageuri_5,dog_food_string;
    private Uri selectedImageUr1,selectedImageUr1_1,selectedImageUr1_2,selectedImageUr1_3,selectedImageUr1_4,selectedImageUr1_5;
    private int count = 1;
    private static final  int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mName = findViewById(R.id.Et_dt_name);
        mWeight = findViewById(R.id.Et_dt_weight);
        selectedDateTextView = findViewById(R.id.dt_selectedDateTextView);
        mActiveRate = findViewById(R.id.active_dt_text);
        dog_food_text = findViewById(R.id.dogfood_dt_text);
        mAllergy = findViewById(R.id.Et_dt_allergy);
        mImageview = findViewById(R.id.imageView);
        mImageview2 = findViewById(R.id.imageView2);
        mImageview3 = findViewById(R.id.imageView3);
        mImageview4 = findViewById(R.id.imageView4);
        mImageview5 = findViewById(R.id.imageView5);

        Intent intent = getIntent();
        if(intent != null){
            String dogName = intent.getStringExtra("dog_name");
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = firebaseUser.getUid();
            if(dogName != null) {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("DoggyDine").child("UserAccount").child(userId).child("pet").child(dogName);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        PetAccount pet = datasnapshot.getValue(PetAccount.class);
                        if(pet != null) {
                            mName.setText(pet.getDog_name());
                            mWeight.setText(pet.getDog_weight());
                            selectedDateTextView.setText(pet.getDog_age());
                            //활동수치 set 필요
                            dog_food_text.setText(pet.getDog_food());
                            mAllergy.setText(pet.getAllergy());
                            String profileImageUrl1 = pet.getProfile1();
                            String profileImageUrl2 = pet.getProfile2();
                            String profileImageUrl3 = pet.getProfile3();
                            String profileImageUrl4 = pet.getProfile4();
                            String profileImageUrl5 = pet.getProfile5();

                            if(profileImageUrl1 !=null){
                                Glide.with(DogDetailsActivity.this)
                                        .load(profileImageUrl1)
                                        .fitCenter()
                                        .into(mImageview);
                            }
                            if(profileImageUrl2 !=null){
                                Glide.with(DogDetailsActivity.this)
                                        .load(profileImageUrl2)
                                        .fitCenter()
                                        .into(mImageview2);
                            }
                            if(profileImageUrl3 !=null){
                                Glide.with(DogDetailsActivity.this)
                                        .load(profileImageUrl3)
                                        .fitCenter()
                                        .into(mImageview3);
                            }
                            if(profileImageUrl4 !=null){
                                Glide.with(DogDetailsActivity.this)
                                        .load(profileImageUrl4)
                                        .fitCenter()
                                        .into(mImageview4);
                            }
                            if(profileImageUrl5 !=null){
                                Glide.with(DogDetailsActivity.this)
                                        .load(profileImageUrl5)
                                        .fitCenter()
                                        .into(mImageview5);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }
}