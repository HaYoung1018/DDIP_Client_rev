package com.example.ddip_client;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class InvitedRoomActivity extends AppCompatActivity {

    private RecyclerView memberRecyclerView;
    private MemberAdapter memberAdapter;
    private List<String> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invited_room);

        // RecyclerView 설정
        memberRecyclerView = findViewById(R.id.member_list);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 초기 멤버 리스트 설정 (초대받은 멤버들)
        memberList = new ArrayList<>();
        memberList.add("홍길동");
        memberList.add("김철수");
        memberList.add("이영희");

        // 어댑터 설정
        memberAdapter = new MemberAdapter(memberList);
        memberRecyclerView.setAdapter(memberAdapter);

        // 근무 등록 버튼 설정
        Button registerWorkButton = findViewById(R.id.register_work_button);
        registerWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 근무 등록 후 멤버 리스트에 사용자 추가
                addNewMember("나 자신");
            }
        });
    }

    // 새로운 멤버 추가 함수
    private void addNewMember(String memberName) {
        memberList.add(memberName);
        memberAdapter.notifyDataSetChanged(); // 리스트 갱신
    }
}
