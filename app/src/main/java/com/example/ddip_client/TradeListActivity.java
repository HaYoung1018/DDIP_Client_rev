package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TradeListActivity extends AppCompatActivity {

    private static final int REQUEST_CREATE_TRADE = 1; // 요청 코드
    private List<TradeItem> tradeItems;
    private TradeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradelist);

        // RecyclerView 초기화
        RecyclerView tradeRecyclerView = findViewById(R.id.recycler_view);
        tradeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터 준비
        tradeItems = new ArrayList<>();
        adapter = new TradeAdapter(tradeItems);
        tradeRecyclerView.setAdapter(adapter);

        // 교환 요청 생성 버튼
        Button createTradeButton = findViewById(R.id.create_trade_button);
        createTradeButton.setOnClickListener(v -> {
            // CreateTradeActivity 호출
            Intent intent = new Intent(TradeListActivity.this, CreateTradeActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_TRADE && resultCode == RESULT_OK && data != null) {
            String selectedWorkTime = data.getStringExtra("selectedWorkTime");

            tradeItems.add(new TradeItem("홍길동", selectedWorkTime, "10:00", "14:00", "4시간"));
            adapter.notifyDataSetChanged();
        }
    }

}
