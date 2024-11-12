package com.example.ddip_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CrewroomAdapter extends RecyclerView.Adapter<CrewroomAdapter.CrewRoomViewHolder> {

    private final List<String> crewRoomList;
    private OnItemClickListener onItemClickListener;

    // 생성자: 크루룸 리스트를 초기화
    public CrewroomAdapter(List<String> crewRoomList) {
        this.crewRoomList = crewRoomList;
    }

    // 클릭 리스너 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(String crewRoomName);
    }

    // 클릭 리스너 설정 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CrewRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // crew_room_item 레이아웃을 inflate하여 ViewHolder 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crewroom, parent, false);
        return new CrewRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewRoomViewHolder holder, int position) {
        // 현재 위치의 크루룸 이름을 가져와 ViewHolder에 바인딩
        String crewRoomName = crewRoomList.get(position);
        holder.crewRoomNameTextView.setText(crewRoomName);

        // 아이템 클릭 시 리스너 호출
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(crewRoomName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewRoomList.size();
    }

    // ViewHolder 클래스 정의
    public static class CrewRoomViewHolder extends RecyclerView.ViewHolder {
        TextView crewRoomNameTextView;

        public CrewRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            crewRoomNameTextView = itemView.findViewById(R.id.crew_room_name);
        }
    }
}
