package com.example.doggydine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;


public class Recommend extends AppCompatActivity {
    private static final String PREF_NAME = "FoodSelectionPref";
    private static final String PREF_KEY_PREFIX = "food_selected_";

    // 음식 아이콘(ImageButton) 정의
    private ImageButton mPotato;
    private ImageButton mCrab;
    private ImageButton mSweetPotato;
    private ImageButton mWheat;
    private ImageButton mInsect;
    private ImageButton mChicken;
    private ImageButton mCarrot;
    private ImageButton mPig;
    private ImageButton mShrimp;
    private ImageButton mCow;
    private ImageButton mSheep;
    private ImageButton mSalmon;
    private ImageButton mDuck;
    private ImageButton mMilk;
    private ImageButton mCheese;
    private ImageButton mTurkey;
    private ImageButton mBean;
    private ImageButton mSunFlower;
    private ImageButton mPumpkin;
    private ImageButton mGinseng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        // ImageButton 초기화
        mPotato = findViewById(R.id.potato_icon);
        mCrab = findViewById(R.id.crab_icon);
        mSweetPotato = findViewById(R.id.sweetpotato_icon);
        mWheat = findViewById(R.id.wheat_icon);
        mInsect = findViewById(R.id.insect_icon);
        mChicken = findViewById(R.id.chicken_icon);
        mCarrot = findViewById(R.id.carrot_icon);
        mPig = findViewById(R.id.pig_icon);
        mShrimp = findViewById(R.id.shrimp_icon);
        mCow = findViewById(R.id.cow_icon);
        mSheep = findViewById(R.id.sheep_icon);
        mSalmon = findViewById(R.id.salmon_icon);
        mDuck = findViewById(R.id.duck_icon);
        mMilk = findViewById(R.id.milk_icon);
        mCheese = findViewById(R.id.cheese_icon);
        mTurkey = findViewById(R.id.turkey_icon);
        mBean = findViewById(R.id.bean_icon);
        mSunFlower = findViewById(R.id.sunflower_icon);
        mPumpkin = findViewById(R.id.pumpkin_icon);
        mGinseng = findViewById(R.id.ginseng_icon);



        // 이전 선택 상태 복원
        restoreSelectionStates();



        // 각 ImageButton에 클릭 리스너 설정
        mPotato.setOnClickListener(v -> toggleSelection(v, "potato"));
        mCrab.setOnClickListener(v -> toggleSelection(v, "crab"));
        mSweetPotato.setOnClickListener(v -> toggleSelection(v, "sweetpotato"));
        mWheat.setOnClickListener(v -> toggleSelection(v, "wheat"));
        mInsect.setOnClickListener(v -> toggleSelection(v, "insect"));
        mChicken.setOnClickListener(v -> toggleSelection(v, "chicken"));
        mCarrot.setOnClickListener(v -> toggleSelection(v, "carrot"));
        mPig.setOnClickListener(v -> toggleSelection(v, "pig"));
        mShrimp.setOnClickListener(v -> toggleSelection(v, "shrimp"));
        mCow.setOnClickListener(v -> toggleSelection(v, "cow"));
        mSheep.setOnClickListener(v -> toggleSelection(v, "sheep"));
        mSalmon.setOnClickListener(v -> toggleSelection(v, "salmon"));
        mDuck.setOnClickListener(v -> toggleSelection(v, "duck"));
        mMilk.setOnClickListener(v -> toggleSelection(v, "milk"));
        mCheese.setOnClickListener(v -> toggleSelection(v, "cheese"));
        mTurkey.setOnClickListener(v -> toggleSelection(v, "turkey"));
        mBean.setOnClickListener(v -> toggleSelection(v, "bean"));
        mSunFlower.setOnClickListener(v -> toggleSelection(v, "sunflower"));
        mPumpkin.setOnClickListener(v -> toggleSelection(v, "pumpkin"));
        mGinseng.setOnClickListener(v -> toggleSelection(v, "ginseng"));

    }



    // 선택 상태를 전환하는 메서드
    private void toggleSelection(View view, String foodName) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isSelected = preferences.getBoolean(PREF_KEY_PREFIX + foodName, false);
        isSelected = !isSelected;
        preferences.edit().putBoolean(PREF_KEY_PREFIX + foodName, isSelected).apply();
        updateUI(view, isSelected);
    }


    // 이전 선택 상태 복원
    private void restoreSelectionStates() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // 각 ImageButton의 선택 상태를 복원합니다.
        restoreSelectionState(mPotato, preferences, "potato");
        restoreSelectionState(mCrab, preferences, "crab");
        restoreSelectionState(mSweetPotato, preferences, "sweetpotato");
        restoreSelectionState(mWheat, preferences, "wheat");
        restoreSelectionState(mInsect, preferences, "insect");
        restoreSelectionState(mChicken, preferences, "chicken");
        restoreSelectionState(mCarrot, preferences, "carrot");
        restoreSelectionState(mPig, preferences, "pig");
        restoreSelectionState(mShrimp, preferences, "shrimp");
        restoreSelectionState(mCow, preferences, "cow");
        restoreSelectionState(mSheep, preferences, "sheep");
        restoreSelectionState(mSalmon, preferences, "salmon");
        restoreSelectionState(mDuck, preferences, "duck");
        restoreSelectionState(mMilk, preferences, "milk");
        restoreSelectionState(mCheese, preferences, "cheese");
        restoreSelectionState(mTurkey, preferences, "turkey");
        restoreSelectionState(mBean, preferences, "bean");
        restoreSelectionState(mSunFlower, preferences, "sunflower");
        restoreSelectionState(mPumpkin, preferences, "pumpkin");
        restoreSelectionState(mGinseng, preferences, "ginseng");

    }

    // 각 ImageButton의 이전 선택 상태를 복원하는 메서드
    private void restoreSelectionState(ImageButton imageButton, SharedPreferences preferences, String foodName) {
        boolean isSelected = preferences.getBoolean(PREF_KEY_PREFIX + foodName, false);
        updateUI(imageButton, isSelected);
    }

    // UI 업데이트를 위한 메서드
    private void updateUI(View view, boolean isSelected) {
        if (isSelected) {
            view.setBackgroundResource(R.drawable.rounder_coner_background_selected);
        } else {
            view.setBackgroundResource(R.drawable.rounder_coner);
        }
    }
}