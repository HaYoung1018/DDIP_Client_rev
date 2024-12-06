package com.example.ddip_client.models;

import java.time.LocalTime;
import java.util.Date;

public class Schedule {
    private Integer crewRoom;
    private String member;
    private LocalTime startTime;
    private LocalTime endTime;
    private Date date;
    private Double totalHours;
    private Integer pay;

    public Schedule(){}

    public Schedule(Integer crewRoom, String member, LocalTime startTime, LocalTime endTime, Date date, Double totalHours, Integer pay){
        this.crewRoom = crewRoom;
        this.member = member;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.totalHours = totalHours;
        this.pay = pay;
    }

    public Integer getCrewRoom() {
        return crewRoom;
    }

    public void setCrewRoom(Integer crewRoom) {
        this.crewRoom = crewRoom;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }
}
