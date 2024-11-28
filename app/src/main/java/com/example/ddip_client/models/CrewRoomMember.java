package com.example.ddip_client.models;

import java.util.Date;

public class CrewRoomMember {
    private Integer crewRoomMemberId;
    private Integer crewRoom;
    private String Member;
    private String color;
    private Date startDate;
    private String contactNumber;
    private String memberType;

    public Integer getcrewRoomMemberId() {
        return crewRoomMemberId;
    }

    public void setcrewRoomMemberId(Integer crewRoomMemberId) {
        this.crewRoomMemberId = crewRoomMemberId;
    }

    public Integer getcrewRoom() {
        return crewRoom;
    }

    public void setcrewRoom(Integer crewRoom) {
        this.crewRoom = crewRoom;
    }

    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    public Date getstartDate() {
        return startDate;
    }

    public void setstartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getcontactNumber() {
        return contactNumber;
    }

    public void setcontactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getmemberType() {
        return memberType;
    }

    public void setmemberType(String memberType) {
        this.memberType = memberType;
    }

    public void getall(){
        System.out.println("크루룸 id " + getcrewRoomMemberId());
        System.out.println("color " + getcolor());
        System.out.println("이름 " + getMember());
        System.out.println("크루룸 이름 " + getcrewRoom());
        System.out.println("사용자 유형" + getmemberType());
        System.out.println("연락처 " + getcontactNumber());
        System.out.println("입사일 " + getstartDate());
    }
}
