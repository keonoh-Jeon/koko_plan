package com.koko_plan.sub;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.koko_plan.R;

public class CustomToastMaker {

    public static void show(Context context, String string)
    {
        View view = View.inflate(context, R.layout.toast_layout, null);
        TextView tvtoastmsg = view.findViewById(R.id.tv_toastmsg);
        tvtoastmsg.setText(string);
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }
}
