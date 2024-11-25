package com.example.ddip_client;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CreateTradeActivity extends AppCompatActivity {

    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trade);

        // 입력 요소 참조
        Button selectDateButton = findViewById(R.id.select_work_date_button);
        Button submitTradeButton = findViewById(R.id.submit_trade_button);
        TimePicker startTimePicker = findViewById(R.id.start_time_picker);
        TimePicker endTimePicker = findViewById(R.id.end_time_picker);
        TextView selectedDateTextView = findViewById(R.id.selected_date_text);

        // 날짜 선택 버튼 클릭 이벤트
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        selectedDateTextView.setText("선택된 날짜: " + selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // 교환 신청 버튼 클릭 이벤트
        submitTradeButton.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "근무 날짜를 선택하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();

            String shiftTime = selectedDate + " " + startHour + ":" + startMinute + " ~ " + endHour + ":" + endMinute;

            // Toast로 확인
            Toast.makeText(this, "교환 신청 완료: " + shiftTime, Toast.LENGTH_SHORT).show();

            // 데이터를 Intent로 전달
            Intent intent = new Intent();
            intent.putExtra("applicantName", "홍길동"); // 예시 이름
            intent.putExtra("shiftTime", shiftTime);
            setResult(RESULT_OK, intent);
            finish(); // 현재 액티비티 종료 후 이전 화면으로 돌아감
        });
    }
}
