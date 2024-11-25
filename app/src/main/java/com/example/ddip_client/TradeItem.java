package com.example.ddip_client;

public class TradeItem {
    private final String applicantName;
    private final String workDate;
    private final String startTime;
    private final String endTime;
    private final String totalWorkTime;

    // 생성자
    public TradeItem(String applicantName, String workDate, String startTime, String endTime, String totalWorkTime) {
        this.applicantName = applicantName;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalWorkTime = totalWorkTime;
    }

    // Getter 메서드
    public String getApplicantName() {
        return applicantName;
    }

    public String getWorkDate() {
        return workDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTotalWorkTime() {
        return totalWorkTime;
    }
}
