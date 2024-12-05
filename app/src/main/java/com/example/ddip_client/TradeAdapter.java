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

import com.example.ddip_client.network.RetrofitClient;
import com.example.ddip_client.network.ScheduleApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.TradeViewHolder> {
    private final List<Map<String, Object>> tradeItems;
    private final String memberId; // 추가된 필드
    private final String savedName; //

    // Constructor
    public TradeAdapter(List<Map<String, Object>> tradeItems, String memberId, String savedName) {
        this.tradeItems = tradeItems;
        this.memberId = memberId; // memberId 저장
        this.savedName = savedName;
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
        Map<String, Object> tradeItem = tradeItems.get(position);

        double hourlyPay = Double.parseDouble(tradeItem.get("pay").toString());
        double workHours = Double.parseDouble(tradeItem.get("totalHours").toString());
        int totalPay = (int) (hourlyPay * workHours);

        String applicantName = "신청자: " + tradeItem.get("member").toString();
        String workDate = "날짜: " + tradeItem.get("date").toString();
        String workTime = "시간: " + tradeItem.get("startTime").toString() + " ~ " + tradeItem.get("endTime").toString();
        String totalWorkTime = "근무 시간: " + tradeItem.get("totalHours").toString() + "시간";
        String pay = "급여: " + totalPay + "원";

        holder.applicantName.setText(applicantName);
        holder.workDate.setText(workDate);
        holder.startTime.setText(workTime);
        holder.totalWorkTime.setText(totalWorkTime);
        holder.endTime.setText(pay);

        // 현재 로그인 중인 계정의 사용자 이름과 교화 신청자의 이름이 같으면 버튼 숨김
        if(applicantName.equals(savedName)){
            holder.exchangeButton.setVisibility(View.INVISIBLE);
        }

        // 교환 버튼 클릭 이벤트
        holder.exchangeButton.setOnClickListener(v -> {
            int scheduleId = ((Number) tradeItem.get("scheduleId")).intValue();

            // AlertDialog를 통한 확인 절차
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("교환 요청")
                    .setMessage("이 스케줄을 교환하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        // Retrofit 호출
                        Map<String, String> requestData = new HashMap<>();
                        requestData.put("memberId", memberId); // TradeListActivity에서 전달받은 memberId 사용
                        requestData.put("scheduleId", String.valueOf(scheduleId));

                        ScheduleApiService scheduleService = RetrofitClient.getClient().create(ScheduleApiService.class);
                        scheduleService.exchangeSchedule(requestData).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(holder.itemView.getContext(), "교환 요청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(holder.itemView.getContext(), "교환 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(holder.itemView.getContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("아니오", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
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
