package com.koko_plan.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.koko_plan.R;

public class EditHabbit extends AppCompatActivity {

    EditText et_habbittitle;
    NumberPicker countPicker, hourPicker, minPicker, secPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habbit);

        et_habbittitle = (EditText) findViewById(R.id.et_habbittitle);

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void mOnConfirm(View v){

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
        intent.putExtra("isrunning", isrunning);

        setResult(RESULT_OK, intent);

        finish();
    }
}