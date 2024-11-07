package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText idInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordCheckInput;
    private Button idCheckButton;
    private Button passwordCheckButton;
    private Button signupButton;
    private CheckBox managerCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // EditText와 Button을 연결합니다
        nameInput = findViewById(R.id.name_input);
        idInput = findViewById(R.id.id_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordCheckInput = findViewById(R.id.password_check_input);
        idCheckButton = findViewById(R.id.id_check_button);
        passwordCheckButton = findViewById(R.id.password_check_button);
        signupButton = findViewById(R.id.signup_button);
        managerCheckbox = findViewById(R.id.manager_checkbox);

        // 비밀번호 확인 버튼 클릭 리스너 설정
        passwordCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString().trim();
                String passwordCheck = passwordCheckInput.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (password.equals(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String id = idInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String passwordCheck = passwordCheckInput.getText().toString().trim();
                boolean isManager = managerCheckbox.isChecked();

                // 입력값이 모두 비어있지 않은지 확인
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "모든 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호와 비밀번호 확인이 일치하는지 확인
                if (!password.equals(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원 정보를 SharedPreferences에 저장
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", id);
                editor.putString("userPassword", password);
                editor.apply();

                // 회원가입 성공 로직 (실제 서버로의 데이터 전송이 필요)
                String role = isManager ? "매장 관리자" : "일반 사용자";
                Toast.makeText(SignupActivity.this, "회원가입 성공!\n역할: " + role, Toast.LENGTH_SHORT).show();

                // 회원가입 후 로그인 화면으로 이동
                Intent intent = new Intent(SignupActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
