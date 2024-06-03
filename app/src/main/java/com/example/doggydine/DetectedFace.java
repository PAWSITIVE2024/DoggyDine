package com.example.doggydine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetectedFace extends AppCompatActivity {
    TextView detectedName;
    TextView metabolic_rate, one_day_cal, once_cal, one_day_food, once_food, left_food, going_food;
    Button ok_btn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detected_face);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        detectedName = findViewById(R.id.detected_name);
        metabolic_rate = findViewById(R.id.tx_metabolic_rate);
        one_day_cal = findViewById(R.id.tx_one_day_cal);
        once_cal = findViewById(R.id.tx_once_cal);
        one_day_food = findViewById(R.id.tx_one_day_food);
        once_food = findViewById(R.id.tx_once_food);
        left_food = findViewById(R.id.tx_left_food);
        going_food = findViewById(R.id.tx_going_food);
        ok_btn = findViewById(R.id.btn_ok);
        // 여기서 데이터 베이스에서 Detected/detected_name 받아옴.
        // 받아온 detected_name을 PetCalorieCalculator로 넣어줌
        // 이름 기반으로 몸무게, 활동수치 등을 받아옴.
        // 이름 기반으로 선택되어있는 사료의 칼로리와 중량을 받아와서 kcal/g 계산
        // 이걸로 급여량 계산 필요.

        // 기초대사량 계산
        double weight = WeightCalculator.getWeight();
        double basalMetabolicRate = PetCalorieCalculator.calculateBasalMetabolicRate(weight);

        // 기초대사량을 텍스트뷰에 설정
        metabolic_rate.setText("기초대사량: " + basalMetabolicRate + " kcal");
    }
}