package com.example.ddip_client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OwnerInviteCodeInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_invite_code_input); // XML 레이아웃 연결

        // XML 뷰 연결
        TextView roomNameTextView = findViewById(R.id.room_name_text); // 크루룸 이름
        TextView inviteCodeTextView = findViewById(R.id.owner_invite_code_value); // 초대 코드
        TextView copyInviteCodeText = findViewById(R.id.owner_copy_invite_code_text); // 복사 텍스트
        Button okbtn = findViewById(R.id.ok_btn);

        // Intent에서 데이터 가져오기
        String roomName = getIntent().getStringExtra("roomName"); // 크루룸 이름
        String inviteCode = getIntent().getStringExtra("inviteCode"); // 초대 코드

        // 가져온 데이터를 TextView에 설정
        roomNameTextView.setText(roomName != null ? roomName : "알 수 없음");
        inviteCodeTextView.setText(inviteCode != null ? inviteCode : "초대코드 없음");

        // 복사 텍스트 클릭 이벤트 처리
        copyInviteCodeText.setOnClickListener(v -> {
            // 클립보드에 초대코드 복사
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Invite Code", inviteCode);
                clipboard.setPrimaryClip(clip);
                // 복사 성공 메시지 출력
                Toast.makeText(this, "초대코드가 복사되었습니다!", Toast.LENGTH_SHORT).show();
            } else {
                // 클립보드 기능이 작동하지 않을 경우
                Toast.makeText(this, "복사에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        okbtn.setOnClickListener(view -> {
            Intent intent = new Intent(OwnerInviteCodeInputActivity.this, OwnerCrewRoomListActivity.class);
            startActivity(intent);
        });
    }
}
