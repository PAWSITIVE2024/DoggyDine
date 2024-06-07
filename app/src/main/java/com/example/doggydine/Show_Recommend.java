package com.example.doggydine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Collections;
import java.util.Comparator;
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
    private ImageView mImageview;

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
        mImageview = findViewById(R.id.Iv_go_shopping);
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

        mspinner = findViewById(R.id.spinner);
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // 원본 상태 선택
                    arrayList.clear();
                    arrayList.addAll(originList);
                    adapter.notifyDataSetChanged();
                } else if (position == 1) { // 가격 순 선택
                    Collections.sort(arrayList, new Comparator<Food>() {
                        @Override
                        public int compare(Food food1, Food food2) {
                            // 가격이 null이면 두 음식은 동등하다고 간주
                            if (food1.getPrice() == null && food2.getPrice() == null) {
                                return 0;
                            } else if (food1.getPrice() == null) {
                                return 1;
                            } else if (food2.getPrice() == null) {
                                return -1;
                            }
                            // 문자열을 Double로 내림차순
                            return Double.compare(Double.parseDouble(food1.getPrice()), Double.parseDouble(food2.getPrice()));
                        }
                    });
                } else if (position == 2) { // 평점 순 선택
                    Collections.sort(arrayList, new Comparator<Food>() {
                        @Override
                        public int compare(Food food1, Food food2) {
                            // 평점이 같으면 이름 순으로 정렬
                            if (food1.getScore().equals(food2.getScore())) {
                                return food1.getName().compareTo(food2.getName());
                            }
                            // 평점을 Double로 오름차순
                            return Double.compare(Double.parseDouble(food2.getScore()), Double.parseDouble(food1.getScore()));
                        }
                    });
                } else if (position == 3) { // 판매량 순 선택
                    Collections.sort(arrayList, new Comparator<Food>() {
                        @Override
                        public int compare(Food food1, Food food2) {
                            // sale_Volume이 null이면 두 음식은 동등하다고 간주
                            if (food1.getSales_Volume() == null && food2.getSales_Volume() == null) {
                                return food1.getName().compareTo(food2.getName());
                            } else if (food1.getSales_Volume() == null) {
                                return 1;
                            } else if (food2.getSales_Volume() == null) {
                                return -1;
                            }
                            // 판매량이 같으면 이름 순으로 정렬
                            if (food1.getSales_Volume().equals(food2.getSales_Volume())) {
                                return food1.getName().compareTo(food2.getName());
                            }
                            // 문자열을 Integer로 오름차순
                            return Integer.compare(Integer.parseInt(food2.getSales_Volume()), Integer.parseInt(food1.getSales_Volume()));
                        }
                    });
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때의 처리
            }
        });

        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Show_Recommend.this, FoodCompare.class);
                startActivity(intent);
                finish();
            }
        });

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
                intent.putExtra("fromFeeding",true);
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
                originList.clear();
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
                            originList.add(food);
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
