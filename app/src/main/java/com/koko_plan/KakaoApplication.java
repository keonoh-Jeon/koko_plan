package com.koko_plan;

import android.app.Application;
import android.util.Log;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"855eeff4baf07de4edf721315792ee8b");
    }
}
