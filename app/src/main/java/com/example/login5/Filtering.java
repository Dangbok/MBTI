package com.example.login5;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Filtering {

    //profile 추가
    private String profile;
    private String name;
    private String mbti;

    Filtering(){

    }

    public Filtering(String name, String mbti, String profile) {
        this.name = name;
        this.mbti = mbti;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}