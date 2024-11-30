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

        scheduleService.getSchedules(roomId, memberId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> schedules = response.body();

                    // RecyclerView에 표시할 데이터 준비
                    List<String> scheduleDisplayList = new ArrayList<>();
                    for (Map<String, Object> schedule : schedules) {
                        // 날짜와 시간을 가져와 원하는 형식으로 변환
                        String displayText = schedule.get("date").toString() + " " +
                                schedule.get("startTime").toString() + " ~ " +
                                schedule.get("endTime").toString();
                        scheduleDisplayList.add(displayText);
                    }

                    // RecyclerView 설정
                    WorkTimeAdapter adapter = new WorkTimeAdapter(scheduleDisplayList, workTime -> {
                        selectedWorkTime = workTime;
                        selectedWorkTimeTextView.setText("선택된 시간: " + workTime);
                    });
                    workTimeRecyclerView.setLayoutManager(new LinearLayoutManager(CreateTradeActivity.this));
                    workTimeRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch schedules", t);
            }
        });

        // 교환 신청 버튼 클릭 이벤트
        submitTradeButton.setOnClickListener(v -> {
            if (selectedWorkTime.isEmpty()) {
                // 선택되지 않은 경우 메시지 표시
                Toast.makeText(this, "근무 시간을 선택하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 선택된 시간 데이터를 반환
                Intent intent = new Intent();
                intent.putExtra("selectedWorkTime", selectedWorkTime);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
