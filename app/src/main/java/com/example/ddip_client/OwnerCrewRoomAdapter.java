package com.example.ddip_client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class OwnerCrewRoomAdapter extends RecyclerView.Adapter<OwnerCrewRoomAdapter.ViewHolder> {

    private final Context context;
    private final List<Map<String, String>> crewRoomList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String roomId, String roomName);
    }

    public OwnerCrewRoomAdapter(Context context, List<Map<String, String>> crewRoomList, OnItemClickListener listener) {
        this.context = context;
        this.crewRoomList = crewRoomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owner_crew_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> crewRoom = crewRoomList.get(position);
        String crewRoomName = crewRoom.get("crewRoomName");
        String crewRoomId = crewRoom.get("crewRoomId");
        String crewRoomInvitation = crewRoom.get("crewRoomInvitation");

        // 크루룸 이름 설정
        holder.crewRoomName.setText(crewRoomName);
        // 크루룸 초대코드 설정
        holder.crewRoomInvitation.setText("초대코드: " + (crewRoomInvitation != null ? crewRoomInvitation : "N/A"));

        // 초대코드 복사 버튼 동작
        holder.menuMore.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Invite Code", crewRoomInvitation);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "초대코드가 복사되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(crewRoom.get("crewRoomId"), crewRoom.get("crewRoomName"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewRoomList != null ? crewRoomList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView crewRoomName, crewRoomInvitation;
        ImageView menuMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crewRoomName = itemView.findViewById(R.id.crew_room_name);
            crewRoomInvitation = itemView.findViewById(R.id.crew_room_invite_code);
            menuMore = itemView.findViewById(R.id.menu_more);
        }
    }
}
