package com.example.doggydine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;
    private DatabaseReference databaseReference;
    private String currentUserId;

    public TodoAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine");
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem todoItem = todoList.get(position);
        holder.taskTextView.setText(todoItem.getTask());

        holder.fixing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showCalendarDetailDialog(view.getContext(), todoItem, adapterPosition);
                }
            }
        });

        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Firebase에서 삭제
                    String dateKey = todoItem.getYear() + "-" + (todoItem.getMonth() + 1) + "-" + todoItem.getDayOfMonth();
                    databaseReference.child("UserAccount").child(currentUserId).child("Calendar").child(dateKey).child(todoItem.getTask()).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            todoList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            Toast.makeText(v.getContext(), "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), "일정 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private void showCalendarDetailDialog(Context context, TodoItem todoItem, int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar_detail);

        // 다이얼로그 내부의 뷰 참조
        EditText titleEditText = dialog.findViewById(R.id.titleEditText);
        EditText locationEditText = dialog.findViewById(R.id.locationEditText);
        TextView timeEdit = dialog.findViewById(R.id.text_clock);
        EditText memoEditText = dialog.findViewById(R.id.memoEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        // 기존 일정 정보 설정
        String dateKey = todoItem.getYear() + "-" + (todoItem.getMonth() + 1) + "-" + todoItem.getDayOfMonth();
        databaseReference.child("UserAccount").child(currentUserId).child("Calendar").child(dateKey).child(todoItem.getTask()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                ScheduleItem scheduleItem = task.getResult().getValue(ScheduleItem.class);
                if (scheduleItem != null) {
                    titleEditText.setText(scheduleItem.getTitle());
                    locationEditText.setText(scheduleItem.getLocation());
                    timeEdit.setText(scheduleItem.getTime());
                    memoEditText.setText(scheduleItem.getMemo());
                }
            } else {
                Toast.makeText(context, "일정 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String memo = memoEditText.getText().toString();
                String time = timeEdit.getText().toString();

                // 기존 키로 삭제 후 새로운 키로 저장
                databaseReference.child("UserAccount").child(currentUserId).child("Calendar").child(dateKey).child(todoItem.getTask()).removeValue();

                // 새로운 일정으로 업데이트
                todoItem.setTask(title);
                databaseReference.child("UserAccount").child(currentUserId).child("Calendar").child(dateKey).child(title).setValue(new ScheduleItem(title, location, time, memo)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        todoList.set(position, todoItem);
                        notifyItemChanged(position);
                        Toast.makeText(context, "할 일이 저장되었습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "할 일 저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 시간을 선택하는 다이얼로그 생성
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
                        timeEdit.setText(String.format("%02d:%02d", hour, minute));
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
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;
        ImageButton fixing_btn;
        ImageButton cancel_btn;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            fixing_btn = itemView.findViewById(R.id.fixing_btn);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
        }
    }
}
