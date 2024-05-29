package com.example.doggydine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PickNumber extends AppCompatActivity {
    private Button save_btn;
    private RadioGroup NumberGroup;
    private int number_Value = 0;
    private RecyclerView timePicking;
    private TimePickerAdapter timePickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pick_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        save_btn = findViewById(R.id.save_number_btn);
        save_btn.setEnabled(false);
        NumberGroup = findViewById(R.id.pick_num_layer);
        NumberGroup.setOnCheckedChangeListener(radioGroupChangeListener);

        timePicking = findViewById(R.id.time_picker_list);
        timePicking.setLayoutManager(new LinearLayoutManager(this));
        timePickerAdapter = new TimePickerAdapter(this);
        timePicking.setAdapter(timePickerAdapter);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveActivationClicked(v);
            }
        });
    }

    private final RadioGroup.OnCheckedChangeListener radioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group == NumberGroup) {
                if (checkedId == R.id.once_btn) {
                    number_Value = 1;
                } else if (checkedId == R.id.twice_btn) {
                    number_Value = 2;
                } else if (checkedId == R.id.triple_btn) {
                    number_Value = 3;
                } else if (checkedId == R.id.four_btn) {
                    number_Value = 4;
                } else if (checkedId == R.id.five_btn) {
                    number_Value = 5;
                }
                updateRecyclerView();
            }
            updateSaveButtonState();
        }
    };

    private void updateRecyclerView() {
        timePickerAdapter.setTimePickers(number_Value);
    }

    private void updateSaveButtonState() {
        boolean allSectionsSelected = isAllSectionsSelected();
        save_btn.setEnabled(allSectionsSelected);
        int color = allSectionsSelected ? android.R.color.holo_blue_light : android.R.color.darker_gray;
        save_btn.setBackgroundColor(getResources().getColor(color));
    }

    private boolean isAllSectionsSelected() {
        return number_Value != 0;
    }

    public void onSaveActivationClicked(View view) {
        if (isAllSectionsSelected()) {
            int numberValue = number_Value;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("numberValue", numberValue);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            showDialog("선택을 완료해주세요");
        }
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}

