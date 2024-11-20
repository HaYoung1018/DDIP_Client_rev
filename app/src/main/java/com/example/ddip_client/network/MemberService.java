package com.example.ddip_client.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface MemberService {
    @DELETE("Member/withdraw/{id}")
    Call<Map<String, String>> withdrawMember(@Path("id") String id);
}
