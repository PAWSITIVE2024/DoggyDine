package com.example.doggydine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
    private List<String> timeList = new ArrayList<>();
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
        String time = timeList.get(position);
        holder.time_txt.setText(time);
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public void setTimePickers(int count) {
        timeList.clear();
        for (int i = 0; i < count; i++) {
            timeList.add(""); // 초기값은 빈 문자열로 설정
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
                    showTimePickerDialog(getAdapterPosition()); // 현재 아이템의 위치 전달
                }
            });
        }

        private void showTimePickerDialog(final int position) {
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
                    String time = String.format("%02d:%02d", hour, minute);
                    timeList.set(position, time); // 선택된 시간을 리스트에 저장
                    notifyDataSetChanged(); // 변경 사항을 반영하여 UI 갱신

                    // SharedPreferences에 저장
                    SharedPreferences sharedPreferences = context.getSharedPreferences("FromSelectDogFood", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("time" + (position+1), time); // 위치별로 다른 key 사용
                    editor.apply();

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
