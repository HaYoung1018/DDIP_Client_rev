package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MypageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // ------------------ Header (상단바) ------------------
        TextView titleTextView = findViewById(R.id.title_text);
        titleTextView.setText("쿠잉");

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton calendarButton = findViewById(R.id.calendar_button);
        ImageButton crewRoomButton = findViewById(R.id.crew_room_button);
        ImageButton myPageButton = findViewById(R.id.mypage_button);

//        homeButton.setOnClickListener(v -> Toast.makeText(this, "홈 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        calendarButton.setOnClickListener(v -> Toast.makeText(this, "캘린더 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        crewRoomButton.setOnClickListener(v -> Toast.makeText(this, "알람 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        myPageButton.setOnClickListener(v -> Toast.makeText(this, "마이페이지 버튼 클릭됨", Toast.LENGTH_SHORT).show());

        // 홈 버튼 클릭 시 홈 화면 이동
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
            startActivity(intent);  // 마이페이지로 이동
        });

        // 크루룸 버튼 클릭 시 크루룸으로 이동
        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, CrewRoomActivity.class);
            startActivity(intent);  // 알람 액티비티로 이동
        });

        // 알람 버튼 클릭 시 알람 페이지로 이동
        crewRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, AlarmActivity.class);
            startActivity(intent);  // 알람 액티비티로 이동
        });

        // 마이페이지 버튼 클릭시 토스트 출력
        myPageButton.setOnClickListener(v -> {
            Toast.makeText(this, "마이페이지에 있습니다.", Toast.LENGTH_SHORT).show();
        });
    }
}
