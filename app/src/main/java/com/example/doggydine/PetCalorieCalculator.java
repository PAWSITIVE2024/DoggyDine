package com.example.doggydine;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



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

/*  <Read.me>
    1.이렇게 만든이유
    :그냥 간단히 DB에서 data를 가져오면 되는줄 알았는데,
     db가 비동기적방식이여서 바로 return을 통해 외부로 값을전달하면안되고 callback함수한테 줘야한다고했다
     그래서 그냥 instance변수를 만들어서 여기에 넘기도록했다

    2.사용방법 :
     2-1) 객체생성 (생성자가 강아지 이름을 받게 했습니다) ->WeightCalculator2 pet1 = new WeightCalculator2("강아지이름");
     2-2) DB로부터 data 가져오기 ->pet1.setWeightFromDB()
     2-3) 가져온 Data 사용하기 double weight = pet1.getWeight();

     *정리 :
     1. 2-2) , 2-3) 순서를 꼭지켜야할것같다
     2. 일단test해보구 잘된다면 age는 참조값만 바꾸면 됩니다.(금방 할수있습니다)

  */
class WeightCalculator2 {
    private double weight;
    private String name;
    WeightCalculator2(String pet_name) {
        this.name = pet_name;
    }

    //클래스 변수 weight를 설정하는곳
    //그냥 간단히 가져오면 되는줄 알았는데 무슨 DB가 비동기적이여서 바로 외부로 return을 못하고 callback에 넘겨줘야한다고했다.
    //그래서 그냥 Instance 변수에 넣어두는 형식으로하고
    public void setWeightFromDB() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine")
                    .child("UserAccount")
                    .child(uid)
                    .child("pet")
                    .child(WeightCalculator2.this.name);
            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    PetAccount pet = datasnapshot.getValue(PetAccount.class);
                    if (pet != null) {
                        String weightStr = pet.getDog_weight();
                        double pet_weight = Double.parseDouble(weightStr);
                        WeightCalculator2.this.weight = pet_weight;
                    } else {
                        // 데이터가 없을 경우에 대한 처리
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 취소될 경우 예외 처리
                }
            });
        }
    }

    public double getWeight() {
        return this.weight;
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
