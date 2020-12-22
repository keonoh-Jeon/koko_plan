package com.koko_plan.member;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.main.MainActivity;
import com.koko_plan.R;
import com.koko_plan.sub.MySoundPlayer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class MemberActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "MemberInitActivity";

    private String name;
    private String gender;
    private TextView tv_name;

    private RadioButton gender1, gender2;

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private int curyear, curmonth, curday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        init();

        this.InitializeView();
        this.InitializeListener();

        this.SetListener();
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.textView_date);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                String strColor = "#000000";
                textView_Date.setTextColor(Color.parseColor(strColor));
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, curyear, curmonth-1, curday);
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void init (){

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.MemberCheckBtn).setOnClickListener(OnClickListener);
        findViewById(R.id.edit_name).setOnClickListener(OnClickListener);
        gender1 = findViewById(R.id.gender1);
        gender2 = findViewById(R.id.gender2);
        tv_name = findViewById(R.id.nameETXT);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        curyear = Integer.parseInt(yearFormat.format(currentTime));
        curmonth = Integer.parseInt(monthFormat.format(currentTime));
        curday = Integer.parseInt(dayFormat.format(currentTime));

        if (gender == null) gender = "male";
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MemberCheckBtn:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    profileUpdate();
                    break;
                case R.id.edit_name:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    AlertDialog.Builder ad = new AlertDialog.Builder(MemberActivity.this);
                    ad.setTitle("Nickname");
                    final EditText et = new EditText(MemberActivity.this);
                    et.setText("  " + MainActivity.name);
                    if(MainActivity.name == null) et.setText("  ");
                    ad.setView(et);
                    ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name = et.getText().toString();
                            tv_name.setText(name);
                            String strColor = "#000000";
                            tv_name.setTextColor(Color.parseColor(strColor));
                            dialog.dismiss();
                        }
                    });
                    ad.show();

                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private void SetListener() {
        View.OnClickListener Listener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.gender1:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        gender = "male";
                        break;
                    case R.id.gender2:
                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        gender = "female";
                        break;
                }
            }
        };

        gender1.setOnClickListener(Listener);
        gender2.setOnClickListener(Listener);
    }

    private void profileUpdate() {

        name = ((TextView) findViewById(R.id.nameETXT)).getText().toString().trim();
        String birthday = ((TextView) findViewById(R.id.textView_date)).getText().toString().trim();

        if (!name.equals("name")  && !birthday.equals("birthday")) {
            if(firebaseUser != null) {
                MemberInfo memberInfo = new MemberInfo(name, birthday, gender, firebaseUser.getUid());
                firebaseFirestore.collection("names").document(MainActivity.name)
                        .set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("success member enregistrement");
                                myStartActivity(MainActivity.class);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("Fail member enregistrement");
                            }
                        });
            }

        }else {
            startToast("please, write personal information");
        }
    }


    private void myStartActivity(Class c){

        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
