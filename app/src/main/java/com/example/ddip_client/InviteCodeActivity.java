package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InviteCodeActivity extends AppCompatActivity {

    private EditText inviteCodeInput;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_code_input);

        inviteCodeInput = findViewById(R.id.invite_code_input);
        confirmButton = findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(v -> {
            String inviteCode = inviteCodeInput.getText().toString().trim();
            if (inviteCode.isEmpty()) {
                Toast.makeText(this, "초대 코드를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 초대 코드 유효성을 확인하고 방으로 이동
                Intent intent = new Intent(InviteCodeActivity.this, InvitedRoomActivity.class);
                intent.putExtra("inviteCode", inviteCode);
                startActivity(intent);
            }
        });
    }
}
