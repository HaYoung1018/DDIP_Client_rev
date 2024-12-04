package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button addWorkButton;
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private List<CalendarItem> calendarItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUserType = sharedPreferences.getString("userType", "");

        // XML 요소와 연결
        calendarView = findViewById(R.id.calendar_view);
        addWorkButton = findViewById(R.id.add_work_button);
        recyclerView = findViewById(R.id.recycler_view);

        // RecyclerView 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false); // ScrollView와 함께 사용할 때 필요
        calendarItemList = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(calendarItemList);
        recyclerView.setAdapter(calendarAdapter);

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        homeButton.setOnClickListener(v -> {
            if(savedUserType.equals("Owner")){
                Intent intent = new Intent(CalendarActivity.this, OwnerMainActivity.class);
                startActivity(intent);
                finish();
            } else if (savedUserType.equals("Staff")) {
                Intent intent = new Intent(CalendarActivity.this, StaffMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CalendarActivity.this, "사용자 종류가 저장되지 않았습니다. 로그아웃 후 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        subCrewButton.setOnClickListener(v -> startActivity(new Intent(CalendarActivity.this, CrewRoomActivity.class)));
        alarmButton.setOnClickListener(v -> startActivity(new Intent(CalendarActivity.this, AlarmActivity.class)));
        myPageButton.setOnClickListener(v -> startActivity(new Intent(CalendarActivity.this, MypageActivity.class)));

        // 캘린더 날짜 선택 리스너 설정
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth);
            long selectedDate = selectedCal.getTimeInMillis();

            // 선택된 날짜 메시지 표시
            String selectedDateMessage = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate);
            Toast.makeText(this, "선택된 날짜: " + selectedDateMessage, Toast.LENGTH_SHORT).show();
        });

        // 근무 추가 버튼 클릭 리스너
        addWorkButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, AddWorkActivity.class);
            startActivityForResult(intent, 100); // AddWorkActivity 호출
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // AddWorkActivity에서 전달받은 데이터 처리
            long workDateMillis = data.getLongExtra("selectedDate", -1);
            String workTime = data.getStringExtra("workTime");
            String wage = data.getStringExtra("wage");

            if (workDateMillis != -1 && workTime != null && wage != null) {
                // 날짜 포맷팅
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(workDateMillis);

                // 요일 계산
                String[] dayOfWeekKorean = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(workDateMillis);
                String dayOfWeek = dayOfWeekKorean[cal.get(Calendar.DAY_OF_WEEK) - 1];

                // 일한 시간 계산
                String[] timeParts = workTime.split("~");
                String[] startParts = timeParts[0].trim().split(":");
                String[] endParts = timeParts[1].trim().split(":");

                int startHour = Integer.parseInt(startParts[0]);
                int startMinute = Integer.parseInt(startParts[1]);
                int endHour = Integer.parseInt(endParts[0]);
                int endMinute = Integer.parseInt(endParts[1]);

                double totalHours = (endHour + endMinute / 60.0) - (startHour + startMinute / 60.0);

                // 시급 계산
                int wagePerHour = Integer.parseInt(wage);
                int calculatedWage = totalHours >= 1 ? (int) (wagePerHour * Math.floor(totalHours)) : 0;

                // 리스트에 아이템 추가
                calendarItemList.add(new CalendarItem(formattedDate, dayOfWeek, workTime, calculatedWage + "원"));

                // RecyclerView 업데이트
                calendarAdapter.notifyDataSetChanged();
            }
        }
    }
}
