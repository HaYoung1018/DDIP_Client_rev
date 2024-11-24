package com.example.ddip_client;

public class TradeItem {
    private final String applicantName;
    private final String shiftTime;

    public TradeItem(String applicantName, String shiftTime) {
        this.applicantName = applicantName;
        this.shiftTime = shiftTime;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getShiftTime() {
        return shiftTime;
    }
}
