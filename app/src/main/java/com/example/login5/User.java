package com.example.login5;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String email;
    private String password;
    private String profile;
    private String name;
    private int age;
    private String mbti;
    private String myself;

    public User(){}

    // Hashmap
    public User(String uid,String email,String password,String name,int age,String mbti,String myself,String profile){
        this.uid=uid;
        this.email=email;
        this.password=password;
        this.name=name;
        this.age=age;
        this.mbti=mbti;
        this.myself=myself;
        this.profile=profile;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("uid",uid);
        result.put("email",email);
        result.put("password",password);
        result.put("name",name);
        result.put("age",age);
        result.put("MBTI",mbti);
        result.put("myself",myself);
        result.put("profile",profile);

        return result;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

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
}
