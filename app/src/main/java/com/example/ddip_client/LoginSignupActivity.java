package com.example.ddip_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginSignupActivity extends AppCompatActivity {

    private EditText idInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // EditText와 Button을 연결합니다
        idInput = findViewById(R.id.id_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        // 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // 아이디 입력 여부 확인
                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(LoginSignupActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 입력 여부 확인
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginSignupActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // SharedPreferences에서 저장된 회원 정보 가져오기
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String savedId = sharedPreferences.getString("userId", "");
                String savedPassword = sharedPreferences.getString("userPassword", "");

                // 로그인 정보 검증
                if (id.equals(savedId) && password.equals(savedPassword)) {
                    Toast.makeText(LoginSignupActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    // 메인 액티비티로 이동하기 위한 Intent
                    Intent intent = new Intent(LoginSignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginSignupActivity.this, "아이디 또는 비밀번호가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동하기 위한 Intent
                Intent intent = new Intent(LoginSignupActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
