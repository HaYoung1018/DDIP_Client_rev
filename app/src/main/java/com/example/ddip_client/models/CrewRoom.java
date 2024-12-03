package com.example.ddip_client.models;
public class CrewRoom {
    private Integer crewRoomId;
    private String crewRoomName;
    private String shopName;
    private String owner;
    private String crewRoomInvitation;
    // Getters and Setters
    public Integer getCrewRoomId() {
        return crewRoomId;
    }
    public void setCrewRoomId(Integer crewRoomId) {
        this.crewRoomId = crewRoomId;
    }
    public String getCrewRoomName() {
        return crewRoomName;
    }
    public void setCrewRoomName(String crewRoomName) {
        this.crewRoomName = crewRoomName;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getCrewRoomInvitation() {
        return crewRoomInvitation;
    }
    public void setCrewRoomInvitation(String crewRoomInvitation) {
        this.crewRoomInvitation = crewRoomInvitation;
    }
}