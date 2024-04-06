package com.example.doggydine;

import java.util.Map;

    public class Food {
        private String profile;
        private String name;
        private String manu;
        private String number;
        private String weight;
        private String score;
        private String price;
        private Map<String, Boolean> material;
        private String kcal;

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

        public String getManu() {
            return manu;
        }

        public void setManu(String manu) {
            this.manu = manu;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Map<String, Boolean> getMaterial() {
            return material;
        }

        public void setMaterial(Map<String, Boolean> material) {
            this.material = material;
        }


        public String getKcal() {return kcal;}

        public void setKcal(String kcal) {this.kcal = kcal;}

        //테스트용 ,로그확인용
        public String toString() {
            return "Food{" +
                    "profile='" + profile + '\'' +
                    ", name='" + name + '\'' +
                    ", manu='" + manu + '\'' +
                    ", number='" + number + '\'' +
                    ", weight='" + weight + '\'' +
                    ", score='" + score + '\'' +
                    ", price='" + price + '\'' +
                    ", material=" + material +
                    '}';
        }

    }


