package com.example.login5;

public class User {
    // 사용자 기본정보

    public String uid;
    public String email;
    public String pass;
    public String name;
    public int age;
    public String mbti;
    public String myself;
    public String profile;

    User(){}

    // Hashmap
    public User(String uid,String email,String pass,String name,int age,String mbti,String myself,String profile){
        this.uid=uid;
        this.email=email;
        this.pass=pass;
        this.name=name;
        this.age=age;
        this.mbti=mbti;
        this.myself=myself;
        this.profile=profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getMyself() {
        return myself;
    }

    public void setMyself(String myself) {
        this.myself = myself;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
