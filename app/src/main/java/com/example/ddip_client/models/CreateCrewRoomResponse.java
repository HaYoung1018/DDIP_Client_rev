package com.example.ddip_client.models;

import com.google.gson.annotations.SerializedName;

public class CreateCrewRoomResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
