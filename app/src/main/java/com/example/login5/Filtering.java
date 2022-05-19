package com.example.login5;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Filtering {

    String name, mbti;
    int image;

    Filtering(){

    }

    public Filtering(String name, String mbti, int image) {
        this.name = name;
        this.mbti = mbti;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
