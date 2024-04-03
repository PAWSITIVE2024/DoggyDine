package com.example.doggydine;

// 사용자가 로그인할떄 어떻게 담아두고 가져올껀지
//사용자 계정 정보 모델 클래스
public class UserAccount {
    //여기에 나머지 나이 무게 등등 다 설정해줘야함
    private String idToken;
    private String emailId;
    private String password;
    private String dog_name;
    private String dog_age;
    private String dog_weight;
    private String active_rate;
    private String allergy;
    private String profile;

    public UserAccount() {}

    public String getIdToken() { return idToken;}

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() { return emailId;}

    public void setEmailId(String emailId) {this.emailId = emailId;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getDog_name() {return dog_name;}

    public void setDog_name(String dog_name) {this.dog_name = dog_name;}

    public String getDog_age() {return dog_age;}

    public void setDog_age(String dog_age) {this.dog_age = dog_age;}

    public String getDog_weight() {return dog_weight;}

    public void setDog_weight(String dong_weight) {this.dog_weight = dong_weight;}

    public String getActive_rate() {return active_rate;}

    public void setActive_rate(String active_rate) {this.active_rate = active_rate;}

    public String getAllergy() {return allergy;}

    public void setAllergy(String allergy) {this.allergy = allergy;}

    public String getProfile() {return profile;}

    public void setProfile(String profile) {this.profile = profile;}
}
