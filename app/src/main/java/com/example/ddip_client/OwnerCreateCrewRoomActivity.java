package com.example.ddip_client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.models.AddMemberRequest;
import com.example.ddip_client.models.CreateCrewRoomResponse;
import com.example.ddip_client.models.CrewRoom;
import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.InviteApiService;
import com.example.ddip_client.network.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerCreateCrewRoomActivity extends AppCompatActivity {
    private EditText crewRoomNameInput;
    private EditText storeNameInput;
    private EditText crewRoomLeaderName;
    private Button createCrewRoomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_create_crew_room);

        // View 초기화
        crewRoomNameInput = findViewById(R.id.crew_room_name_input);
        storeNameInput = findViewById(R.id.store_name_input);
        crewRoomLeaderName = findViewById(R.id.crew_room_leader_name);
        createCrewRoomButton = findViewById(R.id.create_crew_room_button);

        // SharedPreferences에서 userId 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        if (userId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("SharedPreferences", "userId for crew creation: " + userId);
        crewRoomLeaderName.setText(userId);

        // 버튼 클릭 이벤트
        createCrewRoomButton.setOnClickListener(v -> createCrewRoom(userId));
    }

    private void createCrewRoom(String userId) {
        String crewRoomName = crewRoomNameInput.getText().toString().trim();
        String shopName = storeNameInput.getText().toString().trim();

        if (crewRoomName.isEmpty() || shopName.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // CrewRoom 객체 생성
        CrewRoom crewRoom = new CrewRoom();
        crewRoom.setCrewRoomName(crewRoomName);
        crewRoom.setShopName(shopName);
        crewRoom.setOwner(userId);

        // Retrofit API 호출
        CrewRoomApiService apiService = RetrofitClient.getClient().create(CrewRoomApiService.class);
        Call<CreateCrewRoomResponse> call = apiService.createCrewRoom(crewRoom);

        call.enqueue(new Callback<CreateCrewRoomResponse>() {
            @Override
            public void onResponse(Call<CreateCrewRoomResponse> call, Response<CreateCrewRoomResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CreateCrewRoomResponse createResponse = response.body();

                    // 로그로 응답 데이터 출력
                    Log.d("OwnerCreateCrewRoom", "Raw Response JSON: " + new Gson().toJson(createResponse));
                    Log.d("OwnerCreateCrewRoom", "Received CrewRoom ID: " + createResponse.getId());

                    if (createResponse.getId() != null) {
                        Toast.makeText(OwnerCreateCrewRoomActivity.this,
                                "크루룸 생성 완료! ID: " + createResponse.getId(),
                                Toast.LENGTH_SHORT).show();

                        // Owner를 추가
                        addOwnerToCrew(Integer.parseInt(createResponse.getId()), userId);
                    } else {
                        Toast.makeText(OwnerCreateCrewRoomActivity.this, "생성된 크루룸 ID를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("OwnerCreateCrewRoom", "CrewRoom ID is null in the response.");
                    }
                } else {
                    Toast.makeText(OwnerCreateCrewRoomActivity.this, "크루룸 생성 실패!", Toast.LENGTH_SHORT).show();
                    Log.e("OwnerCreateCrewRoom", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CreateCrewRoomResponse> call, Throwable t) {
                Toast.makeText(OwnerCreateCrewRoomActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OwnerCreateCrewRoom", "API call failed: " + t.getMessage());
            }
        });
    }

    private void addOwnerToCrew(int crewRoomId, String userId) {
        InviteApiService inviteApiService = RetrofitClient.getClient().create(InviteApiService.class);

        AddMemberRequest addMemberRequest = new AddMemberRequest(
                crewRoomId,  // 생성된 크루룸 ID
                userId,      // 점주 ID
                "Owner",     // 기본 색상
                "010-0000-0000", // 기본 연락처
                "owner"      // 점주의 멤버 타입
        );

        // 요청 데이터를 로그로 출력
        Log.d("AddOwnerToCrew", "Request Body: " + new Gson().toJson(addMemberRequest));

        inviteApiService.addMemberToCrew(addMemberRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("AddOwnerToCrew", "Owner added successfully to crew room.");
                } else {
                    Log.e("AddOwnerToCrew", "Failed to add owner: " + response.code() + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AddOwnerToCrew", "API call failed: " + t.getMessage());
            }
        });
    }
}