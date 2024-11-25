package com.example.ddip_client.network;

import com.example.ddip_client.models.Member;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginSignupService {
    @POST("/api/Member/signup")
    Call<Member> signup(@Body Member ddip_db);

    @POST("/api/Member/login")
    Call<Member> login(@Query("id") String id, @Query("password") String password);

    @GET("/api/Member/check-username")
    Call<Boolean> checkUserid(@Query("id") String id);

    @GET("/api/Member/check-admin")
    Call<Map<String, String>> checkAdmin(@Query("id") String id);
}
