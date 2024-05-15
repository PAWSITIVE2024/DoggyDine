package com.example.doggydine;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calender extends AppCompatActivity {
    CalendarView calendarView;
    RecyclerView todoRecyclerView;
    TodoAdapter todoAdapter;
    List<TodoItem> todoList;
    ImageButton addTodoButton;

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

        // RecyclerView 설정
        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(todoAdapter);

        // 캘린더 뷰 리스너 설정
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 선택한 날짜로 todoList 갱신
                updateTodoList(year, month, dayOfMonth);
            }
        });
        // 일정 추가하기 버튼 클릭 리스너 설정
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                TodoItem newTodo = new TodoItem("새로운 할 일", year, month, dayOfMonth);

                // todoList에 추가하고 어댑터에 변경 알리기
                todoList.add(newTodo);
                todoAdapter.notifyDataSetChanged();

                // 토스트 메시지 표시
                Toast.makeText(Calender.this, "새로운 할 일이 추가되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateTodoList(int year, int month, int dayOfMonth) {
        // 실제로는 해당 날짜에 저장된 todoList를 가져와서 todoList 변수에 업데이트하는 로직을 구현해야 함
        // 여기서는 임의의 예시 데이터로 대체
        todoList.clear();
        todoAdapter.notifyDataSetChanged();
    }
}