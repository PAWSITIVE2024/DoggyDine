package com.example.doggydine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimePickerAdapter extends RecyclerView.Adapter<TimePickerAdapter.TimePickerViewHolder> {
    private List<Integer> timePickerList = new ArrayList<>();
    @NonNull
    @Override
    public TimePickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_picker, parent, false);
        return new TimePickerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TimePickerViewHolder holder, int position) {
        holder.timePicker.setIs24HourView(true);
        holder.timePicker.setCurrentHour(12);
        holder.timePicker.setCurrentMinute(0);
        holder.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // 시간 변경 시의 동작을 추가
            }
        });
    }

    @Override
    public int getItemCount() {
        return timePickerList.size();
    }

    public void addTimePickers(int count) {
        int startSize = timePickerList.size();
        for (int i = 0; i < count; i++) {
            timePickerList.add(startSize + i);
        }
        notifyItemRangeInserted(startSize, count);
    }

    public static class TimePickerViewHolder extends RecyclerView.ViewHolder {
        public TimePicker timePicker;

        public TimePickerViewHolder(@NonNull View itemView) {
            super(itemView);
            timePicker = itemView.findViewById(R.id.time_picker);
        }
    }
}
