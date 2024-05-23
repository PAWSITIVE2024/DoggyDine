package com.example.doggydine;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;
    public TodoAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
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
                showCalendarDetailDialog(view.getContext());

            }
        });
        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    todoList.remove(itemPosition);
                    notifyItemRemoved(itemPosition);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return todoList.size();
    }
    private void showCalendarDetailDialog(Context context) {
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
        //View tagView = dialog.findViewById(R.id.tag_color_view);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String memo = memoEditText.getText().toString();

                // todoItem 객체를 업데이트하거나 저장 로직 추가
                // 예: todoItem.setTask(title);
                //     databaseReference.child(todoId).setValue(todoItem);

                Toast.makeText(context, "할 일이 저장되었습니다", Toast.LENGTH_SHORT).show();
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
