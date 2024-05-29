package com.example.doggydine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetectedFace extends AppCompatActivity {
    TextView metabolic_rate, one_day_cal, once_cal, one_day_food, once_food, left_food, going_food;
    Button saving_btn;

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
        metabolic_rate = findViewById(R.id.tx_metabolic_rate);
        one_day_cal = findViewById(R.id.tx_one_day_cal);
        once_cal = findViewById(R.id.tx_once_cal);
        one_day_food = findViewById(R.id.tx_one_day_food);
        once_food = findViewById(R.id.tx_once_food);
        left_food = findViewById(R.id.tx_left_food);
        going_food = findViewById(R.id.tx_going_food);
        saving_btn = findViewById(R.id.btn_saving);
    }
}