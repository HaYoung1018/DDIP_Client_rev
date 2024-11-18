package com.example.ddip_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CrewRoomAdapter extends RecyclerView.Adapter<CrewRoomAdapter.ViewHolder> {

    private Context context;
    private List<String> crewRoomList;
    private OnItemClickListener listener;

    // 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(String roomName);
    }

    // 생성자
    public CrewRoomAdapter(Context context, List<String> crewRoomList, OnItemClickListener listener) {
        this.context = context;
        this.crewRoomList = crewRoomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate하여 ViewHolder 생성
        View view = LayoutInflater.from(context).inflate(R.layout.crew_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 바인딩
        String roomName = crewRoomList.get(position);
        holder.roomNameText.setText(roomName);

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(roomName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewRoomList != null ? crewRoomList.size() : 0;
    }

    // ViewHolder 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // crew_room_item.xml의 TextView ID와 연결
            roomNameText = itemView.findViewById(R.id.room_name_text);
        }
    }
}
