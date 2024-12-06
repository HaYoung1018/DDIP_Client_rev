package com.example.ddip_client.network;

import com.example.ddip_client.models.AddMemberRequest;
import com.example.ddip_client.models.InviteCodeRequest;
import com.example.ddip_client.models.InviteCodeResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InviteApiService {

    // 초대 코드 유효성 검사
    @POST("/api/crewroom/checkInviteCode")
    Call<InviteCodeResponse> checkInviteCode(@Body InviteCodeRequest request);

    // 멤버 추가 요청
    @POST("/api/CrewRoomMember/addMemberToCrew")
    Call<Void> addMemberToCrew(@Body AddMemberRequest request);

    // 사용자 정보 가져오기 (전화번호 포함)
    @GET("/api/users/{userId}/contact")
    Call<Map<String, String>> getUserContact(@Path("userId") String userId);
}
