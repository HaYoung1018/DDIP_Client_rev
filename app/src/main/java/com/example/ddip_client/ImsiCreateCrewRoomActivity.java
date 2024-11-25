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

public class ImsiCreateCrewRoomActivity extends AppCompatActivity {

    private EditText editCrewRoomName, editShopName;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imsicreate_crewroom);

        // UI 요소 초기화
        editCrewRoomName = findViewById(R.id.edit_crewRoomName);
        editShopName = findViewById(R.id.edit_shopName);
        btnCreate = findViewById(R.id.btn_create);

        // SharedPreferences에서 userId 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다. 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동
            //Intent intent = new Intent(ImsiCreateCrewRoomActivity.this, LoginSignupActivity.class);
            //startActivity(intent);
            //finish();
            return;
        }

        Log.d("SharedPreferences", "userId for crew creation: " + userId);

        // 버튼 클릭 이벤트
        btnCreate.setOnClickListener(v -> createCrewRoom(userId));
    }

    private void createCrewRoom(String userId) {
        String crewRoomName = editCrewRoomName.getText().toString().trim();
        String shopName = editShopName.getText().toString().trim();

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
                    Toast.makeText(ImsiCreateCrewRoomActivity.this, "크루룸 생성 완료!", Toast.LENGTH_SHORT).show();
                    finish(); // Activity 종료
                } else {
                    Toast.makeText(ImsiCreateCrewRoomActivity.this, "크루룸 생성 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrewRoom> call, Throwable t) {
                Toast.makeText(ImsiCreateCrewRoomActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
