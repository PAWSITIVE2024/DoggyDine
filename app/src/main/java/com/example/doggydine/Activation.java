package com.example.doggydine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;


public class Activation extends AppCompatActivity {

    // 선택한 상태를 추적하는 변수들
    private boolean dietSelected = false;
    private boolean activeSelected = false;
    private boolean genderSelected = false;

    // 선택이 완료되었는지 확인하는 함수
    private boolean isSelectionComplete() {
        return dietSelected && activeSelected && genderSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        showActivationDialog();
    }

    private void showActivationDialog() {
        ActivationDialog activationDialog = new ActivationDialog();
        activationDialog.show(getSupportFragmentManager(), "activation_dialog");
    }

    public class ActivationDialog extends DialogFragment {
        private Button[] dietButtons;
        private Button[] activeButtons;
        private Button[] genderButtons;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.activity_activation, null);
            builder.setView(view);

            dietButtons = new Button[]{
                    view.findViewById(R.id.need_weight),
                    view.findViewById(R.id.no_need_diet),
                    view.findViewById(R.id.need_loss)
            };

            activeButtons = new Button[]{
                    view.findViewById(R.id.less_active),
                    view.findViewById(R.id.normal_active),
                    view.findViewById(R.id.lots_active)
            };

            genderButtons = new Button[]{
                    view.findViewById(R.id.not_genter),
                    view.findViewById(R.id.done_gender)
            };

            // 각 버튼에 대한 클릭 이벤트 처리
            setOnClickListenerForGroup(dietButtons);
            setOnClickListenerForGroup(activeButtons);
            setOnClickListenerForGroup(genderButtons);

            // save_activation 버튼을 클릭하면 선택이 완료되었는지 확인하고 처리
            Button saveButton = view.findViewById(R.id.save_activation);
            saveButton.setOnClickListener(v -> {
                if (isSelectionComplete()) {
                    // 선택이 완료된 경우 처리
                    dismiss();
                } else {
                    // 선택이 완료되지 않은 경우 팝업 표시
                    new AlertDialog.Builder(getActivity())
                            .setTitle("선택을 완료해주세요")
                            .setMessage("각 항목에 대해 선택을 완료해야 합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            return builder.create();
        }

        private void setOnClickListenerForGroup(Button[] buttons) {
            for (Button button : buttons) {
                button.setOnClickListener(v -> {
                    // 해당 버튼을 클릭했을 때 다른 버튼의 색 변경
                    for (Button otherButton : buttons) {
                        if (otherButton != button) {
                            otherButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        }
                    }
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

                    // 선택한 상태 업데이트
                    if (buttons == dietButtons) {
                        dietSelected = true;
                    } else if (buttons == activeButtons) {
                        activeSelected = true;
                    } else if (buttons == genderButtons) {
                        genderSelected = true;
                    }
                });
            }
        }
    }
}