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

public class OwnerCrewRoomAdapter extends RecyclerView.Adapter<OwnerCrewRoomAdapter.ViewHolder> {

    private final List<OwnerCrewRoom> crewRoomList;
    private final Context context;

    public OwnerCrewRoomAdapter(Context context, List<OwnerCrewRoom> crewRoomList) {
        this.context = context;
        this.crewRoomList = crewRoomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.owner_crew_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
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
            /*
            Intent intent = new Intent(context, OwnerInviteCodeInputActivity.class);
            intent.putExtra("invite_code", crewRoom.getInviteCode()); // 초대코드 전달
            context.startActivity(intent);
            */
        });
    }

    @Override
    public int getItemCount() {
        return crewRoomList.size();
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
