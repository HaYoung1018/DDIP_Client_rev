package com.example.ddip_client.network;

public class InviteApiServiceProvider {
    private static InviteApiService inviteApiService;

    public static InviteApiService getInviteApiService() {
        if (inviteApiService == null) {
            inviteApiService = RetrofitClient.getClient().create(InviteApiService.class);
        }
        return inviteApiService;
    }
}
