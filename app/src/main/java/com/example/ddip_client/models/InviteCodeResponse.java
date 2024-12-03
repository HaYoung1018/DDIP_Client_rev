package com.example.ddip_client.models;

public class InviteCodeResponse {
    private boolean isValid;
    private int crewRoomId;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getCrewRoomId() {
        return crewRoomId;
    }

    public void setCrewRoomId(int crewRoomId) {
        this.crewRoomId = crewRoomId;
    }
}
