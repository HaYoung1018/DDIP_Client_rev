package com.example.ddip_client.network;

import com.example.ddip_client.models.CrewRoom;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CrewRoomApiService {
    //크루룸 목록 불러오기
    @GET("/api/crewroom/list/{memberId}")
    Call<List<Map<String, String>>> getCrewRooms(@Path("memberId") String memberId);

    // 새로운 크루룸 생성
    @POST("/api/crewroom/create")
    Call<CrewRoom> createCrewRoom(@Body CrewRoom crewRoom);
}
