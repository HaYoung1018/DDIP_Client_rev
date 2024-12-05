package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.models.CrewRoomMember;
import com.example.ddip_client.models.Member;
import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.MemberService;
import com.example.ddip_client.network.PayApiService;
import com.example.ddip_client.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private CrewRoomApiService api = RetrofitClient.getClient().create(CrewRoomApiService.class);
    private MemberService mapi = RetrofitClient.getClient().create(MemberService.class);
    private PayApiService pay = RetrofitClient.getClient().create(PayApiService.class);
    private List<CrewRoomMember> listedMembers = new ArrayList<CrewRoomMember>();
    private String name;
    private int i;
    private int j;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crewroom_userlist);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intentData = getIntent();
        String RoomId = intentData.getStringExtra("ROOM_ID"); // ROOM_ID 값 받기

        Call<List<Map<String, String>>> call = api.getCrewRoomMembers(RoomId);
        call.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (i = 0; i < response.body().size(); i++) {
                        Map<String, String> memberData = response.body().get(i);
                        CrewRoomMember member = new CrewRoomMember();
                        member.setMember(memberData.get("member"));
                        member.setcrewRoomMemberId(Integer.parseInt(memberData.get("crewRoomMemberId")));
                        member.setmemberType(memberData.get("memberType"));
                        member.setcolor(memberData.get("color"));
                        member.setcontactNumber(memberData.get("contactNumber"));
                        member.setcrewRoom(Integer.parseInt(memberData.get("crewRoom")));

                        listedMembers.add(member);
                    }

                    userAdapter = new UserAdapter(new ArrayList<>(), UserListActivity.this); // this는 Context를 전달
                    recyclerView.setAdapter(userAdapter);


                    for (CrewRoomMember listedMember : listedMembers) {
                        String id = listedMember.getMember();
                        String contact = listedMember.getcontactNumber();
                        int crewRoomId = listedMember.getcrewRoom();

                        Call<Map<String, String>> mcall = mapi.findMemberById(id);
                        mcall.enqueue(new Callback<Map<String, String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    String name = response.body().get("name");

                                    // 급여와 근무시간 API 호출
                                    Call<Map<String, Object>> payCall = pay.getMemberMonthlyPay(crewRoomId, id);
                                    payCall.enqueue(new Callback<Map<String, Object>>() {
                                        @Override
                                        public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                // 급여와 근무시간 계산
                                                double totalSalary = 0;
                                                double totalHours = 0;
                                                for (Object value : response.body().values()) {
                                                    if (value instanceof Map) {
                                                        Map<String, Object> weekData = (Map<String, Object>) value;
                                                        totalSalary += ((Number) weekData.get("pay")).doubleValue();
                                                        totalHours += ((Number) weekData.get("hours")).doubleValue();
                                                    }
                                                }

                                                // User 추가
                                                addUser(new User(name, contact, R.drawable.ic_user_profile, (int) totalSalary, (int) totalHours));
                                            } else {
                                                Toast.makeText(UserListActivity.this, "급여 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                            Toast.makeText(UserListActivity.this, "급여 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(UserListActivity.this, "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                Toast.makeText(UserListActivity.this, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(UserListActivity.this, "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Toast.makeText(UserListActivity.this, "요청 실패.", Toast.LENGTH_SHORT).show();
                System.out.println(t);
            }
        });

        // 예시로 사용자 추가
//        addUser(new User("사용자 이름 1", "010-1234-5678", R.drawable.ic_user_profile));
//        addUser(new User("사용자 이름 2", "010-8765-4321", R.drawable.ic_user_profile));

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUserType = sharedPreferences.getString("userType", "");

        // 홈 버튼 클릭 시 홈 액티비티로 이동
        homeButton.setOnClickListener(v -> {
            if(savedUserType.equals("Owner")){
                Intent intent = new Intent(UserListActivity.this, OwnerMainActivity.class);
                startActivity(intent);
                finish();
            } else if (savedUserType.equals("Staff")) {
                Intent intent = new Intent(UserListActivity.this, StaffMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(UserListActivity.this, "사용자 종류가 저장되지 않았습니다. 로그아웃 후 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        // 문 버튼 클릭 시 크루룸 액티비티로 이동
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, ImsiCrewRoomListActivity.class);
            startActivity(intent);
            finish();
        });

        // 알람 버튼 클릭 시 알람 액티비티로 이동
        alarmButton.setOnClickListener(v ->{
            Intent intent = new Intent(UserListActivity.this, AlarmActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭 시 마이페이지 액티비티로 이동
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, MypageActivity.class);
            startActivity(intent);
        });
    }

    private void addUser(User user) {
        userAdapter.addUser(user);
    }
}