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
    private OwnerCrewRoomAdapter crewRoomAdapter; // 어댑터 이름 변경
    private List<OwnerCrewRoom> crewRoomList; // 크루룸 데이터 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crew_room_list);

        // RecyclerView 설정
        crewRoomRecyclerView = findViewById(R.id.crew_room_list);
        crewRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 초기 크루룸 리스트 설정
        crewRoomList = new ArrayList<>();
        crewRoomList.add(new OwnerCrewRoom("크루룸 A", "초대코드: EV99G85S"));
        crewRoomList.add(new OwnerCrewRoom("크루룸 B", "초대코드: AB12CD34"));
        crewRoomList.add(new OwnerCrewRoom("크루룸 C", "초대코드: XY98ZT76"));

        // 어댑터 설정
        crewRoomAdapter = new OwnerCrewRoomAdapter(this, crewRoomList);
        crewRoomRecyclerView.setAdapter(crewRoomAdapter);

        // 근무 등록 버튼 설정
        Button registerWorkButton = findViewById(R.id.register_work_button);
        registerWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 크루룸 추가
                addNewCrewRoom("새로운 크루룸", "초대코드: NEW12345");
            }
        });
    }

    // 새로운 크루룸 추가 함수
    private void addNewCrewRoom(String crewRoomName, String inviteCode) {
        crewRoomList.add(new OwnerCrewRoom(crewRoomName, inviteCode)); // 리스트에 새로운 크루룸 추가
        crewRoomAdapter.notifyDataSetChanged(); // 리스트 갱신
        Toast.makeText(this, crewRoomName + "이(가) 추가되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
