package com.example.ddip_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<CalendarItem> calendarItemList;

    // 생성자
    public CalendarAdapter(List<CalendarItem> calendarItemList) {
        this.calendarItemList = calendarItemList;
    }

    // ViewHolder 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText;
        public TextView dayOfWeekText;
        public TextView timeText;
        public TextView salaryText;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            dayOfWeekText = itemView.findViewById(R.id.day_of_week_text);
            timeText = itemView.findViewById(R.id.time_text);
            salaryText = itemView.findViewById(R.id.salary_text);
        }
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_calendar.xml을 inflate하여 ViewHolder 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        // 데이터 바인딩
        CalendarItem item = calendarItemList.get(position);
        holder.dateText.setText(item.getDate());
        holder.dayOfWeekText.setText(item.getDayOfWeek());
        holder.timeText.setText(item.getTime());
        holder.salaryText.setText(item.getSalary());
    }

    @Override
    public int getItemCount() {
        return calendarItemList.size();
    }
}