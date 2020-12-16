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
                    addclubtodatabase();
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

            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if(user != null) {
                MemberInfo memberInfo = new MemberInfo(name, birthday, gender);
                db.collection("users").document(user.getUid()).set(memberInfo)
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

    private void addclubtodatabase() {
        AddclubThread thread = new AddclubThread();
        thread.start();
    }

    class AddclubThread extends Thread {
        public void run() {

            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> driver = new HashMap<>();
            driver.put("club", "Driver  ");
            driver.put("set", 230);
            driver.put("loft", 0);
            driver.put("average", 0);

            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Driver  ")
                        .set(driver)
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

            Map<String, Object> wood3 = new HashMap<>();
            wood3.put("club", "Wood 3");
            wood3.put("set", 210);
            wood3.put("loft", 0);
            wood3.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Wood 3")
                        .set(wood3)
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

            Map<String, Object> hybrid2 = new HashMap<>();
            hybrid2.put("club", "Hybrid 2");
            hybrid2.put("set", 200);
            hybrid2.put("loft", 0);
            hybrid2.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Hybrid 2")
                        .set(hybrid2)
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

            Map<String, Object> hybrid3 = new HashMap<>();
            hybrid3.put("club", "Hybrid 3");
            hybrid3.put("set", 180);
            hybrid3.put("loft", 0);
            hybrid3.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Hybrid 3")
                        .set(hybrid3)
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

            Map<String, Object> iron4 = new HashMap<>();
            iron4.put("club", "Iron 4");
            iron4.put("set", 160);
            iron4.put("loft", 0);
            iron4.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 4")
                        .set(iron4)
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

            Map<String, Object> iron5 = new HashMap<>();
            iron5.put("club", "Iron 5");
            iron5.put("set", 150);
            iron5.put("loft", 0);
            iron5.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 5")
                        .set(iron5)
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

            Map<String, Object> iron6 = new HashMap<>();
            iron6.put("club", "Iron 6");
            iron6.put("set", 140);
            iron6.put("loft", 0);
            iron6.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 6")
                        .set(iron6)
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

            Map<String, Object> iron7 = new HashMap<>();
            iron7.put("club", "Iron 7");
            iron7.put("set", 130);
            iron7.put("loft", 0);
            iron7.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 7")
                        .set(iron7)
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

            Map<String, Object> iron8 = new HashMap<>();
            iron8.put("club", "Iron 8");
            iron8.put("set", 120);
            iron8.put("loft", 0);
            iron8.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 8")
                        .set(iron8)
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

            Map<String, Object> iron9 = new HashMap<>();
            iron9.put("club", "Iron 9");
            iron9.put("set", 110);
            iron9.put("loft", 0);
            iron9.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Iron 9")
                        .set(iron9)
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

            Map<String, Object> pw = new HashMap<>();
            pw.put("club", "Wedge P");
            pw.put("set", 100);
            pw.put("loft", 0);
            pw.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Wedge P")
                        .set(pw)
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

            Map<String, Object> aw = new HashMap<>();
            aw.put("club", "Wedge A");
            aw.put("set", 90);
            aw.put("loft", 0);
            aw.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Wedge A")
                        .set(aw)
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

            Map<String, Object> sw = new HashMap<>();
            sw.put("club", "Wedge S");
            sw.put("set", 80);
            sw.put("loft", 0);
            sw.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Wedge S")
                        .set(sw)
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

            Map<String, Object> lw = new HashMap<>();
            lw.put("club", "Wedge L");
            lw.put("set", 70);
            lw.put("loft", 0);
            lw.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Wedge L")
                        .set(lw)
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

            Map<String, Object> putter = new HashMap<>();
            putter.put("club", "Putter  ");
            putter.put("set", 0);
            putter.put("loft", 0);
            putter.put("average", 0);
            if (user != null) {
                db.collection("users")
                        .document(user.getUid())
                        .collection("clubs")
                        .document("Putter  ")
                        .set(putter)
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
        }
    }
}
