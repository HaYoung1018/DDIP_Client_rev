package com.example.ddip_client.network;

import com.example.ddip_client.models.CrewRoomMember;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CrewRoomApiService {
    //크루룸 목록 불러오기
    @GET("/api/crewroom/list/{memberId}")
    Call<List<Map<String, String>>> getCrewRooms(@Path("memberId") String memberId);

    //근무자 목록 불러오기
    @GET("/api/CrewRoomMember/getCrewRoomMembers/{crewRoom}")
    Call<List<Map<String, String>>> getCrewRoomMembers(@Query("crewRoom") String crewRoom);
}
