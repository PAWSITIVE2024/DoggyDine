package com.example.doggydine;

public class PetCalorieCalculator {

    // 기초대사량 계산 메서드
    public static double calculateBasalMetabolicRate(double weight) {
        double basalMetabolicRate;
        if (weight < 2) {
            basalMetabolicRate = 70 * (weight * 0.75);
        } else if (weight >= 2 && weight <= 45) {
            basalMetabolicRate = 30 * weight + 70;
        } else {
            throw new IllegalArgumentException("체중은 2kg에서 45kg 사이여야 합니다.");
        }
        return basalMetabolicRate;
    }

    // 하루 권장 칼로리 계산 메서드
    public static double calculateRecommendedCalories(double basalMetabolicRate, double activityFactor) {
        return basalMetabolicRate * activityFactor;
    }

    // 하루 사료 급여량 계산 메서드
    public static int calculateDailyFoodAmount(double requiredCalories, double foodCaloriesPerKg) {
        return (int) Math.round((requiredCalories * 1000) / foodCaloriesPerKg);
    }

    public static void main(String[] args) {
        // 몸무게와 나이를 받아오기
        double weight = WeightCalculator.getWeight(); // 체중 (kg)
        int age = AgeCalculator.getAge(); // 나이 (years)
        double activityFactor = ActivityFactorCalculator.getActivityFactor(age); // 활동 수치
        double foodCaloriesPerKg = 400; // 사료 칼로리 (kcal/kg)

        // 기초대사량 계산
        double basalMetabolicRate = calculateBasalMetabolicRate(weight);
        System.out.println("기초대사량: " + basalMetabolicRate + " kcal");

        // 하루 권장 칼로리 계산
        double recommendedCalories = calculateRecommendedCalories(basalMetabolicRate, activityFactor);
        System.out.println("하루 권장 칼로리: " + recommendedCalories + " kcal");

        // 하루 사료 급여량 계산
        int dailyFoodAmount = calculateDailyFoodAmount(recommendedCalories, foodCaloriesPerKg);
        System.out.println("하루 사료 급여량: " + dailyFoodAmount + " g");
    }
}

// 몸무게를 받아오는 클래스 예시
class WeightCalculator {
    public static double getWeight() {
        // 예시로 고정된 값인 10kg를 반환하도록 설정
        return 10;
    }
}

// 나이를 받아오는 클래스 예시
class AgeCalculator {
    public static int getAge() {
        // 예시로 고정된 값인 5세를 반환하도록 설정
        return 5;
    }
}

// 활동 수치를 받아오는 클래스 예시
class ActivityFactorCalculator {
    public static double getActivityFactor(int age) {
        // 예시로 고정된 값인 1.2를 반환하도록 설정
        return 1.2;
    }
}
