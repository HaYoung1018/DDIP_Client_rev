package com.example.ddip_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class AddWorkActivity extends AppCompatActivity {

    private EditText wageInput;
    private Button selectWorkDateButton;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button saveButton;

    private List<Long> selectedDates = new ArrayList<>();  // 다중 날짜를 저장하기 위한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwork);

        wageInput = findViewById(R.id.wage_input);
        selectWorkDateButton = findViewById(R.id.select_work_date_button);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        saveButton = findViewById(R.id.save_button);

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
                String wage = wageInput.getText().toString().trim();
                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();

                if (wage.isEmpty()) {
                    Toast.makeText(AddWorkActivity.this, "시급을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 시간 계산
                int totalHours = endHour - startHour;
                int totalMinutes = endMinute - startMinute;
                if (totalMinutes < 0) {
                    totalHours--;
                    totalMinutes += 60;
                }

                // 임금 계산 로직 추가
                double wageDouble = Double.parseDouble(wage);
                double totalPay = wageDouble * (totalHours + totalMinutes / 60.0);

                // 결과 표시
                StringBuilder selectedDatesStr = new StringBuilder("선택된 날짜:\n");
                for (Long date : selectedDates) {
                    selectedDatesStr.append(android.text.format.DateFormat.format("yyyy-MM-dd", date)).append("\n");
                }

                Toast.makeText(AddWorkActivity.this,
                        selectedDatesStr.toString() + "근무 시간: " + totalHours + "시간 " + totalMinutes + "분\n총 급여: " + totalPay + "원",
                        Toast.LENGTH_LONG).show();

                // 메인 화면으로 돌아가기 위한 Intent
                Intent intent = new Intent(AddWorkActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
