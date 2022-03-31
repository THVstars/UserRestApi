package com.careerdevs.UserRestApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentModel {

    private int id;
    @JsonProperty("post_id")
    private int postId;
    private String name;
    private String email;
    private String body;

    // Don't delete, keep your default constructor just in case.
    public CommentModel() {
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }
}
