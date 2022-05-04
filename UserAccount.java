package com.example.login5;

/*
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount {

    private String idToken; //Firebase Uid(고유 토큰정보)
    private String emailId; //이메일 아이디
    private String password; //비밀번호
    private String name; //이름
    private String mbti; //mbti 유형


    public UserAccount() { }

//    public UserAccount(String s, String userID, String userPass, String userName, int userAge, String userMbti, String userMyself) {
//    }


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

    public void setIdToken(String idToken) {this.idToken = idToken; }

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

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }


    public UserAccount(String idToken, String emailId, String password, String name, String Mbti){
        this.idToken=idToken;
        this.emailId=emailId;
        this.password=password;
        this.name=name;
        this.mbti=mbti;
    }
}


