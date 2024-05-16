package com.example.doggydine;

import java.util.Map;

    public class Food {
        private String profile;
        private String sales_Volume;
        private String name;
        private String manu;
        private String number;
        private String weight;
        private String score;
        private String price;
        private Map<String, Boolean> material;
        private String kcal;
        private Map<String, String> nutrient;
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

        public String getSales_Volume() {
            return sales_Volume;
        }

        public void setSales_Volume(String sales_Volume) {
            this.sales_Volume = sales_Volume;
        }

        public String getKcal() {return kcal;}

        public void setKcal(String kcal) {this.kcal = kcal;}

        public Map<String, String> getNutrient() {
            return nutrient;
        }

        public void setNutrient(Map<String, String> nutrient) {
            this.nutrient = nutrient;
        }
    }


