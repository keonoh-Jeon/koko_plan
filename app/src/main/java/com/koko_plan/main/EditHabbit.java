package com.koko_plan.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.koko_plan.R;
import com.koko_plan.sub.MySoundPlayer;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditHabbit extends AppCompatActivity {

    private static final String TAG = "EditHabbit";
    EditText et_habbittitle;
    NumberPicker countPicker, hourPicker, minPicker, secPicker;

    private RadioButton radioButton1, radioButton2;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;

    private Map<String, CheckBox> map;

    private String habbitroutine;

    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habbit);

        radioButton1 = findViewById(R.id.rbtn_radioButton1);
        radioButton2 = findViewById(R.id.rbtn_radioButton2);

        this.SetListener();

        et_habbittitle = (EditText) findViewById(R.id.et_habbittitle);

        cb1 = (CheckBox)findViewById(R.id.checkBox1);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);
        cb4 = (CheckBox)findViewById(R.id.checkBox4);
        cb5 = (CheckBox)findViewById(R.id.checkBox5);
        cb6 = (CheckBox)findViewById(R.id.checkBox6);
        cb7 = (CheckBox)findViewById(R.id.checkBox7);

        countPicker  = (NumberPicker) findViewById(R.id.picker_count);
        countPicker.setMinValue(0);
        countPicker.setMaxValue(100);
        countPicker.setValue(1);

        hourPicker = (NumberPicker) findViewById(R.id.picker_hour);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        hourPicker.setValue(0);

        minPicker = (NumberPicker) findViewById(R.id.picker_min);
        minPicker.setMinValue(0);
        minPicker.setMaxValue(60);
        minPicker.setValue(0);

        secPicker = (NumberPicker) findViewById(R.id.picker_sec);
        secPicker.setMinValue(0);
        secPicker.setMaxValue(60);
        secPicker.setValue(0);
    }

    private void SetListener() {
        View.OnClickListener Listener = new View.OnClickListener() {



            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.rbtn_radioButton1:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        habbitroutine = "매일";
                        break;
                    case R.id.rbtn_radioButton2:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        habbitroutine = "매주";
                        break;
                }
            }
        };

        radioButton1.setOnClickListener(Listener);
        radioButton2.setOnClickListener(Listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void mOnConfirm(View v){

        boolean monday = false;
        boolean tuesday = false;
        boolean wednesday = false;
        boolean thursday = false;
        boolean friday = false;
        boolean saturday = false;
        boolean sunday = false;

        if(habbitroutine.equals("매일")) {
            monday = tuesday = wednesday = thursday = friday = saturday = sunday = true;

        } else {
            if(cb1.isChecked()) monday = true;
            if(cb2.isChecked()) tuesday = true;
            if(cb3.isChecked()) wednesday = true;
            if(cb4.isChecked()) thursday = true;
            if(cb4.isChecked()) friday = true;
            if(cb4.isChecked()) saturday = true;
            if(cb4.isChecked()) sunday = true;
        }

        String habbittitle = et_habbittitle.getText().toString();
        int count = countPicker.getValue();
        int hour = hourPicker.getValue();
        int min = minPicker.getValue();
        int sec = secPicker.getValue();
        boolean isrunning = false;

        Intent intent = new Intent();

        intent.putExtra("habbittitle", habbittitle);
        intent.putExtra("count", count);
        intent.putExtra("hour", hour);
        intent.putExtra("min", min);
        intent.putExtra("sec", sec);
        intent.putExtra("habbitroutine", habbitroutine);
        intent.putExtra("isrunning", isrunning);
        intent.putExtra("monday", monday);
        intent.putExtra("tuesday", tuesday);
        intent.putExtra("wednesday", wednesday);
        intent.putExtra("thursday", thursday);
        intent.putExtra("friday", friday);
        intent.putExtra("saturday", saturday);
        intent.putExtra("sunday", sunday);

        setResult(RESULT_OK, intent);

        finish();
    }
}