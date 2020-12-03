package com.koko_plan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static android.content.ContentValues.TAG;

import java.util.List;

public class EditHabbit extends AppCompatActivity {

    RadioGroup rg;
    EditText et_habbittitle;
    NumberPicker countPicker, hourPicker, minPicker, secPicker;
    View time, counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_habbit);

        et_habbittitle = (EditText) findViewById(R.id.et_habbittitle);

        rg = (RadioGroup) findViewById(R.id.rg_counter);

        countPicker  = (NumberPicker) findViewById(R.id.picker_count);
        countPicker.setMinValue(0);
        countPicker.setMaxValue(100);
        countPicker.setValue(0);

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

        time = findViewById(R.id.view_timeinput);
        time.setVisibility(View.INVISIBLE);
        counter = findViewById(R.id.view_countinput);
        counter.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton select = (RadioButton)findViewById(id);
                Log.d(TAG, "onCheckedChanged: " + id);
                switch (id) {
                    case 1:
                        counter.setVisibility(View.VISIBLE);
                        time.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        time.setVisibility(View.VISIBLE);
                        counter.setVisibility(View.INVISIBLE);
                        break;
                }
                
//                editText.setText(select.getText());
            }
        });
    }

    public void mOnConfirm(View v){

        String habbittitle = et_habbittitle.getText().toString();
        int count = countPicker.getValue();
        int hour = hourPicker.getValue();
        int min = minPicker.getValue();
        int sec = secPicker.getValue();

        Intent intent = new Intent();

        intent.putExtra("habbittitle", habbittitle);
        intent.putExtra("count", count);
        intent.putExtra("hour", hour);
        intent.putExtra("min", min);
        intent.putExtra("sec", sec);

        setResult(RESULT_OK, intent);

        finish();
    }
}