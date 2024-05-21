package com.example.doggydine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;
    private Context context;

    public TodoAdapter(List<TodoItem> todoList, Context context) {this.todoList = todoList; this.context = context;}

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
            public void onClick(View v) {
                // 다이얼로그를 띄워서 수정 기능 구현
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("할 일 수정");

                final EditText input = new EditText(context);
                input.setText(todoItem.getTask());
                builder.setView(input);

                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoItem.setTask(input.getText().toString());
                        notifyItemChanged(position);
                        Toast.makeText(context, "할 일이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이템 삭제 기능 구현
                todoList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, todoList.size());
                Toast.makeText(context, "할 일이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
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
