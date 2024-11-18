package com.example.ddip_client;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CrewRoomListActivity extends AppCompatActivity {

    private RecyclerView crewRoomRecyclerView;
    private CrewRoomAdapter crewRoomAdapter; // 기존 MemberAdapter -> CrewRoomAdapter로 변경
    private List<String> crewRoomList; // 크루룸 리스트 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crew_room_list);

        // RecyclerView 설정
        crewRoomRecyclerView = findViewById(R.id.crew_room_list);
        crewRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 초기 크루룸 리스트 설정
        crewRoomList = new ArrayList<>();
        crewRoomList.add("크루룸 A");
        crewRoomList.add("크루룸 B");
        crewRoomList.add("크루룸 C");

        // 어댑터 설정
        crewRoomAdapter = new CrewRoomAdapter(this, crewRoomList, roomName -> {
            // 크루룸 클릭 시 처리
            Toast.makeText(this, "선택된 크루룸: " + roomName, Toast.LENGTH_SHORT).show();
        });

        crewRoomRecyclerView.setAdapter(crewRoomAdapter);

        // 근무 등록 버튼 설정
        Button registerWorkButton = findViewById(R.id.register_work_button);
        registerWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 근무 등록 후 크루룸 추가
                addNewCrewRoom("새로운 크루룸");
            }
        });
    }

    // 새로운 크루룸 추가 함수
    private void addNewCrewRoom(String crewRoomName) {
        crewRoomList.add(crewRoomName); // 리스트에 새로운 크루룸 추가
        crewRoomAdapter.notifyDataSetChanged(); // 리스트 갱신
    }
}
