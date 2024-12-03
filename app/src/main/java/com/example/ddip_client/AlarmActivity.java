package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView alarmRecyclerView;
    private AlarmAdapter alarmAdapter;
    private List<String> alarmLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // ------------------ RecyclerView 초기화 ------------------
        alarmRecyclerView = findViewById(R.id.alarm_recycler_view);
        alarmLogList = new ArrayList<>();

        // RecyclerView 레이아웃 매니저 및 어댑터 설정
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(alarmLogList);
        alarmRecyclerView.setAdapter(alarmAdapter);

        // 테스트 데이터 추가
        loadTestAlarms();

        // ------------------ 점 세 개 메뉴 버튼 ------------------
        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this::showPopupMenu);

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        // 홈 버튼 클릭 시 홈 액티비티로 이동
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 문 버튼 클릭 시 크루룸 액티비티로 이동
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmActivity.this, ImsiCrewRoomListActivity.class);
            startActivity(intent);
        });

        // 알람 버튼은 현재 화면이므로 토스트만 출력
        alarmButton.setOnClickListener(v ->
                Toast.makeText(this, "알람 화면에 있습니다.", Toast.LENGTH_SHORT).show()
        );

        // 마이페이지 버튼 클릭 시 마이페이지 액티비티로 이동
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmActivity.this, MypageActivity.class);
            startActivity(intent);
        });
    }

    // 테스트 데이터를 로드하는 메서드
    private void loadTestAlarms() {
        alarmLogList.add("user3이 입장했습니다.");
        alarmLogList.add("user1과 user2가 근무를 교환했습니다.");
        alarmLogList.add("user4와 user1이 근무를 교환했습니다.");
        alarmLogList.add("user5가 입장했습니다.");
        alarmAdapter.notifyDataSetChanged();
    }

    // PopupMenu 표시 메서드
    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.alarm_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_clear_all) {
                alarmLogList.clear(); // 알람 로그 모두 삭제
                alarmAdapter.notifyDataSetChanged();
                Toast.makeText(this, "모든 알람이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
