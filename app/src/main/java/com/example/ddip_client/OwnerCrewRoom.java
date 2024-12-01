package com.example.ddip_client;

public class OwnerCrewRoom {
    private final String name;
    private final String inviteCode;

    public OwnerCrewRoom(String name, String inviteCode) {
        this.name = name;
        this.inviteCode = inviteCode;
    }

    public String getName() {
        return name;
    }

    public String getInviteCode() {
        return inviteCode;
    }
}
