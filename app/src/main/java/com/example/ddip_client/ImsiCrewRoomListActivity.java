package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class ImsiCrewRoomListActivity extends AppCompatActivity {

    private RecyclerView crewRoomRecyclerView;
    private ImsiCrewRoomAdapter imsicrewRoomAdapter;
    private List<Map<String, String>> crewRoomList = new ArrayList<>();

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

        Log.d("SharedPreferences", "Retrieved userId: " + memberId);

        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동
            //Intent intent = new Intent(ImsiCrewRoomListActivity.this, LoginSignupActivity.class);
            //startActivity(intent);
            //finish();
            return;
        }

        // 어댑터 초기화
        imsicrewRoomAdapter = new ImsiCrewRoomAdapter(this, crewRoomList, (roomId, roomName) -> {
            Intent intent = new Intent(ImsiCrewRoomListActivity.this, ImsiCrewRoomActivity.class);
            intent.putExtra("ROOM_ID", roomId);
            intent.putExtra("ROOM_NAME", roomName);
            startActivity(intent);
        });

        crewRoomRecyclerView.setAdapter(imsicrewRoomAdapter);

        // Retrofit API 호출
        fetchCrewRooms(memberId);
    }

    private void fetchCrewRooms(String memberId) {
        CrewRoomApiService crewRoomApiService = RetrofitClient.getClient().create(CrewRoomApiService.class);

        crewRoomApiService.getCrewRooms(memberId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d("API Response", "Received data: " + response.body().toString());

                    crewRoomList.clear();
                    crewRoomList.addAll(response.body());
                    imsicrewRoomAdapter.notifyDataSetChanged();
                } else if (response.isSuccessful() && response.body() != null && response.body().isEmpty()) {
                    Toast.makeText(ImsiCrewRoomListActivity.this, "속한 크루룸이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API Error", "Response not successful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Log.e("API Failure", "Error fetching crew rooms", t);
                Toast.makeText(ImsiCrewRoomListActivity.this, "서버와 연결에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
