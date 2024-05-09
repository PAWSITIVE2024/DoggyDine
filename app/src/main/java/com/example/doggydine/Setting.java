package com.example.doggydine;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PetAccount> arrayList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LottieAnimationView lottieAnimationView = findViewById(R.id.LT_s_dog_sleep);
        lottieAnimationView.setAnimation(R.raw.dog_sleep); // .json 파일을 로드
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();

        recyclerView = findViewById(R.id.DogInfoEdit);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("pet");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();

                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    PetAccount petaccount = new PetAccount();
                    petaccount.setProfile1(snapshot.child("profile1").getValue(String.class));
                    petaccount.setDog_name(snapshot.child("dog_name").getValue(String.class));
                    arrayList.add(petaccount); // 데이터를 리스트에 추가
                    Log.d("PetAccount", petaccount.toString());
                }
                adapter.notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new DogInfoAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);



    }
}