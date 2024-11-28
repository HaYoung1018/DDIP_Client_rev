package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.models.CrewRoomMember;
import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private CrewRoomApiService api = RetrofitClient.getClient().create(CrewRoomApiService.class);
    private List<CrewRoomMember> members = new ArrayList<CrewRoomMember>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crewroom_userlist);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intentData = getIntent();
        String RoomId = intentData.getStringExtra("ROOM_ID"); // ROOM_ID 값 받기

        Call <List<Map<String, String >>> call = api.getCrewRoomMembers(RoomId);
        call.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if(response.isSuccessful() && response.body() != null){
                    for(int i = 0; i < response.body().size(); i++){
                        Map<String, String > memberData = response.body().get(i);
                        CrewRoomMember member = new CrewRoomMember();
                        member.setMember(memberData.get("member"));
                        member.setcrewRoomMemberId(Integer.parseInt(memberData.get("crewRoomMemberId")));
                        member.setmemberType(memberData.get("memberType"));
                        member.setcolor(memberData.get("color"));
                        member.setcontactNumber(memberData.get("contactNumber"));
                        member.setcrewRoom(Integer.parseInt(memberData.get("crewRoom")));
                        String strDate = memberData.get("startDate");
                        try {
                            // 문자열을 LocalDate로 변환
                            LocalDate localDate = LocalDate.parse(strDate, formatter);
                            // LocalDate를 java.util.Date로 변환
                            Date date = java.sql.Date.valueOf(String.valueOf(localDate));
                            member.setstartDate(date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        member.getall();
                        members.add(member);
                    }

                    List<User> userList = new ArrayList<>();
                    userAdapter = new UserAdapter(userList);
                    recyclerView.setAdapter(userAdapter);

                    for(int i = 0; i < members.size(); i++){
                        String name = members.get(i).getMember();
                        String contactNumber = members.get(i).getcontactNumber();

                        addUser(new User(name, contactNumber, R.drawable.ic_user_profile));
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

        // 홈 버튼 클릭 시 홈 액티비티로 이동
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 문 버튼 클릭시 크루룸 액티비티로 이동
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, CrewRoomActivity.class);
            startActivity(intent);
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
