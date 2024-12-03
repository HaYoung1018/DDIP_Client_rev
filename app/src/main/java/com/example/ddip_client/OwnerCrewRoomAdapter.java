package com.example.ddip_client;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

    //private final List<OwnerCrewRoom> crewRoomList;
    private final Context context;
    private List<Map<String, String>> crewRoomList; //크루룸ID/크루룸명
    private OnItemClickListener listener;

    // 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(String roomId, String roomName);
    }

/*    public OwnerCrewRoomAdapter(Context context, List<OwnerCrewRoom> crewRoomList) {
        this.context = context;
        this.crewRoomList = crewRoomList;
    }*/

    // 생성자
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
        // Get the current room data
        Map<String, String> crewRoom = crewRoomList.get(position);

        // Set the room name and invite code
        holder.crewRoomName.setText(crewRoom.get("roomName"));
        holder.inviteCode.setText("초대코드: " + crewRoom.get("inviteCode"));

        // Set more menu click listener
        holder.menuMore.setOnClickListener(v -> {
            // Copy invite code to clipboard
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Invite Code", crewRoom.get("inviteCode"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "초대코드가 복사되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(crewRoom.get("roomId"), crewRoom.get("roomName"));
            }
        });
    }
/*    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OwnerCrewRoom crewRoom = crewRoomList.get(position);
        // Set data
        holder.crewRoomName.setText(crewRoom.getName());
        holder.inviteCode.setText("초대코드: " + crewRoom.getInviteCode());
        // 점 세 개 클릭 시 동작
        holder.menuMore.setOnClickListener(v -> {
            // 초대코드 복사
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Invite Code", crewRoom.getInviteCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "초대코드가 복사되었습니다!", Toast.LENGTH_SHORT).show();
            }
            // 또는 owner_invite_code_input.xml 화면으로 이동
            *//*
            Intent intent = new Intent(context, OwnerInviteCodeInputActivity.class);
            intent.putExtra("invite_code", crewRoom.getInviteCode()); // 초대코드 전달
            context.startActivity(intent);
            *//*
        });
    }*/
    @Override
    public int getItemCount() {
        if(crewRoomList == null){
            return 0;
        }else {
            return crewRoomList.size();
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView crewRoomName;
        TextView inviteCode;
        ImageView menuMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crewRoomName = itemView.findViewById(R.id.crew_room_name);
            inviteCode = itemView.findViewById(R.id.crew_room_invite_code);
            menuMore = itemView.findViewById(R.id.menu_more);
        }
    }
}