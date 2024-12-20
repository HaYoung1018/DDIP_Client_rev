package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerCrewRoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OwnerCrewRoomAdapter adapter;
    private List<Map<String, String>> crewRoomList = new ArrayList<>();
    private Button createCrewRoomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_crew_room_list);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUserType = sharedPreferences.getString("userType", "");

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.crew_room_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 생성 버튼 초기화
        createCrewRoomButton = findViewById(R.id.create_crew_room_button);
        createCrewRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerCrewRoomListActivity.this, OwnerCreateCrewRoomActivity.class);
            startActivity(intent);
        });

        // 어댑터 설정
        adapter = new OwnerCrewRoomAdapter(this, crewRoomList, this::onCrewRoomItemClick);
        recyclerView.setAdapter(adapter);

        // 서버에서 데이터 로드
        loadCrewRoomData();

        // ------------------ Bottom Navigation (하단 네비게이션 바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        // 홈 버튼 클릭 리스너 설정
        homeButton.setOnClickListener(v -> {
            if (savedUserType.equals("Owner")){
                Intent intent = new Intent(this, OwnerMainActivity.class);
                startActivity(intent);
                finish();
            } else if (savedUserType.equals("Staff")) {
                Intent intent = new Intent(this, StaffMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "사용자 종류가 저장되지 않았습니다. 로그아웃 후 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 문 버튼 클릭 시 크루룸 액티비티로 이동
        subCrewButton.setOnClickListener(v -> Toast.makeText(this, "서브크루 화면에 있습니다.", Toast.LENGTH_SHORT).show());

        // 알람 버튼 클릭 리스너 설정
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭 리스너 설정
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MypageActivity.class);
            startActivity(intent);
        });
    }

    private void loadCrewRoomData() {
        // SharedPreferences에서 사용자 ID 가져오기
        String userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("userId", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrofit API 호출
        CrewRoomApiService apiService = RetrofitClient.getClient().create(CrewRoomApiService.class);
        Call<List<Map<String, String>>> callWithInvitation = apiService.getCrewRoomsWithInvitation(userId);

        callWithInvitation.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 기존 crewRoomList를 비우고 새 데이터를 추가
                    crewRoomList.clear();
                    crewRoomList.addAll(response.body());

                    // RecyclerView Adapter 갱신
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OwnerCrewRoomListActivity.this, "크루룸 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("CrewRoomList", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Toast.makeText(OwnerCrewRoomListActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CrewRoomList", "API call failed: " + t.getMessage());
            }
        });
    }

    private void onCrewRoomItemClick(String roomId, String roomName) {
        // 클릭된 크루룸에 대한 동작 처리
        //Toast.makeText(this, "Clicked: " + roomName, Toast.LENGTH_SHORT).show();

        // 선택된 크루룸 상세 페이지로 이동
        Intent intent = new Intent(this, OwnerCrewRoomActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("ROOM_NAME", roomName);
        startActivity(intent);
    }
}
