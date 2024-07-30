package com.example.learningenglish.user.usermanager;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class AppUserInfo {

    @SerializedName("id")
    private long id;

    @SerializedName("username")
    private String username = "";

    @SerializedName("role")
    private int role ;

    public AppUserInfo(long id) {
        this.id = id;
    }

    // Getter and Setter methods for all fields

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AppUserInfo{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}