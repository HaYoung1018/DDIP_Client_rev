package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeListActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_TRADE = 1; // 요청 코드
    private List<Map<String, Object>> tradeItems;
    private TradeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradelist);

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


        // RecyclerView 초기화
        RecyclerView tradeRecyclerView = findViewById(R.id.recycler_view);
        tradeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터 준비
        tradeItems = new ArrayList<>();
        adapter = new TradeAdapter(tradeItems, memberId); // memberId를 TradeAdapter에 전달
        tradeRecyclerView.setAdapter(adapter);

        // 교환 가능한 리스트 불러오기
        fetchExchangeableSchedules(roomId);

        // 교환 요청 생성 버튼
        Button createTradeButton = findViewById(R.id.create_trade_button);
        createTradeButton.setOnClickListener(v -> {
            // CreateTradeActivity 호출
            Intent intent = new Intent(TradeListActivity.this, CreateTradeActivity.class);
            intent.putExtra("ROOM_ID", roomId); // roomId 전달
            intent.putExtra("ROOM_NAME", roomName); // roomName 전달
            startActivityForResult(intent, REQUEST_CREATE_TRADE);
        });

        // ------------------ Bottom Navigation (하단 네비게이션 바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        // 홈 버튼 클릭 리스너 설정
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(TradeListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 서브크루룸 이동 버튼 클릭 리스너 설정
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(TradeListActivity.this, ImsiCrewRoomListActivity.class);
            startActivity(intent);
        });

        // 알람 버튼 클릭 리스너 설정
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(TradeListActivity.this, AlarmActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭 리스너 설정
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(TradeListActivity.this, MypageActivity.class);
            startActivity(intent);
        });

    }



    private void fetchExchangeableSchedules(String roomId) {
        // Retrofit API 호출
        ScheduleApiService scheduleService = RetrofitClient.getClient().create(ScheduleApiService.class);
        scheduleService.getExchangeableSchedules(roomId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> schedules = response.body();

                    // 데이터를 RecyclerView의 리스트로 변환
                    tradeItems.addAll(schedules);
                    adapter.notifyDataSetChanged(); // 리스트 업데이트
                } else {
                    Toast.makeText(TradeListActivity.this, "교환 가능한 스케줄을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch exchangeable schedules", t);
                Toast.makeText(TradeListActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
