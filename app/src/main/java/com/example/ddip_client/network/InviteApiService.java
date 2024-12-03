package com.example.ddip_client.network;

import com.example.ddip_client.models.AddMemberRequest;
import com.example.ddip_client.models.InviteCodeRequest;
import com.example.ddip_client.models.InviteCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InviteApiService {

    // 초대 코드 유효성 검사
    @POST("/api/crewroom/checkInviteCode")
    Call<InviteCodeResponse> checkInviteCode(@Body InviteCodeRequest request);

    // 멤버 추가 요청
    @POST("/api/CrewRoomMember/addMemberToCrew")
    Call<Void> addMemberToCrew(@Body AddMemberRequest request);
}
