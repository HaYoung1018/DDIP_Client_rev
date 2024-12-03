package com.example.ddip_client.models;

public class AddMemberRequest {
    private int crewRoom; // 수정된 필드 이름
    private String member; // 수정된 필드 이름
    private String color;
    private String contactNumber;
    private String memberType;

    public AddMemberRequest(int crewRoom, String member, String color, String contactNumber, String memberType) {
        this.crewRoom = crewRoom;
        this.member = member;
        this.color = color;
        this.contactNumber = contactNumber;
        this.memberType = memberType;
    }

    // Getters and Setters
    public int getCrewRoom() {
        return crewRoom;
    }

    public void setCrewRoom(int crewRoom) {
        this.crewRoom = crewRoom;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
}
