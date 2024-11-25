package com.example.ddip_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkTimeAdapter extends RecyclerView.Adapter<WorkTimeAdapter.WorkTimeViewHolder> {

    private final List<String> workTimes;
    private final OnWorkTimeClickListener clickListener;

    public interface OnWorkTimeClickListener {
        void onWorkTimeClick(String workTime);
    }

    public WorkTimeAdapter(List<String> workTimes, OnWorkTimeClickListener clickListener) {
        this.workTimes = workTimes;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public WorkTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new WorkTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkTimeViewHolder holder, int position) {
        String workTime = workTimes.get(position);
        holder.workTimeText.setText(workTime);
        holder.itemView.setOnClickListener(v -> clickListener.onWorkTimeClick(workTime));
    }

    @Override
    public int getItemCount() {
        return workTimes.size();
    }

    static class WorkTimeViewHolder extends RecyclerView.ViewHolder {
        TextView workTimeText;

        public WorkTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            workTimeText = itemView.findViewById(android.R.id.text1);
        }
    }
}
