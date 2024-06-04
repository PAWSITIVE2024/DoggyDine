package com.example.doggydine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DogDetailsActivity extends AppCompatActivity {
    private ImageView mImageview,mImageview2,mImageview3,mImageview4,mImageview5,dog_food;
    private Button mBtnRegister,mBtnDelete;
    private ImageButton activeButton,calendarButton, allergyButton;
    private ImageButton food_num;
    private TextView selectedDateTextView,dog_food_text,activeTextView, how_much_text,mName,mWeight,mAllergy;
    private Calendar calendar;
    private String imageuri_1,imageuri_2,imageuri_3,imageuri_4,imageuri_5,profileImageUrl1,profileImageUrl2,profileImageUrl3,profileImageUrl4,profileImageUrl5;
    private Uri selectedImageUri,selectedImageUrl_1,selectedImageUrl_2,selectedImageUrl_3,selectedImageUrl_4,selectedImageUrl_5;
    private int count = 1;
    private static final  int PICK_IMAGE_REQUEST = 1;
    private String dogName;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,mDatabaseRef;

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
        dog_food_text = findViewById(R.id.dogfood_dt_text);
        mAllergy = findViewById(R.id.Et_dt_allergy);
        mImageview = findViewById(R.id.imageView);
        mImageview2 = findViewById(R.id.imageView2);
        mImageview3 = findViewById(R.id.imageView3);
        mImageview4 = findViewById(R.id.imageView4);
        mImageview5 = findViewById(R.id.imageView5);

        mBtnRegister = findViewById(R.id.Btn_d_s_register);
        mBtnDelete = findViewById(R.id.Btn_d_s_delete);

        dog_food = findViewById(R.id.dog_food_btn);
        dog_food_text = findViewById(R.id.dogfood_dt_text);
        calendarButton = findViewById(R.id.selectDate);
        activeButton = findViewById(R.id.active_btn);
        allergyButton = findViewById(R.id.allergy_btn);
        food_num = findViewById(R.id.food_num_btn);
        activeTextView = findViewById(R.id.active_dt_text);
        selectedDateTextView = findViewById(R.id.dt_selectedDateTextView);
        how_much_text = findViewById(R.id.Et_dt_how_many);
        calendar = Calendar.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid);

        Intent intent = getIntent();
        if(intent != null){
             dogName = intent.getStringExtra("dog_name");
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
                            activeTextView.setText(pet.getActive_rate());
                            dog_food_text.setText(pet.getDog_food());
                            mAllergy.setText(pet.getAllergy());

                            Map<String, String> profileMap = pet.getProfile();
                            if (profileMap != null) {
                                profileImageUrl1 = profileMap.get("profile1");
                                profileImageUrl2 = profileMap.get("profile2");
                                profileImageUrl3 = profileMap.get("profile3");
                                profileImageUrl4 = profileMap.get("profile4");
                                profileImageUrl5 = profileMap.get("profile5");
                            }

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

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ여기부터 eventlistener 달기 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dogNameToDelete = mName.getText().toString();
                if (dogNameToDelete != null && !dogNameToDelete.isEmpty()) {
                    mDatabaseRef.child("pet").child(dogNameToDelete).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DogDetailsActivity.this, "강아지 정보가 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DogDetailsActivity.this, "강아지 정보 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(DogDetailsActivity.this, "삭제할 강아지를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        allergyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent (DogDetailsActivity.this, Recommend.class);
                intent2.putExtra("FromSelectDogFood", true); // fromFeeding 값을 추가
                startActivity(intent2);
            }
        });
        dog_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogDetailsActivity.this,SelectDogFood.class);
                startActivity(intent);
            }
        });

        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivationDialog();
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        food_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showNumberDialog(); }
        });

        //Imageview clickListener 설정
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
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialog dialog = new AppCompatDialog(DogDetailsActivity.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(true); // 다이얼로그를 취소할 수 있도록 설정

                // 다이얼로그의 레이아웃에서 LottieAnimationView 가져오기
                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_loading_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
                dialog.show();

                // 입력필드에서 text를 가져오는 부분 입니다
                String pet_name = mName.getText().toString();
                String pet_age = selectedDateTextView.getText().toString();
                String pet_weight = mWeight.getText().toString();
                String pet_activation = activeTextView.getText().toString();
                //활동수치 부분은 만드시는중인거 같아서 그대로 뒀습니다!!
                //활동수치 등록 필요!!!!!
                String pet_allergy = mAllergy.getText().toString();
                String pet_food = dog_food_text.getText().toString();

                // 첫번째 사진 올리기
                Task<String> uploadTask1 = uploadImageToFirebase(selectedImageUrl_1,pet_name);
                // 두번째 사진 올리기
                Task<String> uploadTask2 = uploadImageToFirebase(selectedImageUrl_2,pet_name);
                // 세번째 사진 올리기
                Task<String> uploadTask3 = uploadImageToFirebase(selectedImageUrl_3,pet_name);
                // 네번째 사진 올리기
                Task<String> uploadTask4 = uploadImageToFirebase(selectedImageUrl_4,pet_name);
                // 다섯번째 사진 올리기
                Task<String> uploadTask5 = uploadImageToFirebase(selectedImageUrl_5,pet_name);

                // 모든 업로드 작업이 완료될 때까지 기다림

                    Tasks.whenAllComplete(uploadTask1, uploadTask2, uploadTask3, uploadTask4, uploadTask5)
                            .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                                @Override
                                public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                    if (task.isSuccessful()) {
                                        // 모든 업로드 작업이 성공적으로 완료된 경우
                                        // 이미지 URL 가져오기
                                        imageuri_1 = uploadTask1.getResult();
                                        imageuri_2 = uploadTask2.getResult();
                                        imageuri_3 = uploadTask3.getResult();
                                        imageuri_4 = uploadTask4.getResult();
                                        imageuri_5 = uploadTask5.getResult();

                                        if(imageuri_1.equals("")) {
                                            imageuri_1 = profileImageUrl1;
                                        }
                                        if(imageuri_2.equals("")){
                                            imageuri_2 = profileImageUrl2;
                                        }
                                        if(imageuri_3.equals("")){
                                            imageuri_3 = profileImageUrl3;
                                        }
                                        if(imageuri_4.equals("")){
                                            imageuri_4 =profileImageUrl4;
                                        }
                                        if(imageuri_5.equals("")){
                                            imageuri_5=profileImageUrl5;
                                        }

                                        PetAccount pet_account = new PetAccount();
                                        pet_account.setDog_name(pet_name);
                                        pet_account.setDog_age(pet_age);
                                        pet_account.setDog_weight(pet_weight);
                                        pet_account.setAllergy(pet_allergy);
                                        pet_account.setDog_food(pet_food);
                                        pet_account.setActive_rate(pet_activation);
                                        pet_account.setProfile1(imageuri_1);
                                        pet_account.setProfile2(imageuri_2);
                                        pet_account.setProfile3(imageuri_3);
                                        pet_account.setProfile4(imageuri_4);
                                        pet_account.setProfile5(imageuri_5);


                                        //DB에 저장한다
                                        mDatabaseRef.child("pet").child(pet_name).setValue(pet_account)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(DogDetailsActivity.this, "강아지 정보가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                    } else {
                                        // 업로드 작업 중 실패한 경우
                                        Toast.makeText(DogDetailsActivity.this, "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


            }
        });
    } //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡOnCreate 끝 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("FromSelectDogFood", Context.MODE_PRIVATE);
        String foodName = sharedPreferences.getString("foodName", "");
        String allergy = sharedPreferences.getString("allergy","");
        if (!foodName.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            dog_food_text.setText(foodName);
            editor.putString("foodName", "");
            editor.apply();

        } else if (!allergy.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            mAllergy.setText(allergy);
            editor.putString("allergy", "");
            editor.apply();


        }
        Log.d("dog_food", foodName);
    }
    private void showActivationDialog() {
        Intent intent = new Intent(this, Activation.class);
        startActivityForResult(intent, 1);
    }
    private void showNumberDialog() {
        Intent intent = new Intent(this, PickNumber.class);
        startActivityForResult(intent, 2);
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        selectedDateTextView.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    //openGallery()
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
                    mImageview.setImageURI(selectedImageUrl_1);
                    break;
                case PICK_IMAGE_REQUEST + 1:
                    mImageview2.setImageURI(selectedImageUri);
                    selectedImageUrl_2 = selectedImageUri;
                    mImageview2.setImageURI(selectedImageUrl_2);
                    break;
                case PICK_IMAGE_REQUEST + 2:
                    mImageview3.setImageURI(selectedImageUri);
                    selectedImageUrl_3 = selectedImageUri;
                    mImageview3.setImageURI(selectedImageUrl_3);
                    break;
                case PICK_IMAGE_REQUEST + 3:
                    mImageview4.setImageURI(selectedImageUri);
                    selectedImageUrl_4 = selectedImageUri;
                    mImageview4.setImageURI(selectedImageUrl_4);
                    break;
                case PICK_IMAGE_REQUEST + 4:
                    mImageview5.setImageURI(selectedImageUri);
                    selectedImageUrl_5 = selectedImageUri;
                    mImageview5.setImageURI(selectedImageUrl_5);
                    break;
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            float averageValue = data.getFloatExtra("averageValue", 0.0f);
            String activationRate = String.valueOf(averageValue);
            activeTextView.setText(activationRate);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            int numberValue = data.getIntExtra("numberValue", -1);
            if (numberValue != -1) {
                String numberRate = String.valueOf(numberValue);
                how_much_text.setText(numberRate);
            }
        }
    }
    private Task<String> uploadImageToFirebase(Uri imageUri,String dog_name ) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);


        // 이미지 파일 이름 생성
        String imageFileName = dog_name+"_"+"image" + count + ".jpg";
        count++;


        // 이미지 파일 이름으로 새로운 참조 생성
        if (imageUri == null){
            return Tasks.forResult("");
        }
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