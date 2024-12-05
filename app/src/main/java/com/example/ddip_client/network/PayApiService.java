package com.example.ddip_client.network;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PayApiService
{
    // 시급 정보를 가져오는 메서드
    @GET("/api/salary/hourly-rate")
    Call<Double> getHourlyRate();

    // 특정 월의 주차별 근무 시간을 가져오는 메서드
    @GET("/api/salary/work-hours/{month}")
    Call<Map<Integer, Double>> getWeeklyHoursByMonth(@Path("month") int month);
    // 특정 크루룸, 특정 멤버의 월 급여를 가져오는 메서드
    @GET("/api/salary/monthly-pay")
    Call<Integer> getMonthlyPayForCrewMember(@Query("crewRoom") int crewRoom,
                                             @Query("member") String member,
                                             @Query("month") int month);
}
