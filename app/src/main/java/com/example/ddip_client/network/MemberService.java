package com.example.ddip_client.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MemberService {
    @DELETE("/api/Member/withdraw/{id}")
    Call<Map<String, String>> withdrawMember(@Path("id") String id);

    @GET("/api/Member/findMemberById/{id}")
    Call<Map<String, String>> findMemberById(@Path("id") String id);

    @GET("/api/Member/findMemberDetails/{id}")
    Call<Map<String, String>> findMemberDetails(@Path("id") String id);
}
