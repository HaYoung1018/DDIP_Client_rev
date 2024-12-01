package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

        // 크루룸장(점주명)은 자동으로 불러오는 설정
        loadLeaderNameFromDB();

        // 생성 버튼 클릭 리스너 설정
        createCrewRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCrewRoom();
            }
        });
    }

    // DB에서 리더 이름을 불러오는 메서드 (샘플 코드)
    private void loadLeaderNameFromDB() {
        // 여기에 데이터베이스에서 정보를 가져오는 로직을 추가해야 합니다.
        // 예시로 "홍길동"을 리더 이름으로 사용합니다.
        String leaderNameFromDB = "홍길동"; // 이 부분은 실제 DB 연결 시 변경되어야 합니다.
        crewRoomLeaderName.setText(leaderNameFromDB);
    }

    // 크루룸을 생성하는 메서드
    private void createCrewRoom() {
        String crewRoomName = crewRoomNameInput.getText().toString().trim();
        String storeName = storeNameInput.getText().toString().trim();
        String leaderName = crewRoomLeaderName.getText().toString().trim();

        // 입력 필드가 비어있을 경우의 유효성 검사
        if (crewRoomName.isEmpty()) {
            Toast.makeText(this, "크루룸명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (storeName.isEmpty()) {
            Toast.makeText(this, "업장명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 크루룸 생성 로직 (예: 서버에 전송)
        // 서버에 데이터 전송 등의 로직을 추가해야 합니다.
        // 현재는 예시로 토스트 메시지를 사용합니다.
        Toast.makeText(this, "크루룸이 생성되었습니다.\n크루룸명: " + crewRoomName + "\n업장명: " + storeName + "\n크루룸장: " + leaderName, Toast.LENGTH_LONG).show();

        // 크루룸 생성이 완료되면 CrewRoomListActivity로 이동
        Intent intent = new Intent(OwnerCreateCrewRoomActivity.this, CrewRoomListActivity.class);
        startActivity(intent);
    }
}
