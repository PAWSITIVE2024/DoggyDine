package com.example.doggydine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Feeding extends AppCompatActivity {
    private String barcode_num, saved_barcode_num;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList;
    private ArrayList<Food> originList;
    private FirebaseDatabase database;
    private Spinner mspinner;
    private DatabaseReference databaseReference;

    private ImageView mImageview;
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feeding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        barcode_num = null;
        saved_barcode_num = null;
        recyclerView = findViewById(R.id.d_f_recyclerView);
        mImageview = findViewById(R.id.Iv_go_shopping);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        originList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("DoggyDine").child("Food");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCheckRef = FirebaseDatabase.getInstance().getReference("DoggyDine")
                .child("UserAccount").child(userID).child("check");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                originList.clear();

                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Food food = new Food();
                    food.setProfile(snapshot.child("profile").getValue(String.class));
                    food.setName(snapshot.child("name").getValue(String.class));
                    String foodName = snapshot.child("name").getValue(String.class);
                    food.setScore(snapshot.child("score").getValue(String.class));
                    food.setPrice(snapshot.child("price").getValue(String.class));
                    food.setSales_Volume(snapshot.child("sales_Volume").getValue(String.class));
                    userCheckRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                String foodName = snapshot.getKey();
                                Boolean check = snapshot.getValue(Boolean.class);
                                // 해당 음식을 찾아서 check 값을 업데이트
                                for (int i = 0; i < arrayList.size(); i++) {
                                    if (arrayList.get(i).getName().equals(foodName)) {
                                        arrayList.get(i).setCheck(check);
                                        adapter.notifyItemChanged(i);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 처리할 내용
                        }
                    });
                    Map<String, Boolean> materialMap = new HashMap<>();
                    DataSnapshot materialSnapshot = snapshot.child("material");
                    for (DataSnapshot materialChild : materialSnapshot.getChildren()) {
                        Boolean isTrue = materialChild.getValue(Boolean.class);
                        if (isTrue) {
                            materialMap.put(materialChild.getKey(), isTrue);
                        }
                    }
                    food.setMaterial(materialMap);

                    arrayList.add(food);
                    originList.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 처리할 내용
            }
        });
        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feeding.this, FoodCompare.class);
                startActivity(intent);
                finish();
            }
        });

        Button recommend_btn = findViewById(R.id.recommend_btn);
        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feeding.this, Recommend.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_stay); // 애니메이션 설정
            }
        });

        // Spinner에서 선택된 정렬 방법을 감지하는 리스너 구현
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

        btn_scan = findViewById(R.id.barcode_btn);
        btn_scan.setOnClickListener(v -> {
            scanCode();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Feeding.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // 바코드 결과를 변수에 저장
            barcode_num = result.getContents();
            // Test용
            Log.d("Barcode", "Scanned Barcode Number: " + barcode_num);

            // AlertDialog 생성 및 표시
            AlertDialog.Builder builder = new AlertDialog.Builder(Feeding.this);
            builder.setTitle("알림");
            builder.setMessage("등록하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    saved_barcode_num = barcode_num;
                    processBarcode(saved_barcode_num);
                    saved_barcode_num=null;
                    barcode_num=null;
                    //Toast.makeText(Feeding.this, "상품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    barcode_num = null;
                }
            }).show();
        }
    });

    private void processBarcode(String barcode) {
        // 바코드 번호를 사용하여 필요한 작업 수행
        if (barcode != null && !barcode.isEmpty()) {
            Log.d("Barcode", "Processed Barcode Number: " + barcode);
        }
    }

}
