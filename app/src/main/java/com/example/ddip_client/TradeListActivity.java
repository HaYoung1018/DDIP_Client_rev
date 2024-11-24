package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_TRADE && resultCode == RESULT_OK && data != null) {
            // 전달받은 데이터 추가
            String applicantName = data.getStringExtra("applicantName");
            String shiftTime = data.getStringExtra("shiftTime");

            tradeItems.add(new TradeItem(applicantName, shiftTime));
            adapter.notifyDataSetChanged(); // RecyclerView 새로고침
        }
    }
}
