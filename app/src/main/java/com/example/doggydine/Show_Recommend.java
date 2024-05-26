package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Show_Recommend extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList, originList;
    private Button mback, mrecommend_btn;

    private FirebaseDatabase database;
    private Spinner mspinner;
    private DatabaseReference databaseReference;
    private LottieAnimationView mLt_empty;
    private TextView mTv_emty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_recommend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.s_r_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        originList = new ArrayList<>();
        mLt_empty = findViewById(R.id.Lt_empty);
        mLt_empty.setVisibility(View.INVISIBLE);
        mLt_empty.setAnimation(R.raw.dog_sleep); // .json 파일을 로드
        mLt_empty.loop(true);
        mback = findViewById(R.id.back_btn);
        mrecommend_btn = findViewById(R.id.recommend_btn);
        mTv_emty = findViewById(R.id.tv_empty);
        mTv_emty.setVisibility(View.INVISIBLE);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("DoggyDine").child("Food");

        adapter = new Show_RecommendAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Recommend.this, Feeding.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mrecommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Recommend.this, Recommend.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_stay); // 애니메이션 설정
            }
        });

        Intent intent = getIntent();
        // 테스트
        if (intent != null && intent.getBooleanExtra("fromRecommend", false)) {
            ArrayList<String> selectedIngredients = intent.getStringArrayListExtra("selectedIngredients");
            if (selectedIngredients != null) {
                Log.d("SelectedIngredients", selectedIngredients.toString());
                filterFoodsByIngredients(selectedIngredients);
            }
        }
    }

    private void filterFoodsByIngredients(ArrayList<String> selectedIngredients) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                boolean dataFound = false;
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Food food = snapshot.getValue(Food.class);
                    if (food != null) {
                        boolean allIngredientsFalse = true;
                        for (String ingredient : selectedIngredients) {
                            if (food.getMaterial().containsKey(ingredient) && food.getMaterial().get(ingredient)) {
                                allIngredientsFalse = false;
                                break;
                            }
                        }
                        // 모든 재료가 false이면 RecyclerView에 추가
                        if (allIngredientsFalse) {
                            arrayList.add(food);
                            dataFound = true;
                        }
                    }
                }
                // 데이터가 없으면 빈 상태를 표시
                if (!dataFound || arrayList.isEmpty()) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                }
                // UI 스레드에서 어댑터 갱신
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 쿼리 취소 시 처리할 내용
            }
        });
    }

    private void showEmptyState() {
        mLt_empty.setVisibility(View.VISIBLE);
        mTv_emty.setVisibility(View.VISIBLE);
        mLt_empty.playAnimation();
    }

    private void hideEmptyState() {
        mLt_empty.setVisibility(View.INVISIBLE);
        mTv_emty.setVisibility(View.INVISIBLE);
        mLt_empty.pauseAnimation();
    }
}
