package com.example.doggydine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;


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
    private Button mBtn_adpat;
    private CheckBox mCheckBox;


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



        mCheckBox = findViewById(R.id.cb_total_select);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    selectAll();
                }
                else{
                    resetAll();
                }
            }
        });


        mBtn_adpat = findViewById(R.id.btn_adapt);
        mBtn_adpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSelectionStates();
                ArrayList<String> selectedIngredients = getSelectedIngredients();
                Intent intent = new Intent(Recommend.this, Feeding.class);
                // 선택된 재료 목록을 Feeding 액티비티로 전달
                intent.putExtra("selectedIngredients", selectedIngredients);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();

            }
        });



        // 이전 선택 상태 복원 필요없을것 같아 주석처리
        //restoreSelectionStates();



        // 각 ImageButton에 클릭 리스너 설정
        mPotato.setOnClickListener(v -> toggleSelection(v, "감자"));
        mCrab.setOnClickListener(v -> toggleSelection(v, "게껍질"));
        mSweetPotato.setOnClickListener(v -> toggleSelection(v, "고구마"));
        mWheat.setOnClickListener(v -> toggleSelection(v, "곡류"));
        mInsect.setOnClickListener(v -> toggleSelection(v, "곤충"));
        mChicken.setOnClickListener(v -> toggleSelection(v, "닭고기"));
        mCarrot.setOnClickListener(v -> toggleSelection(v, "당근"));
        mPig.setOnClickListener(v -> toggleSelection(v, "돼지고기"));
        mShrimp.setOnClickListener(v -> toggleSelection(v, "새우"));
        mCow.setOnClickListener(v -> toggleSelection(v, "소고기"));
        mSheep.setOnClickListener(v -> toggleSelection(v, "양고기"));
        mSalmon.setOnClickListener(v -> toggleSelection(v, "연어"));
        mDuck.setOnClickListener(v -> toggleSelection(v, "오리고기"));
        mMilk.setOnClickListener(v -> toggleSelection(v, "우유"));
        mCheese.setOnClickListener(v -> toggleSelection(v, "치즈"));
        mTurkey.setOnClickListener(v -> toggleSelection(v, "칠면조"));
        mBean.setOnClickListener(v -> toggleSelection(v, "콩류"));
        mSunFlower.setOnClickListener(v -> toggleSelection(v, "해바라기씨"));
        mPumpkin.setOnClickListener(v -> toggleSelection(v, "호박씨"));
        mGinseng.setOnClickListener(v -> toggleSelection(v, "홍삼"));

    }


    // 선택 상태를 전환하는 메서드
    private void toggleSelection(View view, String foodName) {
        boolean isSelected = !isSelected((ImageButton) view);
        updateUI(view, isSelected);
    }


    private void selectAll() {
        updateSelectionState(true);
    }

    private void resetAll() {
        updateSelectionState(false);
    }
    private void updateSelectionState(boolean isSelected) {
        updateUI(mPotato, isSelected);
        updateUI(mCrab, isSelected);
        updateUI(mSweetPotato, isSelected);
        updateUI(mWheat, isSelected);
        updateUI(mInsect, isSelected);
        updateUI(mChicken, isSelected);
        updateUI(mCarrot, isSelected);
        updateUI(mPig, isSelected);
        updateUI(mShrimp, isSelected);
        updateUI(mCow, isSelected);
        updateUI(mSheep, isSelected);
        updateUI(mSalmon, isSelected);
        updateUI(mDuck, isSelected);
        updateUI(mMilk, isSelected);
        updateUI(mCheese, isSelected);
        updateUI(mTurkey, isSelected);
        updateUI(mBean, isSelected);
        updateUI(mSunFlower, isSelected);
        updateUI(mPumpkin, isSelected);
        updateUI(mGinseng, isSelected);
    }


    // 이전 선택 상태 복원
    private void restoreSelectionStates() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // 각 ImageButton의 선택 상태를 복원합니다.
        restoreSelectionState(mPotato, preferences, "감자");
        restoreSelectionState(mCrab, preferences, "게껍질");
        restoreSelectionState(mSweetPotato, preferences, "고구마");
        restoreSelectionState(mWheat, preferences, "곡류");
        restoreSelectionState(mInsect, preferences, "곤충");
        restoreSelectionState(mChicken, preferences, "닭고기");
        restoreSelectionState(mCarrot, preferences, "당근");
        restoreSelectionState(mPig, preferences, "돼지고기");
        restoreSelectionState(mShrimp, preferences, "새우");
        restoreSelectionState(mCow, preferences, "소고기");
        restoreSelectionState(mSheep, preferences, "양고기");
        restoreSelectionState(mSalmon, preferences, "연어");
        restoreSelectionState(mDuck, preferences, "오리고기");
        restoreSelectionState(mMilk, preferences, "우유");
        restoreSelectionState(mCheese, preferences, "치즈");
        restoreSelectionState(mTurkey, preferences, "칠면조");
        restoreSelectionState(mBean, preferences, "콩류");
        restoreSelectionState(mSunFlower, preferences, "해바라기씨");
        restoreSelectionState(mPumpkin, preferences, "호박씨");
        restoreSelectionState(mGinseng, preferences, "홍삼");

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


    private void saveSelectionStates() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // 각 ImageButton의 선택 상태를 SharedPreferences에 저장
        editor.putBoolean(PREF_KEY_PREFIX + "감자", isSelected(mPotato));
        editor.putBoolean(PREF_KEY_PREFIX + "게껍질", isSelected(mCrab));
        editor.putBoolean(PREF_KEY_PREFIX + "고구마", isSelected(mSweetPotato));
        editor.putBoolean(PREF_KEY_PREFIX + "곡류", isSelected(mWheat));
        editor.putBoolean(PREF_KEY_PREFIX + "곤충", isSelected(mInsect));
        editor.putBoolean(PREF_KEY_PREFIX + "닭고기", isSelected(mChicken));
        editor.putBoolean(PREF_KEY_PREFIX + "당근", isSelected(mCarrot));
        editor.putBoolean(PREF_KEY_PREFIX + "돼지고기", isSelected(mPig));
        editor.putBoolean(PREF_KEY_PREFIX + "새우", isSelected(mShrimp));
        editor.putBoolean(PREF_KEY_PREFIX + "소고기", isSelected(mCow));
        editor.putBoolean(PREF_KEY_PREFIX + "양고기", isSelected(mSheep));
        editor.putBoolean(PREF_KEY_PREFIX + "연어", isSelected(mSalmon));
        editor.putBoolean(PREF_KEY_PREFIX + "오리고기", isSelected(mDuck));
        editor.putBoolean(PREF_KEY_PREFIX + "우유", isSelected(mMilk));
        editor.putBoolean(PREF_KEY_PREFIX + "치즈", isSelected(mCheese));
        editor.putBoolean(PREF_KEY_PREFIX + "칠면조", isSelected(mTurkey));
        editor.putBoolean(PREF_KEY_PREFIX + "콩류", isSelected(mBean));
        editor.putBoolean(PREF_KEY_PREFIX + "해바라기씨", isSelected(mSunFlower));
        editor.putBoolean(PREF_KEY_PREFIX + "호박씨", isSelected(mPumpkin));
        editor.putBoolean(PREF_KEY_PREFIX + "홍삼", isSelected(mGinseng));
        editor.apply();
    }
    private ArrayList<String> getSelectedIngredients() {
        ArrayList<String> selectedIngredients = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // 각 ImageButton의 선택 상태를 확인하고 선택된 재료를 selectedIngredients 목록에 추가
        if (isSelected(mPotato)) selectedIngredients.add("감자");
        if (isSelected(mCrab)) selectedIngredients.add("게껍질");
        if (isSelected(mSweetPotato)) selectedIngredients.add("고구마");
        if (isSelected(mWheat)) selectedIngredients.add("곡류");
        if (isSelected(mInsect)) selectedIngredients.add("곤충");
        if (isSelected(mChicken)) selectedIngredients.add("닭고기");
        if (isSelected(mCarrot)) selectedIngredients.add("당근");
        if (isSelected(mPig)) selectedIngredients.add("돼지고기");
        if (isSelected(mShrimp)) selectedIngredients.add("새우");
        if (isSelected(mCow)) selectedIngredients.add("소고기");
        if (isSelected(mSheep)) selectedIngredients.add("양고기");
        if (isSelected(mSalmon)) selectedIngredients.add("연어");
        if (isSelected(mDuck)) selectedIngredients.add("오리고기");
        if (isSelected(mMilk)) selectedIngredients.add("우유");
        if (isSelected(mCheese)) selectedIngredients.add("치즈");
        if (isSelected(mTurkey)) selectedIngredients.add("칠면조");
        if (isSelected(mBean)) selectedIngredients.add("콩류");
        if (isSelected(mSunFlower)) selectedIngredients.add("해바라기씨");
        if (isSelected(mPumpkin)) selectedIngredients.add("호박씨");
        if (isSelected(mGinseng)) selectedIngredients.add("홍삼");

        // 나머지 ImageButton에 대해서도 동일하게 처리

        return selectedIngredients;
    }



    // ImageButton의 선택 상태를 확인하는 메서드
    private boolean isSelected(ImageButton imageButton) {
        // 선택된 경우 true를 반환하고, 그렇지 않으면 false를 반환
        return imageButton.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.rounder_coner_background_selected).getConstantState());
    }
}