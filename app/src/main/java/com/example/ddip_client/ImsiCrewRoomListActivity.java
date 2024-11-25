package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.myPageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImsiCrewRoomListActivity extends AppCompatActivity {

    private RecyclerView crewRoomRecyclerView;
    private ImsiCrewRoomAdapter imsicrewRoomAdapter; // 기존 MemberAdapter -> CrewRoomAdapter로 변경
    private List<Map<String, String>> crewRoomList = new ArrayList<>();// 크루룸 리스트 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crew_room_list);

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
        imsicrewRoomAdapter = new ImsiCrewRoomAdapter(this, crewRoomList, (roomId, roomName) -> {
            // 새로운 액티비티로 이동(ImsiCrewRoomActivity)
            Intent intent = new Intent(ImsiCrewRoomListActivity.this, ImsiCrewRoomActivity.class);
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
                Toast.makeText(ImsiCrewRoomListActivity.this, "크루룸 정보를 불러오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }


        //여기까지 새로 추가



//        // 초기 크루룸 리스트 설정
//        crewRoomList = new ArrayList<>();
//        crewRoomList.add("크루룸 A");
//        crewRoomList.add("크루룸 B");
//        crewRoomList.add("크루룸 C");

//        // 어댑터 설정
//        imsicrewRoomAdapter = new ImsiCrewRoomAdapter(this, crewRoomList, roomName -> {
//            // 크루룸 클릭 시 처리
//            Toast.makeText(this, "선택된 크루룸: " + roomName, Toast.LENGTH_SHORT).show();
//        });

//        crewRoomRecyclerView.setAdapter(imsicrewRoomAdapter);

        // 근무 등록 버튼 설정
//        Button registerWorkButton = findViewById(R.id.register_work_button);
//        registerWorkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 근무 등록 후 크루룸 추가
//                addNewCrewRoom("새로운 크루룸");
//            }
//        });
    }

    // 새로운 크루룸 추가 함수
//    private void addNewCrewRoom(String crewRoomName) {
//        crewRoomList.add(crewRoomName); // 리스트에 새로운 크루룸 추가
//        imsicrewRoomAdapter.notifyDataSetChanged(); // 리스트 갱신
//    }

