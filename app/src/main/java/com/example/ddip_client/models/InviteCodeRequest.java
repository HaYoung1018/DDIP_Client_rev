package com.example.ddip_client.models;

public class InviteCodeRequest {
    private String inviteCode;

    public InviteCodeRequest(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
