package com.example.doggydine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;

public class CalendarDetail extends AppCompatActivity {
    private static final String TAG = "CalendarDetail";

    private EditText titleEditText, locationEditText, memoEditText;
    private TextClock timeEdit;
    private Button saveButton, cancelButton;
    private DatabaseReference databaseReference;
    private String todoId;
    private TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Start");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar_detail);
        Log.d(TAG, "onCreate: setContentView completed");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        titleEditText = findViewById(R.id.titleEditText);
        locationEditText = findViewById(R.id.locationEditText);
        timeEdit = findViewById(R.id.text_clock);
        memoEditText = findViewById(R.id.memoEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String memo = memoEditText.getText().toString();

                todoItem.setTask(title);
                databaseReference.child(todoId).setValue(todoItem);

                Toast.makeText(CalendarDetail.this, "할 일이 저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dialog 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarDetail.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.time_picker, null);
                builder.setView(dialogView);

                // TimePicker와 버튼 참조
                TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                Button saveButton = dialogView.findViewById(R.id.saveButton);

                // Dialog 생성
                AlertDialog dialog = builder.create();
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        // 시간 설정 값을 사용하여 EditText에 표시
                        timeEdit.setText(String.format("%02d:%02d", hour, minute));
                        dialog.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        Log.d(TAG, "onCreate: End");
    }
}