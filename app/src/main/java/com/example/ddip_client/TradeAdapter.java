package com.example.ddip_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
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
        holder.applicantName.setText("신청자: " + item.getApplicantName());
        holder.shiftTime.setText("근무 시간: " + item.getShiftTime());

        // 버튼 클릭 이벤트
        holder.exchangeButton.setOnClickListener(v -> {
            // 버튼 클릭 시 처리할 동작
            // Toast 메시지로 테스트
            Toast.makeText(holder.itemView.getContext(),
                    item.getApplicantName() + "와(과) 교환 완료",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return tradeItems.size();
    }

    // ViewHolder 내부 클래스
    public static class TradeViewHolder extends RecyclerView.ViewHolder {
        TextView applicantName, shiftTime;
        Button exchangeButton;

        public TradeViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicant_name);
            shiftTime = itemView.findViewById(R.id.shift_time);
            exchangeButton = itemView.findViewById(R.id.exchange_button);
        }
    }
}
