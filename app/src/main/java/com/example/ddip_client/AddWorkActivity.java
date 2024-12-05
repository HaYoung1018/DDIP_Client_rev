package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class AddWorkActivity extends AppCompatActivity {

    private EditText wageInput;
    private Button selectWorkDateButton;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button saveButton;

    private List<Long> selectedDates = new ArrayList<>();  // 다중 날짜를 저장하기 위한 리스트
    private ScheduleApiService scheduleApiService;
    //임시id
    private String id ="1";
    private String Croomid = "1";
    private long selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwork);
        // RetrofitClient 사용해 ScheduleApiService 초기화
        scheduleApiService = RetrofitClient.getClient().create(ScheduleApiService.class);

        wageInput = findViewById(R.id.wage_input);
        selectWorkDateButton = findViewById(R.id.select_work_date_button);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        saveButton = findViewById(R.id.save_button);
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("userId", "");
        id = memberId;
        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }



        // 전달된 날짜 값을 Intent에서 가져옴
        selectedDate = getIntent().getLongExtra("selectedDate", -1);
        if (selectedDate != -1) {
            // Date 객체로 변환
            Date date = new Date(selectedDate);

            // 날짜 형식 지정
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = dateFormat.format(date);

            // 예: 전달된 날짜를 Toast 메시지로 확인
            Toast.makeText(this, "선택된 날짜: " + formattedDate, Toast.LENGTH_SHORT).show();
        }

        selectWorkDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("근무 날짜를 선택하세요");

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                builder.setCalendarConstraints(constraintsBuilder.build());

                MaterialDatePicker<Long> datePicker = builder.build();
                datePicker.show(getSupportFragmentManager(), datePicker.toString());

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        selectedDates.add(selection);
                        Toast.makeText(AddWorkActivity.this, "선택된 날짜: " + datePicker.getHeaderText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkSchedule();
                if (check){//checkSchedule에서 문제가 없으면 실행
                    saveSchedule();
                }
            }
        });
    }

    private boolean checkSchedule(){
        String wageText = wageInput.getText().toString().trim(); // 입력값 가져오기 및 공백 제거

        // 시급 입력값 검증
        if (wageText.isEmpty()) {
            // 입력값이 비어있는 경우
            Toast.makeText(AddWorkActivity.this, "시급을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                // 입력값을 정수로 변환
                int wage = Integer.parseInt(wageText);
                if (wage < 0) {
                    // 음수 입력에 대한 처리
                    Toast.makeText(AddWorkActivity.this, "양수를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                // 숫자가 아닌 경우 처리
                Toast.makeText(AddWorkActivity.this, "숫자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //날짜 선택 여부 검증
        if (selectedDates.isEmpty()) {
            Toast.makeText(this, "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 시작 시간
        int startHour = startTimePicker.getHour();
        int startMinute = startTimePicker.getMinute();
        // 종료 시간
        int endHour = endTimePicker.getHour();
        int endMinute = endTimePicker.getMinute();

        String comparisonResult;
        if (startHour == endHour && startMinute == endMinute) {
            Toast.makeText(this, "시작 시간과 종료 시간이 같습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (startHour > endHour || (startHour == endHour && startMinute > endMinute)) {
            Toast.makeText(this, "시작 시간이 종료 시간보다 늦습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private void saveSchedule() {
        String wageText = wageInput.getText().toString().trim(); // 입력값 가져오기 및 공백 제거
        int wage = Integer.parseInt(wageText);

        Long selectedDate = selectedDates.get(0); // 첫 번째 선택된 날짜 사용 예시
        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("member", id);
        scheduleData.put("crewRoom", Croomid);
        // 시간과 날짜 형식으로 데이터를 설정
        String startTime = String.format("%02d:%02d:00", startTimePicker.getHour(), startTimePicker.getMinute());
        String endTime = String.format("%02d:%02d:00", endTimePicker.getHour(), endTimePicker.getMinute());
        scheduleData.put("startTime", startTime);
        scheduleData.put("endTime", endTime);
        scheduleData.put("date", selectedDates.get(0));
        scheduleData.put("pay", wage);
        scheduleData.put("status", "ACTIVE");

        Call<Void> call = scheduleApiService.saveSchedule(scheduleData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddWorkActivity.this, "Schedule saved successfully.", Toast.LENGTH_SHORT).show();
                    passDataToCalendarActivity();
                } else {
                    Toast.makeText(AddWorkActivity.this, "Failed to save schedule.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddWorkActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void passDataToCalendarActivity() {
        String wageText = wageInput.getText().toString().trim();
        int startHour = startTimePicker.getHour();
        int startMinute = startTimePicker.getMinute();
        int endHour = endTimePicker.getHour();
        int endMinute = endTimePicker.getMinute();

        String startTime = String.format("%02d:%02d", startHour, startMinute);
        String endTime = String.format("%02d:%02d", endHour, endMinute);
        String workTime = startTime + " ~ " + endTime;

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedDate", selectedDates.get(0));
        resultIntent.putExtra("workTime", workTime);
        resultIntent.putExtra("wage", wageText);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

