package com.example.ddip_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.models.*;
import com.example.ddip_client.network.LoginSignupService;
import com.example.ddip_client.network.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        // SharedPreferences에서 저장된 회원 정보 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedId = sharedPreferences.getString("userId", "");
        String savedPassword = sharedPreferences.getString("userPassword", "");

        if (!savedId.isEmpty() && !savedPassword.isEmpty()){

            // 로그인 기능 호출
            LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
            Call<Member> login = userApi.login(savedId, savedPassword);
            login.enqueue(new Callback<Member>() {
                @Override
                public void onResponse(Call<Member> call, Response<Member> response) {
                    if(response.isSuccessful()){
                        // 계정 유형에 따른 분기점 설정
                        LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
                        Call<Map<String, String>> userType = userApi.checkAdmin(savedId);

                        userType.enqueue(new Callback<Map<String,String>>() {
                            @Override
                            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                String result = response.body().get("result");
                                if(response.isSuccessful() && response.body() != null){
                                    if(result.equals("Owner")){
                                        Toast.makeText(LoginSignupActivity.this, "Owner계정", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginSignupActivity.this, StaffMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginSignupActivity.this, "Staff계정", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginSignupActivity.this, OwnerMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }else{
                                    return;
                                }
                            }
                            @Override
                            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                System.out.println(t);
                                Toast.makeText(LoginSignupActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else{
                        Toast.makeText(LoginSignupActivity.this, "기존 정보로 로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Member> call, Throwable t) {
                    Toast.makeText(LoginSignupActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", t.getMessage());
                }
            });
        }
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

                    // 로그인 기능 호출
                    LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
                    Call<Member> login = userApi.login(id, password);

                    login.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if(response.isSuccessful()){
                                // 계정 유형에 따른 분기점 설정
                                LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
                                Call<Map<String, String>> userType = userApi.checkAdmin(id);

                                userType.enqueue(new Callback<Map<String,String>>() {
                                    @Override
                                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                        String result = response.body().get("result");
                                        if(response.isSuccessful() && response.body() != null){

                                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                                            autoLogin.putString("userId", id);
                                            autoLogin.putString("userPassword", password);
                                            autoLogin.apply();

                                            if(result.equals("Owner")){
                                                Toast.makeText(LoginSignupActivity.this, "Owner계정", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginSignupActivity.this, StaffMainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(LoginSignupActivity.this, "Staff계정", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginSignupActivity.this, OwnerMainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }else{
                                            return;
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                        System.out.println(t);
                                        Toast.makeText(LoginSignupActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }else{
                                Toast.makeText(LoginSignupActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            Toast.makeText(LoginSignupActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity", t.getMessage());
                        }
                    });
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



    private void loginUser(String ID, String PW){
        LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
        Call<Member> call = userApi.login(ID, PW);

        call.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginSignupActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginSignupActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                Toast.makeText(LoginSignupActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", t.getMessage());
            }
        });
    }

    private void isAdmin(String ID){
        LoginSignupService userApi = RetrofitClient.getClient().create(LoginSignupService.class);
        Call<Map<String, String>> call = userApi.checkAdmin(ID);


        call.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                System.out.println(response.body().get("result"));
                String result;
                if(response.isSuccessful() && response.body() != null){
                    result = response.body().get("result").toString();
                }else{
                    return;
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                System.out.println(t);
                Toast.makeText(LoginSignupActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}