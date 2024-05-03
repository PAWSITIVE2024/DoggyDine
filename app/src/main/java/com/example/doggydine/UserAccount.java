package com.example.doggydine;

// 사용자가 로그인할떄 어떻게 담아두고 가져올껀지
//사용자 계정 정보 모델 클래스
public class UserAccount {
    //여기에 나머지 나이 무게 등등 다 설정해줘야함
    private String idToken;
    private String emailId;
    private String password;
    private String username;
    private String profile; //이건 userface

    public UserAccount() {}

    public String getIdToken() { return idToken;}

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() { return emailId;}

    public void setEmailId(String emailId) {this.emailId = emailId;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
