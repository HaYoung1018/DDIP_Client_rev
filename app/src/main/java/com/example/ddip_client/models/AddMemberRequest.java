package com.example.ddip_client.models;

public class AddMemberRequest {
    private int crewRoomId;
    private String memberId;
    private String color;
    private String contactNumber;
    private String memberType;

    public AddMemberRequest(int crewRoomId, String memberId, String color, String contactNumber, String memberType) {
        this.crewRoomId = crewRoomId;
        this.memberId = memberId;
        this.color = color;
        this.contactNumber = contactNumber;
        this.memberType = memberType;
    }

    public int getCrewRoomId() {
        return crewRoomId;
    }

    public void setCrewRoomId(int crewRoomId) {
        this.crewRoomId = crewRoomId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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
