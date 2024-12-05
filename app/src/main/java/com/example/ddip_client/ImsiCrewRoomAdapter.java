package com.example.ddip_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;
import java.util.Map;

public class ImsiCrewRoomAdapter extends RecyclerView.Adapter<ImsiCrewRoomAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, String>> crewRoomList; //크루룸ID/크루룸명
    private OnItemClickListener listener;

    // 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(String roomId, String roomName);
    }

    // 생성자
    public ImsiCrewRoomAdapter(Context context, List<Map<String, String>> crewRoomList, OnItemClickListener listener) {
        this.context = context;
        this.crewRoomList = crewRoomList;
        this.listener = listener;
    }

    //데이터 전체 길이
    @Override
    public int getItemCount() {
        if(crewRoomList == null){
            return 0;
        }else {
            return crewRoomList.size();
        }
    }

    //ViewHolder객체 만들기
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate하여 ViewHolder 생성
        View view = LayoutInflater.from(context).inflate(R.layout.crew_room_item, parent, false);
        return new ViewHolder(view);
    }

    //ViewHolder에 데이터를 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 가져오기
        Map<String, String> crewRoom = crewRoomList.get(position);
        String crewRoomName = crewRoom.get("crewRoomName");
        String crewRoomId = crewRoom.get("crewRoomId");
        String crewRoomPay = crewRoom.get("monthlyPay");

        holder.crewRoomName.setText(crewRoomName);
        holder.crewRoomPay.setText(crewRoomPay != null ? "이번달 급여  " + crewRoomPay + "원" : "0원");

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(crewRoomId, crewRoomName);
            }
        });
    }



    // ViewHolder 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView crewRoomName, crewRoomPay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // crew_room_item.xml의 TextView ID와 연결
            crewRoomName = itemView.findViewById(R.id.room_name_text);
            crewRoomPay = itemView.findViewById(R.id.room_pay); // ID 연결
        }
    }
}
