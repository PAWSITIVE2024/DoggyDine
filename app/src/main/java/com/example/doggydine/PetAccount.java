package com.example.doggydine;

import java.security.PrivateKey;
import java.util.Map;

public class PetAccount {
    private Map<String, String> time;
    private String dog_name;
    private String feeding_num;
    private String dog_food;
    private String dog_age;
    private String dog_weight;
    private String active_rate;
    private String allergy;
    private String profile1; //profile1,2,3,4,5 부터는 강아지 사진입니다
    private Map<String, String> profile;
    private String profile2;
    private String profile3;
    private String profile4;
    private String profile5;

    public String getDog_name() {
        return dog_name;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getDog_age() {
        return dog_age;
    }

    public void setDog_age(String dog_age) {
        this.dog_age = dog_age;
    }

    public String getDog_weight() {
        return dog_weight;
    }

    public void setDog_weight(String dog_weight) {
        this.dog_weight = dog_weight;
    }

    public String getActive_rate() {
        return active_rate;
    }

    public void setActive_rate(String active_rate) {
        this.active_rate = active_rate;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
    public String getProfile1() {
        return profile1;
    }

    public void setProfile1(String profile1) {
        this.profile1 = profile1;
    }

    public String getProfile2() {
        return profile2;
    }

    public void setProfile2(String profile2) {
        this.profile2 = profile2;
    }

    public String getProfile3() {
        return profile3;
    }

    public void setProfile3(String profile3) {
        this.profile3 = profile3;
    }

    public String getProfile4() {
        return profile4;
    }

    public void setProfile4(String profile4) {
        this.profile4 = profile4;
    }

    public String getProfile5() {
        return profile5;
    }

    public void setProfile5(String profile5) {
        this.profile5 = profile5;
    }

    public String getDog_food() {
        return dog_food;
    }

    public void setDog_food(String dog_food) {
        this.dog_food = dog_food;
    }

    public Map<String, String> getProfile() {
        return profile;
    }

    public void setProfile(Map<String, String> profile) {
        this.profile = profile;
    }

    public String getFeeding_num() {
        return feeding_num;
    }

    public void setFeeding_num(String feeding_num) {
        this.feeding_num = feeding_num;
    }

    public Map<String, String> getTime() {
        return time;
    }

    public void setTime(Map<String, String> time) {
        this.time = time;
    }
}
