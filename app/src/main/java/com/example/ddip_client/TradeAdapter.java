package com.example.ddip_client;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.TradeViewHolder> {

    private final List<TradeItem> tradeItems;

    // Constructor
    public TradeAdapter(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    @NonNull
    @Override
    public TradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trade, parent, false);
        return new TradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeViewHolder holder, int position) {
        TradeItem item = tradeItems.get(position);

        // 데이터 바인딩
        holder.applicantName.setText("신청자: " + item.getApplicantName());
        holder.workDate.setText("날짜: " + item.getWorkDate());
        holder.startTime.setText("시작 시간: " + item.getStartTime());
        holder.endTime.setText("종료 시간: " + item.getEndTime());
        holder.totalWorkTime.setText("총 근무 시간: " + item.getTotalWorkTime());

        // 교환 버튼 클릭 이벤트
        holder.exchangeButton.setOnClickListener(v -> {
            // AlertDialog 생성
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("교환 요청")
                    .setMessage("교환하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        // 교환 로직 실행
                        Toast.makeText(holder.itemView.getContext(),
                                item.getApplicantName() + "의 근무와 교환이 완료되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        // TODO: 교환 완료 시 필요한 추가 작업은 여기에 작성
                    })
                    .setNegativeButton("아니오", (dialog, which) -> {
                        // 아무 작업도 하지 않음
                        Toast.makeText(holder.itemView.getContext(),
                                "교환이 취소되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        // 점 세 개 클릭 이벤트
        holder.overflowMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.overflowMenu);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.item_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.action_delete) {
                    tradeItems.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(holder.itemView.getContext(), "항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
        return tradeItems.size();
    }

    // ViewHolder 내부 클래스
    public static class TradeViewHolder extends RecyclerView.ViewHolder {
        TextView applicantName, workDate, startTime, endTime, totalWorkTime;
        Button exchangeButton;
        ImageView overflowMenu; // 점 세 개 추가

        public TradeViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicant_name);
            workDate = itemView.findViewById(R.id.work_date);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            totalWorkTime = itemView.findViewById(R.id.total_work_time);
            exchangeButton = itemView.findViewById(R.id.exchange_button);
            overflowMenu = itemView.findViewById(R.id.overflow_menu); // 점 세 개 참조
        }
    }

}
