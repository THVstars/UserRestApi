package com.careerdevs.UserRestApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDosModel {
    private int id;
    @JsonProperty("user_id")
    private int userId;
    private String title;
    @JsonProperty("due_on")
    private String dueOn;
    private String status;

    public ToDosModel() {
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

    public String getDueOn() {
        return dueOn;
    }

    public String getStatus() {
        return status;
    }
}
