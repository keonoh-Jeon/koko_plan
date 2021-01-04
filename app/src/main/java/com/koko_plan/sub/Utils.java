package com.koko_plan.sub;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.core.content.ContextCompat;

import com.koko_plan.R;

public class Utils {

    int standardSize_X, standardSize_Y;
    float density;

    public enum StatusBarColorType {
//        BLACK_STATUS_BAR(R.color.statue),
        GREEN_STATUS_BAR(R.color.statusbargreen );

        private int backgroundColorId;

        StatusBarColorType(int backgroundColorId){
            this.backgroundColorId = backgroundColorId;
        }

        public int getBackgroundColorId() {
            return backgroundColorId;
        }
    }

    public static void setStatusBarColor(Activity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }
}
