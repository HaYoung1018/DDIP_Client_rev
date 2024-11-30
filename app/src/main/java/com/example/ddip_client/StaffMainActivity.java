package com.example.ddip_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddip_client.network.CrewRoomApiService;
import com.example.ddip_client.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffMainActivity extends AppCompatActivity {

    // Memo 관련 변수
    private EditText memoInput;
    private ImageButton previousDayButton, nextDayButton, moreOptionsButton;
    private TextView dateTextView;
    private Calendar calendar;

    // 방 리스트 관련 변수
    private RecyclerView roomRecyclerView;
    private CrewRoomAdapter roomAdapter;
    private List<String> roomList;

    private static final String ROOM_PREFS = "RoomPrefs"; // 방 데이터 저장소
    private static final String PREFS_NAME = "MemoPrefs"; // 메모 저장소
    private static final String MEMO_PREFIX = "memo_"; // 메모 저장 키 접두사

    private RecyclerView crewRoomRecyclerView;
    private ImsiCrewRoomAdapter crewRoomAdapter; // 기존 Adapter 재사용
    private List<Map<String, String>> crewRoomList = new ArrayList<>(); // 크루룸 리스트 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        // ------------------ Header (상단바) ------------------
        TextView titleTextView = findViewById(R.id.title_text);
        //titleTextView.setText("쿠잉");

        // ------------------ RecyclerView 설정 ------------------
        crewRoomRecyclerView = findViewById(R.id.crew_room_list);
        crewRoomRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("userId", "");
        if (memberId.isEmpty()) {
            Toast.makeText(this, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 어댑터 초기화
        crewRoomAdapter = new ImsiCrewRoomAdapter(this, crewRoomList, (roomId, roomName) -> {
            // 크루룸 클릭 시 새로운 액티비티로 이동
            Intent intent = new Intent(StaffMainActivity.this, ImsiCrewRoomActivity.class);
            intent.putExtra("ROOM_ID", roomId); // roomId 전달
            intent.putExtra("ROOM_NAME", roomName); // roomName 전달
            startActivity(intent);
        });

        crewRoomRecyclerView.setAdapter(crewRoomAdapter);

        // 크루룸 데이터 로드
        loadCrewRooms(memberId);

//        // RecyclerView Adapter 설정
//        roomAdapter = new CrewRoomAdapter(this, roomList, roomName -> {
//            Toast.makeText(this, "선택된 방: " + roomName, Toast.LENGTH_SHORT).show();
//            // 방 클릭 시 추가 작업 구현 가능
//            Intent intent = new Intent(StaffMainActivity.this, CrewRoomActivity.class);
//            intent.putExtra("roomName", roomName); // 방 이름 전달
//            startActivity(intent);
//        });
//        roomRecyclerView.setAdapter(roomAdapter);

        // ------------------ Bottom Navigation (하단바) ------------------
        ImageButton homeButton = findViewById(R.id.home_button);
        ImageButton subCrewButton = findViewById(R.id.sub_crew_button);
        ImageButton alarmButton = findViewById(R.id.alarm_button);
        ImageButton myPageButton = findViewById(R.id.my_page_button);

        // 홈 버튼 클릭
        homeButton.setOnClickListener(v -> Toast.makeText(this, "홈 화면에 있습니다.", Toast.LENGTH_SHORT).show());

        // 서브크루 버튼 클릭
        subCrewButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, ImsiCrewRoomListActivity.class);
            startActivity(intent);
        });

        // 알람 버튼 클릭
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, AlarmActivity.class);
            startActivity(intent);
        });

        // 마이페이지 버튼 클릭
        myPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, MypageActivity.class);
            startActivity(intent);
        });

        // ------------------ Add Work (근무지 추가) ------------------
        Button addWorkButton = findViewById(R.id.add_work_button);
        addWorkButton.setOnClickListener(v -> {
            Toast.makeText(this, "근무지 추가 클릭됨", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StaffMainActivity.this, InviteCodeActivity.class);
            startActivityForResult(intent, 100); // 초대코드 Activity 결과 반환 요청
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

    private void loadCrewRooms(String memberId) {
        // Retrofit API 호출
        CrewRoomApiService crewRoomApiService = RetrofitClient.getClient().create(CrewRoomApiService.class);

        crewRoomApiService.getCrewRooms(memberId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    crewRoomList.clear();
                    crewRoomList.addAll(response.body());
                    crewRoomAdapter.notifyDataSetChanged(); // RecyclerView 갱신
                } else {
                    Toast.makeText(StaffMainActivity.this, "크루룸 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch crew rooms", t);
                Toast.makeText(StaffMainActivity.this, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    // SharedPreferences에서 방 리스트 불러오기
//    private List<String> loadRoomListFromPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences(ROOM_PREFS, Context.MODE_PRIVATE);
//        Map<String, ?> allEntries = sharedPreferences.getAll();
//        List<String> roomList = new ArrayList<>();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            roomList.add(entry.getValue().toString());
//        }
//        return roomList;
//    }
//
//    // SharedPreferences에 방 저장
//    private void saveRoomToPreferences(String roomName) {
//        SharedPreferences sharedPreferences = getSharedPreferences(ROOM_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(roomName, roomName);
//        editor.apply();
//    }

    // 초대코드 Activity 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String newRoom = data.getStringExtra("newRoom");
            if (newRoom != null && !newRoom.isEmpty()) {
                //saveRoomToPreferences(newRoom);
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


