package com.example.login5;

import com.google.firebase.auth.FirebaseUserMetadata;

/*
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {

    private String idToken; //Firebase Uid(고유 토큰정보)
    private String emailId; //이메일 아이디
    private String password; //비밀번호
    private String name; //이름
    private int age; //나이
    private FirebaseUserMetadata mbti; //mbti 유형
    private String myself; //자기소개
    private String imageUrl;


    public UserAccount(String s, String userID, String userPass, String userName, int userAge, String mbti, String userMbti, String userMyself) {
    }


//    UserAccount(String s, String userID, String userPass, String userName, int userAge, String userMbti, String userMyself) { } //Firebase 사용시 모델 타입 빈 생성자 필요!
//    public UserAccount(String idToken, String emailId, String password, String name, int age, String mbti, String myself,Uri imageUrl){
//        this.idToken=idToken;
//        this.emailId=emailId;
//        this.password=password;
//        this.name=name;
//        this.age=age;
//        this.mbti=mbti;
//        this.myself=myself;
//        this.imageUrl=imageUrl;
//    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return Integer.parseInt(String.valueOf(age));
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FirebaseUserMetadata getMbti() {
        return mbti;
    }

    public void setMbti(FirebaseUserMetadata mbti) {
        this.mbti = mbti;
    }
    public String getMyself() {
        return myself;
    }

    public void setMyself(String myself) {
        this.myself = myself;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
