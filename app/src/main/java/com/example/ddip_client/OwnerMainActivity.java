package com.example.ddip_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;

public class OwnerMainActivity extends AppCompatActivity {

    // Memo 관련 변수
    private EditText memoInput;
    private ImageButton previousDayButton, nextDayButton, moreOptionsButton;
    private TextView dateTextView;
    private Calendar calendar;

    // RecyclerView 관련 변수
    private RecyclerView roomRecyclerView;
    private CrewRoomAdapter roomAdapter;
    private List<String> roomList;

    private static final String ROOM_PREFS = "RoomPrefs"; // 방 데이터 저장소
    private static final String PREFS_NAME = "MemoPrefs"; // 메모 저장소
    private static final String MEMO_PREFIX = "memo_"; // 메모 저장 키 접두사

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        // ------------------ Header (상단바) ------------------
        TextView titleTextView = findViewById(R.id.title_text);
        titleTextView.setText("쿠잉");

        // ------------------ RecyclerView 설정 ------------------
        roomRecyclerView = findViewById(R.id.crew_room_list);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // SharedPreferences에서 방 리스트 불러오기
        roomList = loadRoomListFromPreferences();

        // RecyclerView Adapter 설정
        roomAdapter = new CrewRoomAdapter(this, roomList, roomName -> {
            Toast.makeText(this, "선택된 방: " + roomName, Toast.LENGTH_SHORT).show();
            // 방 클릭 시 추가 작업 구현 가능
            Intent intent = new Intent(OwnerMainActivity.this, CrewRoomActivity.class);
            intent.putExtra("roomName", roomName); // 방 이름 전달
            startActivity(intent);
        });
        roomRecyclerView.setAdapter(roomAdapter);

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        // 홈 버튼 클릭
        homeButton.setOnClickListener(v -> Toast.makeText(this, "홈 화면에 있습니다.", Toast.LENGTH_SHORT).show());

        // 서브크루 버튼 클릭
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerMainActivity.this, CrewRoomListActivity.class);
            startActivity(intent);
        });

        // 알람 버튼 클릭
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerMainActivity.this, AlarmActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerMainActivity.this, MypageActivity.class);
            startActivity(intent);
        });

        // ------------------ Add Work (근무지 추가) ------------------
        Button addWorkButton = findViewById(R.id.add_work_button);
        addWorkButton.setOnClickListener(v -> {
            Toast.makeText(this, "근무지 추가 클릭됨", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(OwnerMainActivity.this, InviteCodeActivity.class);

            startActivityForResult(intent, 100); // 초대코드 Activity 결과 반환 요청

            startActivity(intent);
        });

        // ------------------ Memo (메모) ------------------
        memoInput = findViewById(R.id.memo_input);
        previousDayButton = findViewById(R.id.previous_day_button);
        nextDayButton = findViewById(R.id.next_day_button);
        moreOptionsButton = findViewById(R.id.more_options_button);
        dateTextView = findViewById(R.id.date_text);

        calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        dateTextView.setText(dateFormat.format(calendar.getTime()));

        loadMemo(dateFormat);

        previousDayButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDate(dateFormat);
        });

        nextDayButton.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDate(dateFormat);
        });

        moreOptionsButton.setOnClickListener(v -> showOptionsMenu());
    }

    // SharedPreferences에서 방 리스트 불러오기
    private List<String> loadRoomListFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(ROOM_PREFS, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<String> roomList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            roomList.add(entry.getValue().toString());
        }
        return roomList;
    }

    // SharedPreferences에 방 저장
    private void saveRoomToPreferences(String roomName) {
        SharedPreferences sharedPreferences = getSharedPreferences(ROOM_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(roomName, roomName);
        editor.apply();
    }

    // 초대코드 Activity 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String newRoom = data.getStringExtra("newRoom");
            if (newRoom != null && !newRoom.isEmpty()) {
                saveRoomToPreferences(newRoom);
                roomList.add(newRoom);
                roomAdapter.notifyDataSetChanged(); // RecyclerView 업데이트
            }
        }
    }

    private void updateDate(SimpleDateFormat dateFormat) {
        String formattedDate = dateFormat.format(calendar.getTime());
        dateTextView.setText(formattedDate);
        loadMemo(dateFormat);
    }

    private void showOptionsMenu() {
        PopupMenu popupMenu = new PopupMenu(this, moreOptionsButton);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.memo_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete_memo) {
                deleteMemo(new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    private void loadMemo(SimpleDateFormat dateFormat) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentDate = dateFormat.format(calendar.getTime());
        String memoText = sharedPreferences.getString(MEMO_PREFIX + currentDate, "");
        memoInput.setText(memoText);
    }

    private void deleteMemo(SimpleDateFormat dateFormat) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentDate = dateFormat.format(calendar.getTime());
        editor.remove(MEMO_PREFIX + currentDate);
        editor.apply();
        memoInput.setText("");
        Toast.makeText(this, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show();
    }
}

