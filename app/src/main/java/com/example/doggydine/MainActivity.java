package com.example.doggydine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    public static LottieComposition mainAnimationComposition;
    private TextView timeBox;
    private TextView profileName;
    private CircleImageView profileImg;
    private Handler handler;
    private Runnable updateRunnable;
    private static final int CHECK_INTERVAL = 1000; // 1초
    private String currentClosestTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView lottieAnimationView_back = findViewById(R.id.lottieAnimationView_back);
        LottieAnimationView lottieAnimationView_dog_1 = findViewById(R.id.dog_sitting_1);
        LottieAnimationView lottieAnimationView_dog_2 = findViewById(R.id.dog_sitting_2);
        LottieAnimationView lottieAnimationView_dog_3 = findViewById(R.id.dog_sitting_3);

        // 캐시된 애니메이션을 설정합니다.
        if (Login.mainAnimationComposition != null) {
            lottieAnimationView_back.setComposition(Login.mainAnimationComposition);
        } else {
            // 만약 캐시된 애니메이션이 없으면 다시 로드합니다.
            lottieAnimationView_back.setAnimation(R.raw.background_main);
        }

        lottieAnimationView_back.loop(true);
        lottieAnimationView_back.playAnimation();

        lottieAnimationView_dog_1.setAnimation(R.raw.dog_1);
        lottieAnimationView_dog_1.loop(true);
        lottieAnimationView_dog_1.playAnimation();

        lottieAnimationView_dog_2.setAnimation(R.raw.dog_2);
        lottieAnimationView_dog_2.loop(true);
        lottieAnimationView_dog_2.playAnimation();

        lottieAnimationView_dog_3.setAnimation(R.raw.dog_3);
        lottieAnimationView_dog_3.loop(true);
        lottieAnimationView_dog_3.playAnimation();


        CircleImageView settingBtn = findViewById(R.id.setting_btn);

        ImageButton feed_btn = findViewById(R.id.feed_btn);
        ImageButton detecting_btn = findViewById(R.id.btn_detecting);
        timeBox = findViewById(R.id.time_box);
        profileName = findViewById(R.id.profile_name);
        profileImg = findViewById(R.id.profile_img);

        detecting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialog dialog = new AppCompatDialog(MainActivity.this, R.style.TransparentDialog);
                dialog.setContentView(R.layout.detecting);
                dialog.setCancelable(true);

                LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.LT_detecting_animation);
                lottieAnimationView.setAnimation(R.raw.loading_animation); // .json 파일을 로드
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();

                dialog.show();

                mFirebaseAuth = FirebaseAuth.getInstance();
                String uid = mFirebaseAuth.getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("Detected");

                // 업데이트할 데이터 설정
                Map<String, Object> updates = new HashMap<>();
                updates.put("start", true);
                updates.put("Detected_name", "");

                // 해당 항목만 업데이트
                databaseReference.updateChildren(updates);
                databaseReference.child("Detected_name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String detectedName = snapshot.getValue(String.class);
                        if (detectedName != null && !detectedName.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, DetectedFace.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });

        feed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Feeding.class);
                startActivity(intent);
            }
        });

        ImageButton chatbot_btn = findViewById(R.id.chatbot_btn);
        chatbot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatBot.class);
                startActivity(intent);
            }
        });

        ImageButton calender_btn = findViewById(R.id.calender_btn);
        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calender.class);
                startActivity(intent);
            }
        });

        handler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                fetchClosestFeedingTime();
                handler.postDelayed(this, CHECK_INTERVAL); // 1초마다 실행
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoggyDine").child("UserAccount").child(uid).child("pet");
            fetchClosestFeedingTime();
            handler.post(updateRunnable); // 시간 업데이트 시작
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable); // 시간 업데이트 중지
    }

    private void fetchClosestFeedingTime() {
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long closestTimeDiff = Long.MAX_VALUE;
                String closestTime = "";
                String closestDogName = "";
                String closestProfileImg = "";
                boolean timeFound = false;

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getDefault());
                // Date currentTime = new Date();
                Calendar now = Calendar.getInstance();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    String dogName = petSnapshot.child("dog_name").getValue(String.class);
                    String profileImg = petSnapshot.child("profile").child("profile1").getValue(String.class);

                    for (DataSnapshot timeSnapshot : petSnapshot.child("time").getChildren()) {
                        String timeValue = timeSnapshot.getValue(String.class);
                        try {
                            Date feedingTime = sdf.parse(timeValue);

                            Calendar feedingCalendar = Calendar.getInstance();
                            feedingCalendar.setTime(feedingTime);
                            feedingCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
                            feedingCalendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
                            feedingCalendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

                            if (feedingCalendar.before(now)) {
                                continue;
                            }

                            timeFound = true;
                            long diff = feedingCalendar.getTimeInMillis() - now.getTimeInMillis();

                            if (diff < closestTimeDiff) {
                                closestTimeDiff = diff;
                                closestTime = timeValue;
                                closestDogName = dogName;
                                closestProfileImg = profileImg;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // UI 업데이트가 필요한 경우에만 업데이트
                if (!timeFound) {
                    timeBox.setText("설정된 시간 없음");
                    timeBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, 47);
                    profileName.setText("강아지 이름");
                    profileImg.setImageResource(R.drawable.ic_launcher_background);
                } else if (!closestTime.isEmpty() && !closestTime.equals(currentClosestTime)) {
                    currentClosestTime = closestTime;
                    timeBox.setText(closestTime);
                    timeBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 37);
                    profileName.setText(closestDogName);
                    Glide.with(MainActivity.this).load(closestProfileImg).into(profileImg);

                    setAlarmForFeedingTime(closestTime, closestDogName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAlarmForFeedingTime(String time, String dogName) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date feedingTime = sdf.parse(time);

            Calendar feedingCalendar = Calendar.getInstance();
            feedingCalendar.setTime(feedingTime);
            feedingCalendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            feedingCalendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            feedingCalendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.putExtra("dog_name", dogName);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE); // FLAG_CANCEL_CURRENT 플래그 사용
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, feedingCalendar.getTimeInMillis(), pendingIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
