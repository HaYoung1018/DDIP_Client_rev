package com.example.ddip_client;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ddip_client.models.Member;
import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.myPageService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberDataEditActivity extends AppCompatActivity {
    private boolean checkPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data_edit);

        EditText nameInput = findViewById(R.id.name_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText contactNumberInput = findViewById(R.id.contact_number_input);
        EditText passwordInput = findViewById(R.id.password_input);
        EditText passwordCheckInput = findViewById(R.id.password_check_input);
        Button passworCheckButton = findViewById(R.id.password_check_button);
        Button editButton = findViewById(R.id.edit_button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String savedId = sharedPreferences.getString("userId", "");

        // 로그인된 계정의 정보를 받아와 화면에 표시
        myPageService mypageservice = RetrofitClient.getClient().create(myPageService.class);
        Call<Map<String, String>> collectData = mypageservice.collectData(savedId);

        collectData.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().get("name");
                    String id = response.body().get("id");
                    String email = response.body().get("email");
                    String password = response.body().get("password");
                    String contactNumber = response.body().get("contact_number");
                    String userType = response.body().get("user_type");
                    Member user = new Member(id, password, name, email, userType, contactNumber);

                    nameInput.setText(user.getName());
                    emailInput.setText(user.getEmail());
                    contactNumberInput.setText(user.getContact_number());
                    passwordInput.setText(user.getPassword());

                } else {
                    Toast.makeText(MemberDataEditActivity.this, "회원 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(MemberDataEditActivity.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        passworCheckButton.setOnClickListener(v -> {
            String pass1 = passwordInput.getText().toString().trim();
            String pass2 = passwordCheckInput.getText().toString().trim();

            if (pass1.isEmpty() && pass2.isEmpty()){
                Toast.makeText(MemberDataEditActivity.this, "비밀번호를 인증해주세요.", Toast.LENGTH_SHORT).show();
                checkPassword = false;
                return;
            } else {
                if (pass1.equals(pass2)){
                    Toast.makeText(MemberDataEditActivity.this, "인증 완료", Toast.LENGTH_SHORT).show();
                    passworCheckButton.setText("완료");
                    checkPassword = true;
                } else {
                    Toast.makeText(MemberDataEditActivity.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    checkPassword = false;
                    return;
                }
            }
        });

        editButton.setOnClickListener(v -> {
            if (checkPassword){
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String contactNumber = contactNumberInput.getText().toString();
                String password = passwordInput.getText().toString().trim();

                Member member = new Member();
                member.setId(savedId);
                member.setName(name);
                member.setPassword(password);
                member.setEmail(email);
                member.setContact_number(contactNumber);


                // DB에 저장하는 로직 작성부
                myPageService edit = RetrofitClient.getClient().create(myPageService.class);
                Call<Map<String, String >> call = edit.updateMember(savedId, member);

                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MemberDataEditActivity.this, "회원 정보가 수정되었습니다.\n다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                            editor.remove("userId");
                            editor.remove("userPassword");
                            editor.commit();
                            restartApplication(MemberDataEditActivity.this);
                        } else {
                            String errorMessage = response.errorBody() != null ? "회원 정보 수정 실패: " + response.message().toString() : "회원 정보 수정 성공";
                            Toast.makeText(MemberDataEditActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(MemberDataEditActivity.this, "에러 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(MemberDataEditActivity.this, "비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restartApplication(Context mContext){
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(mContext.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        mContext.startActivity(mainIntent);
        System.exit(0);
    }
}
