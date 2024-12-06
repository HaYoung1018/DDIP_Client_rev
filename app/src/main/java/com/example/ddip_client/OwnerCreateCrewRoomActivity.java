package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ddip_client.models.CrewRoom;
import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;
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
        setContentView(R.layout.owner_create_crew_room); // 여기서 XML 파일을 설정합니다. 이 파일명은 실제 XML 파일명과 일치해야 합니다.

        // View들 초기화
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

        // 크루룸장(점주명)은 자동으로 불러오도록할 예정
        crewRoomLeaderName.setText(userId);

        // 버튼 클릭 이벤트
        createCrewRoomButton.setOnClickListener(v -> createCrewRoom(userId));
    }

    private void createCrewRoom(String userId) {
        String crewRoomName = crewRoomNameInput.getText().toString().trim();
        String shopName = storeNameInput.getText().toString().trim();

        // 필수 필드 유효성 검사
        if (crewRoomName.isEmpty() || shopName.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // CrewRoom 객체 생성
        CrewRoom crewRoom = new CrewRoom();
        crewRoom.setCrewRoomName(crewRoomName);
        crewRoom.setShopName(shopName);
        crewRoom.setOwner(userId); // 사용자 ID를 owner로 설정

        // Retrofit API 호출
        CrewRoomApiService apiService = RetrofitClient.getClient().create(CrewRoomApiService.class);
        Call<CrewRoom> call = apiService.createCrewRoom(crewRoom);
        call.enqueue(new Callback<CrewRoom>() {
            @Override
            public void onResponse(Call<CrewRoom> call, Response<CrewRoom> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OwnerCreateCrewRoomActivity.this, "크루룸 생성 완료!", Toast.LENGTH_SHORT).show();

                    // 크루룸 생성이 완료되면 OwnerInviteCodeInputActivity로 이동
                    Intent intent = new Intent(OwnerCreateCrewRoomActivity.this, OwnerInviteCodeInputActivity.class);
                    startActivity(intent);

                    // 액티비티 종료 (필요하다면 추가)
                    finish();
                } else {
                    Toast.makeText(OwnerCreateCrewRoomActivity.this, "크루룸 생성 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrewRoom> call, Throwable t) {
                Toast.makeText(OwnerCreateCrewRoomActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
