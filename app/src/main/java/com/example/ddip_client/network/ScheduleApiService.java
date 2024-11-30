package com.example.ddip_client.network;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScheduleApiService {

    // 스케줄 정보를 서버에 저장하는 API
    @POST("/api/schedules/add")
    Call<Void> saveSchedule(@Body Map<String, Object> scheduleData);

    @GET("/api/schedules/{crewRoomId}/{memberId}")
    Call<List<Map<String, Object>>> getSchedules(
            @Path("crewRoomId") String crewRoomId,
            @Path("memberId") String memberId
    );

}
