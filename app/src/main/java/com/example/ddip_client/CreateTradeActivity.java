package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTradeActivity extends AppCompatActivity {

    private String selectedWorkTime = "";
    private int selectedScheduleId = -1; // 선택된 스케줄 ID 저장 (-1은 초기값)


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trade);

        RecyclerView workTimeRecyclerView = findViewById(R.id.work_time_recycler_view);
        TextView selectedWorkTimeTextView = findViewById(R.id.selected_work_time);
        Button submitTradeButton = findViewById(R.id.submit_trade_button);

        ScheduleApiService scheduleService = RetrofitClient.getClient().create(ScheduleApiService.class);

        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("userId", "");
        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 전달된 데이터 받기
        Intent intentData = getIntent();
        String roomId = intentData.getStringExtra("ROOM_ID"); // ROOM_ID 값 받기
        String roomName = intentData.getStringExtra("ROOM_NAME"); // ROOM_NAME 값 받기

        // RecyclerView 데이터 및 어댑터 준비
        List<String> scheduleDisplayList = new ArrayList<>();
        List<Integer> scheduleIds = new ArrayList<>(); // 각 스케줄 ID를 저장

        scheduleService.getSchedules(roomId, memberId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> schedules = response.body();

                    // RecyclerView에 표시할 데이터 준비
                    List<String> scheduleDisplayList = new ArrayList<>();
                    for (Map<String, Object> schedule : schedules) {
                        // 스케줄 ID, 날짜, 시작 시간, 종료 시간을 가져와 원하는 형식으로 변환
                        int scheduleId = ((Number) schedule.get("scheduleId")).intValue(); // 정수형으로 변환
                        // 날짜와 시간을 가져와 원하는 형식으로 변환
                        String displayText = scheduleId + ". " + // 스케줄 ID 추가
                                schedule.get("date").toString() + " " +
                                schedule.get("startTime").toString() + " ~ " +
                                schedule.get("endTime").toString();
                        scheduleIds.add(scheduleId); // ID 저장
                        scheduleDisplayList.add(displayText); // 표시 텍스트 저장
                    }
                    // RecyclerView 어댑터 설정
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateTradeActivity.this,
                            android.R.layout.simple_list_item_1, scheduleDisplayList);
                    workTimeRecyclerView.setLayoutManager(new LinearLayoutManager(CreateTradeActivity.this));
                    workTimeRecyclerView.setAdapter(new WorkTimeAdapter(scheduleDisplayList, position -> {
                        // 항목 클릭 시 동작
                        selectedScheduleId = scheduleIds.get(position); // 선택된 스케줄 ID 저장
                        selectedWorkTime = scheduleDisplayList.get(position); // 선택된 작업 시간 저장
                        selectedWorkTimeTextView.setText("선택된 시간: " + selectedWorkTime);
                    }));
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch schedules", t);
            }
        });


        // 교환 신청 버튼 클릭 이벤트
        submitTradeButton.setOnClickListener(v -> {
            if (selectedWorkTime.isEmpty() || selectedScheduleId == -1) {
                // 선택되지 않은 경우 메시지 표시
                Toast.makeText(this, "근무 시간을 선택하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Call<Void> call = scheduleService.updateScheduleStatus(selectedScheduleId); // 선택된 스케줄 ID 사용

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // 성공 메시지 표시
                            Toast.makeText(CreateTradeActivity.this, "교환 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish(); // 화면 종료
                        } else {
                            // 실패 메시지 표시
                            Toast.makeText(CreateTradeActivity.this, "교환 신청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // 에러 메시지 표시
                        Toast.makeText(CreateTradeActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
