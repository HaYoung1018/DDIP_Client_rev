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

import com.example.ddip_client.models.Schedule;
import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        String savedId = sharedPreferences.getString("userId", "");

        // XML 요소와 연결
        calendarView = findViewById(R.id.calendar_view);
        addWorkButton = findViewById(R.id.add_work_button);
        recyclerView = findViewById(R.id.recycler_view);

        // RecyclerView 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(true); // ScrollView와 함께 사용할 때 필요
        calendarItemList = new ArrayList<>();
        calendarAdapter = new CalendarAdapter(calendarItemList);
        recyclerView.setAdapter(calendarAdapter);

        ScheduleApiService scheduleApiService = RetrofitClient.getClient().create(ScheduleApiService.class);
        Call<List<Map<String, String>>> call = scheduleApiService.getMySchedule(savedId);

        call.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                for(int i = 0; i < response.body().size(); i++){
                    Map<String, String> scheduleData = response.body().get(i);
                    String strtimeS = scheduleData.get("startTime");
                    String strtimeE = scheduleData.get("endTime");
                    Double totalHours = Double.valueOf(scheduleData.get("totalHours"));
                    Integer pay = Integer.valueOf(scheduleData.get("pay"));
                    Date date;
                    try {
                        date = StringToDate(scheduleData.get("date"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String day = DateToDay(date);
                    String itemTime = strtimeS + " ~ " + strtimeE;
                    Double dSalary = totalHours * pay;
                    String salary = String.valueOf(dSalary.intValue());

                    calendarItemList.add(new CalendarItem(scheduleData.get("date"), day, itemTime, salary+"원"));
                    calendarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {

            }
        });

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
        subCrewButton.setOnClickListener(v -> startActivity(new Intent(CalendarActivity.this, ImsiCrewRoomListActivity.class)));
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//            // AddWorkActivity에서 전달받은 데이터 처리
//            long workDateMillis = data.getLongExtra("selectedDate", -1);
//            String workTime = data.getStringExtra("workTime");
//            String wage = data.getStringExtra("wage");
//
//            if (workDateMillis != -1 && workTime != null && wage != null) {
//                // 날짜 포맷팅
//                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(workDateMillis);
//
//                // 요일 계산
//                String[] dayOfWeekKorean = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(workDateMillis);
//                String dayOfWeek = dayOfWeekKorean[cal.get(Calendar.DAY_OF_WEEK) - 1];
//
//                // 일한 시간 계산
//                String[] timeParts = workTime.split("~");
//                String[] startParts = timeParts[0].trim().split(":");
//                String[] endParts = timeParts[1].trim().split(":");
//
//                int startHour = Integer.parseInt(startParts[0]);
//                int startMinute = Integer.parseInt(startParts[1]);
//                int endHour = Integer.parseInt(endParts[0]);
//                int endMinute = Integer.parseInt(endParts[1]);
//
//                double totalHours = (endHour + endMinute / 60.0) - (startHour + startMinute / 60.0);
//
//                // 시급 계산
//                int wagePerHour = Integer.parseInt(wage);
//                int calculatedWage = totalHours >= 1 ? (int) (wagePerHour * Math.floor(totalHours)) : 0;
//
//                // 리스트에 아이템 추가
//                calendarItemList.add(new CalendarItem(formattedDate, dayOfWeek, workTime, calculatedWage + "원"));
//
//                // RecyclerView 업데이트
//                calendarAdapter.notifyDataSetChanged();
//            }
//        }
//    }
    public LocalTime StringToLocalTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime;
    }

    public Date StringToDate(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(time);
        return date;
    }

    public String DateToDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String[] dayOfWeekKorean = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};

        return dayOfWeekKorean[dayOfWeek - 1];
    }
}
