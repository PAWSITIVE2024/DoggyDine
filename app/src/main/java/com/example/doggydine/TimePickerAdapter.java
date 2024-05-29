package com.example.doggydine;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimePickerAdapter extends RecyclerView.Adapter<TimePickerAdapter.TimePickerViewHolder> {
    private List<Integer> timePickerList = new ArrayList<>();
    private Context context;

    public TimePickerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TimePickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_picker, parent, false);
        return new TimePickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimePickerViewHolder holder, int position) {
        // 여기서 필요한 경우 holder의 데이터 설정 가능
    }

    @Override
    public int getItemCount() {
        return timePickerList.size();
    }

    public void setTimePickers(int count) {
        timePickerList.clear();
        for (int i = 0; i < count; i++) {
            timePickerList.add(i);
        }
        notifyDataSetChanged();
    }

    public class TimePickerViewHolder extends RecyclerView.ViewHolder {
        private TextView time_txt;

        public TimePickerViewHolder(@NonNull View itemView) {
            super(itemView);
            time_txt = itemView.findViewById(R.id.time_txt);

            time_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialog();
                }
            });
        }

        private void showTimePickerDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.time_picker, null);
            builder.setView(dialogView);

            TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
            Button cancelDialogButton = dialogView.findViewById(R.id.cancelButton);
            Button saveDialogButton = dialogView.findViewById(R.id.saveButton);

            AlertDialog timeDialog = builder.create();
            saveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    time_txt.setText(String.format("%02d:%02d", hour, minute));
                    timeDialog.dismiss();
                }
            });
            cancelDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeDialog.dismiss();
                }
            });
            timeDialog.show();
        }
    }
}