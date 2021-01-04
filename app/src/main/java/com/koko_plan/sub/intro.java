package com.koko_plan.sub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.koko_plan.main.MainActivity;
import com.koko_plan.R;

public class intro extends AppCompatActivity {

    private Handler handler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(intro.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        init();

        handler.postDelayed(runnable, 3000);
    }

    public void init() {

        LottieAnimationView lottieAnimationView = findViewById(R.id.plans);
        lottieAnimationView.setAnimation("calendaranimation.json");
        lottieAnimationView.setRepeatCount(3);
        lottieAnimationView.playAnimation();

        handler = new Handler();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}


