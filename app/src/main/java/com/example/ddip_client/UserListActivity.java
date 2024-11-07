package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crewroom_userlist);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<User> userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        // 예시로 사용자 추가
        addUser(new User("사용자 이름 1", "010-1234-5678", R.drawable.ic_user_profile));
        addUser(new User("사용자 이름 2", "010-8765-4321", R.drawable.ic_user_profile));

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
