package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.models.Member;
import com.example.ddip_client.network.LoginSignupService;
import com.example.ddip_client.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText NameInput, IdInput, emailInput, pwdInput, pwdCheck, contactNumberInput;
    private Button signupBtn, checkIdBtn, checkPwdBtn;
    private CheckBox checkManager;
    private boolean isIdValid = false;
    private boolean isPwdValid = false;
    private String usertype = "Staff";
//    private String tempphone = "010-0000-0000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // EditText와 Button을 연결합니다
        NameInput = findViewById(R.id.name_input);
        IdInput = findViewById(R.id.id_input);
        checkIdBtn = findViewById(R.id.id_check_button);
        emailInput = findViewById(R.id.email_input);
        pwdInput = findViewById(R.id.password_input);
        pwdCheck = findViewById(R.id.password_check_input);
        checkPwdBtn = findViewById(R.id.password_check_button);
        checkManager = findViewById(R.id.manager_checkbox);
        signupBtn = findViewById(R.id.signup_button);
        contactNumberInput = findViewById(R.id.contact_number_input);

        //아이디 중복 확인 버튼 클릭 리스너 설정
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = IdInput.getText().toString().trim();
                checkIdAvailability(id);
            }
        });

        // 비밀번호 확인 버튼 클릭 리스너 설정
        checkPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pwdInput.getText().toString().trim();
                String passwordCheck = pwdCheck.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    isPwdValid = false;
                } else if (password.equals(passwordCheck)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                    isPwdValid = true;
                } else {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    isPwdValid = false;
                }
            }
        });

        checkManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkManager.isChecked()){
                    usertype = "Owner";
                }else{
                    usertype = "Staff";
                }
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = NameInput.getText().toString().trim();
                String id = IdInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = pwdInput.getText().toString().trim();
                String passwordCheck = pwdCheck.getText().toString().trim();
                String contactNumber = contactNumberInput.getText().toString().trim();
                String isManager = usertype;


                // 입력값이 모두 비어있지 않은지 확인
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCheck) || TextUtils.isEmpty(contactNumber)) {
                    Toast.makeText(SignupActivity.this, "모든 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호와 비밀번호 확인이 일치하는지 확인
                if (!isPwdValid) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 성공 로직 (실제 서버로의 데이터 전송이 필요)
                isManager = usertype;
                Member data = new Member(id, password, name, email, isManager, contactNumber);
                signupUser(data);
                Intent intent = new Intent(SignupActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //아이디 중복 확인 요청 함수
    private void checkIdAvailability(String userid) {
        LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
        //서버로 아이디 중복 확인 요청 보내기
        Call<Boolean> call = userApi.checkUserid(userid);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("CheckUsername", "Request URL: " + call.request().url());  // 요청 URL 확인
                Log.d("CheckUsername", "Response Code: " + response.code());     // 응답 코드 확인

                if (response.isSuccessful() && response.body() != null) {
                    boolean isTaken = response.body();
                    Log.d("CheckUsername", "Response Body: " + isTaken);
                    Toast.makeText(SignupActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    isIdValid = true;
                } else {
                    Log.d("CheckUsername", "Response Error: " + response.errorBody()); // 에러 응답 출력
                    Toast.makeText(SignupActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                    isIdValid = false;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("registActivity", "Request Failure: " + t.getMessage());  // 실패 메시지 출력
                Toast.makeText(SignupActivity.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isIdValid = false;
            }
        });
    }

    //비밀번호 확인 함수
    private void checkPwdAvaliability(String pwd1, String pwd2){
        if(pwd1.equals(pwd2)){
            Toast.makeText(SignupActivity.this, "비밀번호 확인 완료", Toast.LENGTH_SHORT).show();
            isPwdValid = true;
        }else{
            Toast.makeText(SignupActivity.this, "비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            isPwdValid = false;
        }
    }

    //비밀번호 조건 함수
    public boolean PwdFormat(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = false; //대문자
        boolean hasLowerCase = false; //소문자
        boolean hasDigit = false; //숫자
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) { //Character 클래스의 isUpperCase 메소드를 호출
                hasUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasUpperCase && hasLowerCase && hasDigit;
    }

    //회원가입 요청 함수
    private void signupUser(Member user){
        System.out.println(user.getUser_type());
        LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
        Call<Member> call = userApi.signup(user);
        call.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    if(user.getUser_type().equals("Owner")){
                        Toast.makeText(SignupActivity.this, "회원가입 성공 (Owner)", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignupActivity.this, "회원가입 성공 (Staff)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("registActivity", t.getMessage());
            }
        });
    }
}
