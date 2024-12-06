package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.PayApiService;
import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.myPageService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUserType = sharedPreferences.getString("userType", "");

        // RecyclerView 설정
        crewRoomRecyclerView = findViewById(R.id.crew_room_list);
        crewRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // SharedPreferences에서 사용자 ID 가져오기
        String memberId = sharedPreferences.getString("userId", "");
        Log.d("SharedPreferences", "Retrieved userId: " + memberId);
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
        fetchCrewRooms(memberId);

        Button registerWorkButton = findViewById(R.id.register_work_button);
        registerWorkButton.setOnClickListener(v -> {
            Intent intent = new Intent(ImsiCrewRoomListActivity.this, InviteCodeActivity.class);
            startActivity(intent);
        });

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

    private void fetchCrewRooms(String memberId) {
        CrewRoomApiService crewRoomApiService = RetrofitClient.getClient().create(CrewRoomApiService.class);

        crewRoomApiService.getCrewRooms(memberId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Log.d("API Response", "Received data: " + response.body().toString());

                    crewRoomList.clear();
                    crewRoomList.addAll(response.body());

                    // 크루룸 데이터를 가져온 후 급여 데이터를 가져옵니다.
                    fetchSalariesForCrewRooms(memberId);
                } else if (response.isSuccessful() && response.body() != null && response.body().isEmpty()) {
                    Toast.makeText(ImsiCrewRoomListActivity.this, "속한 크루룸이 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.w("fetchCrewRooms", "No crew rooms found for memberId: " + memberId); // 데이터 없음
                } else {
                    Log.e("fetchCrewRooms", "Error fetching crew rooms. Response code: " + response.code()); // 실패 로그
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch crew rooms", t);
                Toast.makeText(ImsiCrewRoomListActivity.this, "크루룸 정보를 불러오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSalariesForCrewRooms(String memberId) {
        PayApiService payApiService = RetrofitClient.getClient().create(PayApiService.class);

        for (Map<String, String> crewRoom : crewRoomList) {
            String crewRoomId = crewRoom.get("crewRoomId");

            Log.d("fetchSalaries", "Fetching salary for crewRoomId: " + crewRoomId + ", memberId: " + memberId); // 요청 전 로그

            payApiService.getMemberMonthlyPay(Integer.parseInt(crewRoomId), memberId).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("fetchSalaries", "Salary data for crewRoomId " + crewRoomId + ": " + response.body()); // 성공 로그

                        // 월급 계산
                        double monthlyPay = response.body().values().stream()
                                .mapToDouble(value -> {
                                    if (value instanceof Map) {
                                        Map<String, Object> weekData = (Map<String, Object>) value;
                                        Object pay = weekData.get("pay");
                                        return pay instanceof Number ? ((Number) pay).doubleValue() : 0.0;
                                    }
                                    return 0.0;
                                })
                                .sum();
                        // 금액 포맷팅 (쉼표 추가)
                        String formattedSalary = NumberFormat.getInstance(Locale.getDefault()).format(monthlyPay);
                        crewRoom.put("monthlyPay",formattedSalary); // 월급 추가
                    } else {
                        Log.w("fetchSalaries", "No salary data for crewRoomId: " + crewRoomId); // 데이터 없음
                        crewRoom.put("monthlyPay", "0");
                    }
                    imsicrewRoomAdapter.notifyDataSetChanged(); // UI 갱신
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e("fetchSalaries", "Failed to fetch salary for crewRoomId: " + crewRoomId, t); // 네트워크 실패 로그
                    crewRoom.put("monthlyPay", "0");
                    imsicrewRoomAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}