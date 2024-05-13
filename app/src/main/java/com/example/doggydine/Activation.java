package com.example.doggydine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

public class Activation extends AppCompatActivity{
    private Button saveActivationButton;
    private RadioGroup dietRadioGroup, activeRadioGroup, genderRadioGroup, pragnentRadioGroup, plusRadioGroup, pupRadioGroup;
    private float dietValue = 0, activeValue = 0, genderValue = 0, pragValue = 0, pupValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        saveActivationButton = findViewById(R.id.save_activation);
        saveActivationButton.setEnabled(false);

        dietRadioGroup = findViewById(R.id.diet_radio_group);
        activeRadioGroup = findViewById(R.id.active_radio_group);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        pragnentRadioGroup = findViewById(R.id.prag_radio_group);
        plusRadioGroup = findViewById(R.id.plus_radio_group);
        pupRadioGroup = findViewById(R.id.pup_radio_group);

        dietRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        activeRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        genderRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        pragnentRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        plusRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
        pupRadioGroup.setOnCheckedChangeListener(radioGroupChangeListener);
    }
    private RadioGroup.OnCheckedChangeListener radioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group == dietRadioGroup) {
                if (checkedId == R.id.need_weight) {
                    dietValue = 1.7f;
                } else if (checkedId == R.id.no_need_diet) {
                    dietValue = 1.4f;
                } else if (checkedId == R.id.need_loss) {
                    dietValue = 1.0f;
                }
            } else if (group == activeRadioGroup) {
                if (checkedId == R.id.less_active) {
                    activeValue = 2f;
                } else if (checkedId == R.id.normal_active) {
                    activeValue = 3f;
                } else if (checkedId == R.id.lots_active) {
                    activeValue = 4f;
                }
            } else if (group == genderRadioGroup) {
                if (checkedId == R.id.not_gender) {
                    genderValue = 1.8f;
                } else if (checkedId == R.id.done_gender) {
                    genderValue = 1.6f;
                }
            } else if (group == pragnentRadioGroup) {
                if (checkedId == R.id.not_prag) {
                    pragValue = 1f;
                    RadioGroup radioGroup = findViewById(R.id.plus_radio_group);
                    radioGroup.setVisibility(View.INVISIBLE);
                } else if (checkedId == R.id.yes_prag) {
                    RadioGroup radioGroup = findViewById(R.id.plus_radio_group);
                    radioGroup.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.yes_breeding) {
                    pragValue = 10f;
                    RadioGroup radioGroup = findViewById(R.id.plus_radio_group);
                    radioGroup.setVisibility(View.INVISIBLE);
                }
            } else if (group == plusRadioGroup) {
                if (checkedId == R.id.early_prag) {
                    pragValue = 5f;
                } else if (checkedId == R.id.late_prag) {
                    pragValue = 8f;
                }
            } else if (group == pupRadioGroup) {
                if (checkedId == R.id.yes_pup) {
                    pupValue = 3f;
                } else if (checkedId == R.id.middle_pup) {
                    pupValue = 2f;
                } else if (checkedId == R.id.not_pup) {
                    pupValue = 1f;
                }
            }
            saveActivationButton.setEnabled(isAllSectionsSelected());
        }
    };
    private boolean isAllSectionsSelected() {
        return dietValue != 0 && activeValue != 0 && genderValue != 0 && pragValue != 0 && pupValue != 0;
    }
    private void updateSaveButtonState() {
        if (isAllSectionsSelected()) {
            saveActivationButton.setEnabled(true);
            saveActivationButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        } else {
            saveActivationButton.setEnabled(false);
            saveActivationButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
    public void onSaveActivationClicked(View view) {
        if (isAllSectionsSelected()) {
            float averageValue = (dietValue + activeValue + genderValue + pragValue + pupValue) / 5;
            Intent resultIntent = new Intent();
            resultIntent.putExtra("averageValue", averageValue);
            setResult(RESULT_OK, resultIntent);
            finish(); // 현재 액티비티 종료
        } else {
            showDialog("선택을 완료해주세요");
        }
    }
    public void onDismissActivationClicked(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateSaveButtonState();
    }
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}