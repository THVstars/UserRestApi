package com.careerdevs.UserRestApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostModel {

    private int id;
    @JsonProperty("user_id")
    private int userId;
    private String title;
    private String body;

    public PostModel() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
