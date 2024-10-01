package com.homework.homeworkProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSwagger {

    @JsonProperty("username")
    private String username;
    private String password;
    private String token;
    @JsonProperty("userID")
    private String userID;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return userID;
    }

    public void setId(String id) {
        this.userID = id;
    }
}
