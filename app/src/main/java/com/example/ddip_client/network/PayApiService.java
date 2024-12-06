package com.example.ddip_client.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PayApiService {
    @GET("/api/pay/crewroom/{crewroomid}/{member}")
    Call<Map<String, Object>> getMemberMonthlyPay(
            @Path("crewroomid") int crewRoomId,
            @Path("member") String memberId
    );

    @GET("/api/pay/all/member/{crewroomid}")
    Call<Map<String, Object>> getAllMembersMonthlyPay(
            @Path("crewroomid") int crewroomid
    );
}
