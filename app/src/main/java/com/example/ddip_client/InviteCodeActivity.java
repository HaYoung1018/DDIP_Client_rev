package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.models.AddMemberRequest;
import com.example.ddip_client.models.InviteCodeRequest;
import com.example.ddip_client.models.InviteCodeResponse;
import com.example.ddip_client.network.InviteApiService;
import com.example.ddip_client.network.InviteApiServiceProvider;
import com.example.ddip_client.network.MemberService;
import com.example.ddip_client.network.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteCodeActivity extends AppCompatActivity {

    private EditText inviteCodeInput;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_code_input);

        inviteCodeInput = findViewById(R.id.invite_code_input);
        confirmButton = findViewById(R.id.confirm_button);

        // InviteApiService를 InviteApiServiceProvider를 통해 가져오기
        InviteApiService apiService = InviteApiServiceProvider.getInviteApiService();

        confirmButton.setOnClickListener(v -> {
            String inviteCode = inviteCodeInput.getText().toString().trim();
            if (inviteCode.isEmpty()) {
                Toast.makeText(this, "초대 코드를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 초대 코드 유효성 검사
                checkInviteCode(apiService, inviteCode);
            }
        });
    }

    private void checkInviteCode(InviteApiService apiService, String inviteCode) {
        InviteCodeRequest request = new InviteCodeRequest(inviteCode);

        apiService.checkInviteCode(request).enqueue(new Callback<InviteCodeResponse>() {
            @Override
            public void onResponse(Call<InviteCodeResponse> call, Response<InviteCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InviteCodeResponse inviteCodeResponse = response.body();
                    if (inviteCodeResponse.isValid()) {
                        int crewRoomId = inviteCodeResponse.getCrewRoomId();
                        fetchMemberDetailsAndAddToCrew(apiService, crewRoomId);
                    } else {
                        Toast.makeText(InviteCodeActivity.this, "잘못된 초대 코드입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InviteCodeActivity.this, "초대 코드 확인 실패.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InviteCodeResponse> call, Throwable t) {
                Toast.makeText(InviteCodeActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMemberDetailsAndAddToCrew(InviteApiService apiService, int crewRoomId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("userId", "");
        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        MemberService memberService = RetrofitClient.getClient().create(MemberService.class);

        memberService.findMemberDetails(memberId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().containsKey("contactNumber")) {
                    String contactNumber = response.body().get("contactNumber");
                    addMemberToCrew(apiService, crewRoomId, memberId, contactNumber);
                } else {
                    Log.e("fetchMemberDetails", "Failed to fetch user details: " + response.message());
                    addMemberToCrew(apiService, crewRoomId, memberId, "010-0000-0000"); // 기본 전화번호
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("fetchMemberDetails", "API call failed: " + t.getMessage());
                addMemberToCrew(apiService, crewRoomId, memberId, "010-0000-0000"); // 기본 전화번호
            }
        });
    }

    private void addMemberToCrew(InviteApiService apiService, int crewRoomId, String memberId, String contactNumber) {
        AddMemberRequest addMemberRequest = new AddMemberRequest(
                crewRoomId,
                memberId,
                "Red",
                contactNumber,
                "crew"
        );

        Log.d("InviteCodeActivity", "Adding member with data: " +
                "CrewRoomId: " + crewRoomId +
                ", MemberId: " + memberId +
                ", Color: " + addMemberRequest.getColor() +
                ", Contact: " + addMemberRequest.getContactNumber() +
                ", MemberType: " + addMemberRequest.getMemberType());

        apiService.addMemberToCrew(addMemberRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InviteCodeActivity.this, "크루룸에 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InviteCodeActivity.this, MainActivity.class);
                    intent.putExtra("crewRoomId", crewRoomId);
                    startActivity(intent);
                } else {
                    Log.e("InviteCodeActivity", "Failed to add member: " + response.code() + ", " + response.message());
                    Toast.makeText(InviteCodeActivity.this, "멤버 추가 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("InviteCodeActivity", "Error: " + t.getMessage());
                Toast.makeText(InviteCodeActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
