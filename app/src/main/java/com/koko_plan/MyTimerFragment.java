package com.koko_plan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.app.Fragment;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class MyTimerFragment extends Fragment {

    Timer mTimer;
    Handler mHandler = new Handler();
    private TextView mTextView;
    long mStartTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container,false);
        final TextView tvTitle = rootView.findViewById(R.id.tvTitle);

        if(savedInstanceState != null){
            tvTitle.setText(savedInstanceState.getString("title"));
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 데이터 저장
        TextView textCounter = Objects.requireNonNull(getView()).findViewById(R.id.tvTitle);
        String title = textCounter.getText().toString();
        outState.putString("title", title);
    }

}
