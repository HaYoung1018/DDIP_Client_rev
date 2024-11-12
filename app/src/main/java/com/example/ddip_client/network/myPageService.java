package com.example.ddip_client.network;

import com.example.ddip_client.models.Member;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface myPageService {
    @GET("collectData")
    Call<Map<String, String>> collectData(@Query("id") String id);
}
