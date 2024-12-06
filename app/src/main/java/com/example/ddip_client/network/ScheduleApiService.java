package com.example.ddip_client.network;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScheduleApiService {

    // 스케줄 정보를 서버에 저장하는 API
    @POST("/api/schedules/add")
    Call<Void> saveSchedule(@Body Map<String, Object> scheduleData);

    //활성 스케줄 조회
    @GET("/api/schedules/{crewRoomId}/{memberId}")
    Call<List<Map<String, Object>>> getSchedules(
            @Path("crewRoomId") String crewRoomId,
            @Path("memberId") String memberId
    );
    // 스케줄 상태 업데이트 API
    @PATCH("/api/schedules/exchange/{scheduleId}")
    Call<Void> updateScheduleStatus(@Path("scheduleId") int scheduleId);
    @GET("/api/schedules/exchangeable/{crewRoomId}")
    Call<List<Map<String, Object>>> getExchangeableSchedules(@Path("crewRoomId") String crewRoomId);

    @PATCH("/api/schedules/DDIP")
    Call<Void> exchangeSchedule(@Body Map<String, String> requestData);

    //sharedpreference에 저장된 로그인 된 아이디를 통해 crewRoomSchedule 테이블에서 데이터 가져오기
    @GET("/api/schedules/getMySchedule/{member}")
    Call<List<Map<String, String>>> getMySchedule(String member);
}
