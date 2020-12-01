package com.koko_plan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.app.Fragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class MyTimerFragment extends Fragment {

    TextView dateNow;

    Timer mTimer;
    Handler mHandler = new Handler();
    private TextView mTextView;
    long mStartTime;

    private Context context;

    //제일 먼저 호출
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container,false);
        final TextView tvTitle = rootView.findViewById(R.id.tvTitle);
        final TextView tvTime = rootView.findViewById(R.id.tvTime);

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);
        Log.e(TAG, "onCreateView: "+ formatDate );

        if(getArguments() != null){
            tvTitle.setText(getArguments().getString("title"));
        }

        rootView.findViewById(R.id.tvTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);

                MyAlertDialogFragment dialog = MyAlertDialogFragment.newInstance(new MyAlertDialogFragment.TitleInputListener() {
                    @Override
                    public void onTitleInputComplete(String name) {
                        if(name != null) {
                        tvTitle.setText(name);
                        }
                    }
                });
                         dialog.show(getFragmentManager(), "addDialog");
            }
        });

        class DeviceEventReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (Intent.ACTION_TIME_CHANGED.equals(action)
                        || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
                    // 시간이 변경된 경우 해야 될 작업을 한다.
                    tvTime.setText(formatDate);
                }
            }
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        mTextView = (TextView)view.findViewById(R.id.textView1);

        mStartTime = SystemClock.elapsedRealtime();

        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        long total = SystemClock.elapsedRealtime() - mStartTime;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat f = new SimpleDateFormat("mm:ss");
                        String s = f.format(new Date(total)) + "." + ((int)(total % 1000) / 100);
                        mTextView.setText(s);
                    }
                });
            }
        }, 0, 50);

        Button button = (Button)view.findViewById(R.id.buttonStopDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimer != null){
                    mTimer.cancel();
                    mTimer = null;
                }
                else{
                    MyTimerFragment.this.getFragmentManager().beginTransaction().remove(MyTimerFragment.this).commit();
                }
            }
        });
    }
}
