package com.example.ddip_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkActivity extends AppCompatActivity {

    private EditText wageInput;
    private Button selectWorkDateButton, saveButton;
    private TimePicker startTimePicker, endTimePicker;
    private List<Long> selectedDates = new ArrayList<>();  // 선택된 날짜 리스트
    private ScheduleApiService scheduleApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwork);
        // XML 요소와 연결
        wageInput = findViewById(R.id.wage_input);
        selectWorkDateButton = findViewById(R.id.select_work_date_button);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        saveButton = findViewById(R.id.save_button);
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        // RetrofitClient 사용해 ScheduleApiService 초기화
        scheduleApiService = RetrofitClient.getClient().create(ScheduleApiService.class);

        // 날짜 선택 버튼 클릭 리스너
        selectWorkDateButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("근무 날짜를 선택하세요");

            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            builder.setCalendarConstraints(constraintsBuilder.build());

            MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.show(getSupportFragmentManager(), datePicker.toString());

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDates.add(selection);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = sdf.format(selection);
                Toast.makeText(this, "선택된 날짜: " + formattedDate, Toast.LENGTH_SHORT).show();
            });
        });

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveSchedule(); // 서버 저장
                passDataToCalendarActivity(); // RecyclerView에 데이터 추가
            }
        });
    }

    private boolean validateInputs() {
        // 시급 입력 검증
        String wageText = wageInput.getText().toString().trim();
        if (wageText.isEmpty()) {
            Toast.makeText(this, "시급을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 날짜 선택 여부 검증
        if (selectedDates.isEmpty()) {
            Toast.makeText(this, "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 시간 검증
        int startHour = startTimePicker.getHour();
        int startMinute = startTimePicker.getMinute();
        int endHour = endTimePicker.getHour();
        int endMinute = endTimePicker.getMinute();

        if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
            Toast.makeText(this, "종료 시간은 시작 시간보다 늦어야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveSchedule() {
        String wageText = wageInput.getText().toString().trim();
        int wage = Integer.parseInt(wageText);

        String startTime = String.format("%02d:%02d:00", startTimePicker.getHour(), startTimePicker.getMinute());
        String endTime = String.format("%02d:%02d:00", endTimePicker.getHour(), endTimePicker.getMinute());

        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("member", "1"); // 임시 memberId
        scheduleData.put("crewRoom", 1); // 임시 크루룸 ID
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
                    Toast.makeText(AddWorkActivity.this, "근무 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddWorkActivity.this, "근무 저장 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddWorkActivity.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
