package com.koko_plan.member;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.main.MainActivity;
import com.koko_plan.R;

public class MemberEditActivity extends AppCompatActivity {

    private static final String TAG = "MemberInitActivity";
    private String name, birthday, gender;
    private TextView tv_name;
    private TextView tv_birthday;

    private RadioButton gender1, gender2;
    private FirebaseUser user;
    FirebaseFirestore db;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private DatePickerDialog.OnDateSetListener callbackMethod;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_edit);

        init();

        listenerDoc();
        this.InitializeView();
        this.InitializeListener();

        this.SetListener();

    }

    public void InitializeView()
    {
        tv_birthday = findViewById(R.id.tv_birthday);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                tv_birthday.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2020,  1, 1);

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init (){

        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        gender1 = findViewById(R.id.gender1);
        gender2 = findViewById(R.id.gender2);
        TextView tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.nameETXT);
        ImageView bitmap = findViewById(R.id.nhtv_image);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_email.setText(MainActivity.email);
        tv_name.setText(name);
        bitmap.setImageBitmap(MainActivity.profile);
        if(MainActivity.profile == null) {
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            bitmap.setImageBitmap(bt);}
        bitmap.setBackground(new ShapeDrawable(new OvalShape()));
        bitmap.setClipToOutline(true);

        findViewById(R.id.MemberCheckBtn).setOnClickListener(OnClickListener);
        findViewById(R.id.edit_name).setOnClickListener(OnClickListener);
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.MemberCheckBtn:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    profileUpdate();
                    break;
                case R.id.edit_name:
//                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    AlertDialog.Builder ad = new AlertDialog.Builder(MemberEditActivity.this);
                    ad.setTitle("Nickname");
                    final EditText et = new EditText(MemberEditActivity.this);
                    et.setText("  " + name);
                    ad.setView(et);
                    ad.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            // Event
                        }
                    });

                    ad.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name = et.getText().toString();
                            tv_name.setText(name);
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
        myStartActivity(MainActivity.class);
        finish();
    }

    private void SetListener() {

        View.OnClickListener Listener = new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.gender1:
//                        MySoundPlayer.play(MySoundPlayer.CLICK);
                        gender = "male";
                        break;
                    case R.id.gender2:
//                        MySoundPlayer.play(MySoundPlayer.CLICK);
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
        birthday = ((TextView) findViewById(R.id.tv_birthday)).getText().toString().trim();

        if (gender == null) gender = "male";
        if (name.length() > 0 && birthday.length() > 0) {

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

    private void listenerDoc(){

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "memberedit data: " + document.getData());
                            Profile_Item profileItem = document.toObject(Profile_Item.class);

                            assert profileItem != null;
                            birthday = profileItem.getBirthday(); // 히스토리 아이템 규격을 통해서 데이터 획득
                            name = profileItem.getName();
                            gender = profileItem.getGender();

                            gender1.setChecked(true);
                            if(gender.equals("female")) gender2.setChecked(true);

                            tv_birthday.setText(birthday);
                            tv_name.setText(name);

                        } else {
                            Log.d(TAG, "No such document");
                            myStartActivity(MemberActivity.class);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
