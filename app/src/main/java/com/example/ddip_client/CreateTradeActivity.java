package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class CreateTradeActivity extends AppCompatActivity {

    private String selectedWorkTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trade);

        RecyclerView workTimeRecyclerView = findViewById(R.id.work_time_recycler_view);
        TextView selectedWorkTimeTextView = findViewById(R.id.selected_work_time);
        Button submitTradeButton = findViewById(R.id.submit_trade_button);

        // 근무 시간 목록 데이터
        List<String> workTimes = Arrays.asList(
                "2024-11-26 09:00 ~ 13:00",
                "2024-11-26 14:00 ~ 18:00",
                "2024-11-27 10:00 ~ 14:00"
        );

        // RecyclerView 설정
        WorkTimeAdapter adapter = new WorkTimeAdapter(workTimes, workTime -> {
            selectedWorkTime = workTime;
            selectedWorkTimeTextView.setText("선택된 시간: " + workTime);
        });
        workTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workTimeRecyclerView.setAdapter(adapter);

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
