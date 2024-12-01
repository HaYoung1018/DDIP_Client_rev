package com.example.ddip_client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OwnerInviteCodeInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_invite_code_input); // XML 레이아웃 연결

        // 초대 코드 텍스트 (출력되는 코드)
        TextView inviteCodeTextView = findViewById(R.id.owner_invite_code_value);

        // 초대 코드 복사 텍스트 (클릭 시 복사)
        TextView copyInviteCodeText = findViewById(R.id.owner_copy_invite_code_text);

        // 복사 텍스트 클릭 이벤트 처리
        copyInviteCodeText.setOnClickListener(v -> {
            // 초대 코드 텍스트 가져오기
            String inviteCode = inviteCodeTextView.getText().toString();

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
    }
}
