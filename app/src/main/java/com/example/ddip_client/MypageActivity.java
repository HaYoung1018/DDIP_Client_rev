package com.example.ddip_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddip_client.models.Member;
import com.example.ddip_client.network.myPageService;
import com.example.ddip_client.network.MemberService;
import com.example.ddip_client.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MypageActivity extends AppCompatActivity {
    // 로그인 된 계정의 id 불러오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedId = sharedPreferences.getString("userId", "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        //-------------------------프로필에 데이터 뿌리기------------------------
        TextView profileName = findViewById(R.id.username_text);
        TextView profileEmail = findViewById(R.id.user_email_text);



        // 로그인된 계정의 Member객체 생성
        myPageService mypageservice = RetrofitClient.getClient().create(myPageService.class);
        Call<Map<String, String>> collectData = mypageservice.collectData(savedId);

        collectData.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().get("name");
                    String id = response.body().get("id");
                    String email = response.body().get("email");
                    String password = response.body().get("password");
                    String contactNumber = response.body().get("contactNumber");
                    String userType = response.body().get("userType");
                    Member user = new Member(id, password, name, email, userType, contactNumber);

                    profileName.setText(user.getName());
                    profileEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(MypageActivity.this, "회원 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(MypageActivity.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // ------------------ Header (상단바) ------------------
        TextView titleTextView = findViewById(R.id.title_text);
        titleTextView.setText("쿠잉");

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton calendarButton = findViewById(R.id.calendar_button);
        ImageButton crewRoomButton = findViewById(R.id.crew_room_button);
        ImageButton myPageButton = findViewById(R.id.mypage_button);

//        homeButton.setOnClickListener(v -> Toast.makeText(this, "홈 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        calendarButton.setOnClickListener(v -> Toast.makeText(this, "캘린더 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        crewRoomButton.setOnClickListener(v -> Toast.makeText(this, "알람 버튼 클릭됨", Toast.LENGTH_SHORT).show());
//        myPageButton.setOnClickListener(v -> Toast.makeText(this, "마이페이지 버튼 클릭됨", Toast.LENGTH_SHORT).show());


        //--------------------(유우선) 임시 개인정보 수정--------------------
        TextView editMyData = findViewById(R.id.edit_profile_text);
        editMyData.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, MemberDataEditActivity.class);
            startActivity(intent);
        });


        //---------------------(유우선) 임시 로그아웃------------------------
        TextView logoutButton = findViewById(R.id.logout_text);
        logoutButton.setOnClickListener(V -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.remove("userPassword");
            editor.commit();
            restartApplication(this);
        });

        //---------------------(유우선) 임시 탈퇴------------------------
        TextView withdrawButton = findViewById(R.id.unsignup_text);
        withdrawButton.setOnClickListener(v -> {
            MemberService memberService = RetrofitClient.getClient().create(MemberService.class);
            Call<Map<String, String>> call = memberService.withdrawMember(savedId);
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        String responseMassage = response.body().get("message");
                        Toast.makeText(MypageActivity.this, responseMassage, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("userId");
                        editor.remove("userPassword");
                        editor.commit();
                        restartApplication(MypageActivity.this);
                    } else {
                        Toast.makeText(MypageActivity.this, "탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(MypageActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
                }
            });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.remove("userPassword");
            editor.apply();
            // 탈뢰 로직 추가하기
        });

        // 홈 버튼 클릭 시 홈 화면 이동
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
            startActivity(intent);  // 마이페이지로 이동
        });

        // 크루룸 버튼 클릭 시 크루룸으로 이동
        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, CrewRoomActivity.class);
            startActivity(intent);  // 알람 액티비티로 이동
        });

        // 알람 버튼 클릭 시 알람 페이지로 이동
        crewRoomButton.setOnClickListener(v -> {
            Intent intent = new Intent(MypageActivity.this, AlarmActivity.class);
            startActivity(intent);  // 알람 액티비티로 이동
        });

        // 마이페이지 버튼 클릭시 토스트 출력
        myPageButton.setOnClickListener(v -> {
            Toast.makeText(this, "마이페이지에 있습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    // 어플리케이션 재시작 함수
    private void restartApplication(Context mContext){
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(mContext.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        mContext.startActivity(mainIntent);
        System.exit(0);
    }
}
