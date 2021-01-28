package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.koko_plan.R;

public class kat_OverdrawActivity extends Service {

    private WindowManager windowManager;
    private View view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.activity_subscribe_popup, null);

        @SuppressLint("InlinedApi") WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(

                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT
        );
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(view, layoutParams);
    }

    @Override
    public void onDestroy() {
        if(windowManager != null) {        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(view != null) windowManager.removeView(view);
        }
        super.onDestroy();
    }


}