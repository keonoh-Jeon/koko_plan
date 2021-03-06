package com.koko_plan.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.koko_plan.R;
import com.koko_plan.sub.MySoundPlayer;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class EditHabbit extends AppCompatActivity {

    private static final String TAG = "EditHabbit";
    EditText et_habbittitle;
    NumberPicker countPicker, hourPicker, minPicker, secPicker;

    private RadioButton radioButton1, radioButton2;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7;

    private View vieweveryday;

    private String habbitroutine = "매일";

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    boolean monday = false;
    boolean tuesday = false;
    boolean wednesday = false;
    boolean thursday = false;
    boolean friday = false;
    boolean saturday = false;
    boolean sunday = false;

    @SuppressLint({"FindViewByIdCast", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habbit);

        radioButton1 = findViewById(R.id.rbtn_radioButton1);
        radioButton2 = findViewById(R.id.rbtn_radioButton2);

        this.SetListener();

        et_habbittitle = (EditText) findViewById(R.id.et_habbittitle);
        et_habbittitle.setFilters(new InputFilter[]{specialCharacterFilter});

        vieweveryday = findViewById(R.id.view_everyday);
        vieweveryday.setVisibility(View.INVISIBLE);

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

        // 저장 위치 초기화
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
    }

    /** 이모티콘이 있을경우 "" 리턴, 그렇지 않을 경우 null 리턴 **/
    private final InputFilter specialCharacterFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                // 이모티콘 패턴
                Pattern unicodeOutliers = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
                // '-' 입력 받고 싶을 경우 : unicodeOutliers.matcher(source).matches() && !source.toString().matches(".*-.*")
                if(unicodeOutliers.matcher(source).matches()) {
                    startToast("이모티콘 제한");
                    return ""; }
            }
            return null;
        }
    };

    private void SetListener() {
        View.OnClickListener Listener = new View.OnClickListener() {

            @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.rbtn_radioButton1:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        habbitroutine = "매일";
                        vieweveryday.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rbtn_radioButton2:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        habbitroutine = "매주";
                        vieweveryday.setVisibility(View.VISIBLE);
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

        if(!et_habbittitle.getText().toString().equals("") && countPicker.getValue()!=0 && hourPicker.getValue()!=0 | minPicker.getValue()!=0 | secPicker.getValue()!=0) {

            if(radioButton1.isChecked()) {
                habbitroutine = "매일";
                monday = tuesday = wednesday = thursday = friday = saturday = sunday = true;

            } else {
                habbitroutine = "매주";
                if(cb1.isChecked()) monday = true;
                if(cb2.isChecked()) tuesday = true;
                if(cb3.isChecked()) wednesday = true;
                if(cb4.isChecked()) thursday = true;
                if(cb5.isChecked()) friday = true;
                if(cb6.isChecked()) saturday = true;
                if(cb7.isChecked()) sunday = true;
            }

            String habbittitle = et_habbittitle.getText().toString();
            int count = countPicker.getValue();
            int hour = hourPicker.getValue();
            int min = minPicker.getValue();
            int sec = secPicker.getValue();
            boolean isrunning = false;

            @SuppressLint("DefaultLocale")
            int totalsec = (hour*60*60+min*60+sec)*count;

            Map<String, Object> todayprogresslist = new HashMap<>();
            todayprogresslist.put("start", todaydate);
            todayprogresslist.put("totalsec", totalsec);
            todayprogresslist.put("sumtotalsec", 0);
            todayprogresslist.put("count", count);
            todayprogresslist.put("countsum", 0);
            todayprogresslist.put("habbittitle", habbittitle);
            todayprogresslist.put("isrunning", isrunning);
            todayprogresslist.put("curtime", 0);
            todayprogresslist.put("habbitroutine", habbitroutine);
            todayprogresslist.put("curtimesum", 0);
            todayprogresslist.put("num", pref.getInt("todayitemsize", 0)+1);
            todayprogresslist.put("monday", monday);
            todayprogresslist.put("tuesday", tuesday);
            todayprogresslist.put("wednesday", wednesday);
            todayprogresslist.put("thursday", thursday);
            todayprogresslist.put("friday", friday);
            todayprogresslist.put("saturday", saturday);
            todayprogresslist.put("sunday", sunday);

            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid())
                        .collection("total")
                        .document(habbittitle)
                        .set(todayprogresslist)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
            finish();
        } else {
            startToast("습관 명과 시간을 설정해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}