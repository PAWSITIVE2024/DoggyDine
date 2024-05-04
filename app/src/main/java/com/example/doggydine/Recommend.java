package com.example.doggydine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Recommend extends AppCompatActivity {
    private ImageButton mPotato;
    private ImageButton mCrab;
    private ImageButton mSweatPotato;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mPotato = findViewById(R.id.potato_icon);
        mCrab = findViewById(R.id.crab_icon);
        mSweatPotato = findViewById(R.id.sweetpotato_icon);
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

        //클릭리스너 달아주기
        mPotato.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mPotato.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mPotato.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


        mCrab.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mCrab.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mCrab.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


        mSweatPotato.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mSweatPotato.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mSweatPotato.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


        mWheat.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mWheat.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mWheat.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


        mInsect.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mInsect.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mInsect.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


        mChicken.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mChicken.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mChicken.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mCarrot.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mCarrot.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mCarrot.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mPig.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mPig.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mPig.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mShrimp.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mShrimp.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mShrimp.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mCow.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mCow.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mCow.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mSheep.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수
            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mSheep.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mSheep.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mSalmon.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mSalmon.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mSalmon.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mDuck.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mDuck.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mDuck.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mMilk.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mMilk.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mMilk.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mCheese.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mCheese.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mCheese.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mTurkey.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mTurkey.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mTurkey.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mBean.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mBean.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mBean.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mSunFlower.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mSunFlower.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mSunFlower.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mPumpkin.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mPumpkin.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mPumpkin.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });
        mGinseng.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 선택 여부를 저장하는 변수

            @Override
            public void onClick(View view) {
                isSelected = !isSelected; // 선택 상태를 토글
                if (isSelected) {
                    // 선택된 상태일 때
                    mGinseng.setBackgroundResource(R.drawable.rounder_coner_background_selected);
                } else {
                    // 선택이 해제된 상태일 때
                    mGinseng.setBackgroundResource(R.drawable.rounder_coner);
                }
            }
        });


    }
}