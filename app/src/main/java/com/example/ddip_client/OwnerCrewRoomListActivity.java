package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.SharedPreferences;
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

    private RecyclerView crewRoomRecyclerView;
    private OwnerCrewRoomAdapter imsicrewRoomAdapter; // 어댑터 이름 변경
    private List<Map<String, String>> crewRoomList = new ArrayList<>(); // 크루룸 데이터 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_crew_room_list);

        // RecyclerView 설정
        crewRoomRecyclerView = findViewById(R.id.crew_room_list);
        crewRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("userId", "");
        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 어댑터 초기화
        imsicrewRoomAdapter = new OwnerCrewRoomAdapter(this, crewRoomList, (roomId, roomName) -> {
            // 새로운 액티비티로 이동
            Intent intent = new Intent(OwnerCrewRoomListActivity.this, OwnerCrewRoomActivity.class);
            intent.putExtra("ROOM_ID", roomId); // roomId 전달
            intent.putExtra("ROOM_NAME", roomName); // roomName 전달
            startActivity(intent);
        });

        crewRoomRecyclerView.setAdapter(imsicrewRoomAdapter);

        // Retrofit API 호출
        CrewRoomApiService crewRoomApiService = RetrofitClient.getClient().create(CrewRoomApiService.class);

        crewRoomApiService.getCrewRooms(memberId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    crewRoomList.clear();
                    crewRoomList.addAll(response.body());
                    imsicrewRoomAdapter.notifyDataSetChanged(); // RecyclerView 데이터 갱신
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch crew rooms", t);
                Toast.makeText(OwnerCrewRoomListActivity.this, "크루룸 정보를 불러오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        // 근무 등록 버튼 설정
        Button CreateCrewRoomButton = findViewById(R.id.create_crew_room_button);
        CreateCrewRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerCrewRoomListActivity.this, OwnerCreateCrewRoomActivity.class);
            startActivity(intent);
        });
    }
}

/*
        // 초기 크루룸 리스트 설정
        crewRoomList = new ArrayList<>();
        crewRoomList.add(new OwnerCrewRoom("크루룸 A", "초대코드: EV99G85S"));
        crewRoomList.add(new OwnerCrewRoom("크루룸 B", "초대코드: AB12CD34"));
        crewRoomList.add(new OwnerCrewRoom("크루룸 C", "초대코드: XY98ZT76"));

        // 어댑터 설정
        imsicrewRoomAdapter = new OwnerCrewRoomAdapter(this, crewRoomList);
        crewRoomRecyclerView.setAdapter(imsicrewRoomAdapter);

        // 근무 등록 버튼 설정
        Button registerWorkButton = findViewById(R.id.register_work_button);
        registerWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 크루룸 추가
                addNewCrewRoom("새로운 크루룸", "초대코드: NEW12345");
            }
        });
    }

    // 새로운 크루룸 추가 함수
    private void addNewCrewRoom(String crewRoomName, String inviteCode) {
        crewRoomList.add(new OwnerCrewRoom(crewRoomName, inviteCode)); // 리스트에 새로운 크루룸 추가
        imsicrewRoomAdapter.notifyDataSetChanged(); // 리스트 갱신
        Toast.makeText(this, crewRoomName + "이(가) 추가되었습니다.", Toast.LENGTH_SHORT).show();
    }
}*/
