package com.example.doggydine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;
    ImageButton fixing_btn;
    ImageButton cancel_btn;

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
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
        }
    }

}
