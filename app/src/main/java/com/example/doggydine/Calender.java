package com.example.doggydine;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calender extends AppCompatActivity {
    CalendarView calendarView;
    RecyclerView todoRecyclerView;
    TodoAdapter todoAdapter;
    List<TodoItem> todoList;
    ImageButton addTodoButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private int selectedYear, selectedMonth, selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calender);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        calendarView = findViewById(R.id.calendarView);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        addTodoButton = findViewById(R.id.addTodoButton);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(currentUserId).child("Calendar");


        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);

        // 기본 선택 날짜 설정
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 기본 선택 날짜에 대한 일정 업데이트
        updateTodoList(selectedYear, selectedMonth, selectedDay);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                updateTodoList(year, month, dayOfMonth);
            }
        });

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItem newTodo = new TodoItem("새로운 할 일", selectedYear, selectedMonth, selectedDay);
                todoList.add(newTodo);
                todoAdapter.notifyDataSetChanged();

                Toast.makeText(Calender.this, "새로운 할 일이 추가되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTodoList(int year, int month, int dayOfMonth) {

        String dateKey = year + "-" + (month + 1) + "-" + dayOfMonth;
        databaseReference.child(dateKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                todoList.clear();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String taskTitle = snapshot.getKey();
                    ScheduleItem scheduleItem = snapshot.getValue(ScheduleItem.class);
                    TodoItem todoItem = new TodoItem(taskTitle, year, month, dayOfMonth);
                    todoList.add(todoItem);
                }
                todoAdapter.notifyDataSetChanged();
            } else {
            }
        });
    }
}
