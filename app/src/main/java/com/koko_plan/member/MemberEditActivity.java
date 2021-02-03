package com.koko_plan.member;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.main.MainActivity;
import com.koko_plan.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.todaydate;

public class MemberEditActivity extends AppCompatActivity {

    private static final String TAG = "MemberInitActivity";
    private String name, birthday, gender;
    private TextView tv_name;
    private TextView tv_birthday;

    private RadioButton gender1, gender2;
    private FirebaseUser user;
    FirebaseFirestore db;
    private EditText et;

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

    @SuppressLint("SimpleDateFormat")
    public void OnClickHandler(View view)
    {
        // 현재 날짜 구하기
        Date date = new Date();
        //날짜 표시 형식 지정
        SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthformat = new SimpleDateFormat("MM");
        SimpleDateFormat dayformat = new SimpleDateFormat("dd");
        int year = Integer.parseInt(yearformat.format(date));
        int month = Integer.parseInt(monthformat.format(date));
        int day = Integer.parseInt(dayformat.format(date));

        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year,  month, day);

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
            bitmap.setImageBitmap(bt);
//            bitmap.setBackground(new ShapeDrawable(new OvalShape()));
            bitmap.setClipToOutline(true);}

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
                    et = new EditText(MemberEditActivity.this);
                    et.setText("  " + name);
                    ad.setView(et);
                    ad.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                    });

                    ad.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name = et.getText().toString();
                            checknickname(dialog, name.replaceAll(" ", ""));
                        }
                    });

                    ad.show();

                    break;
            }
        }
    };

    //닉네임 중복 여부 체크
    private void checknickname(DialogInterface dialog, String nick){

        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기

                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    private boolean duplication;

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("listen:error", e);
                            return;                        }

                        duplication = false;
                        String nickname = "이름 기입";

                        assert snapshots != null;
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {

                            switch (dc.getType()) {
                                case ADDED:
                                    if(Objects.equals(dc.getDocument().getData().get("name"), nick)
                                            && !Objects.equals(dc.getDocument().getData().get("id"), firebaseUser.getUid())){
                                        startToast("같은 닉네임이 존재합니다.");
                                        duplication = true;
                                    }
                                    break;
                                case MODIFIED:
                                    Log.w("MODIFIED","Data: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.w("REMOVED", "Data: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                        if(duplication) {
                            nickname = "";
                        } else {
                            nickname = et.getText().toString();
                        }
                        tv_name.setText(nickname);
                        String strColor = "#000000";
                        tv_name.setTextColor(Color.parseColor(strColor));
                        dialog.dismiss();
                    }
                });
    }

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

            if(firebaseUser != null) {
                Map<String, Object> profileset = new HashMap<>();
                profileset.put("name", name);
                profileset.put("birthday", birthday);
                profileset.put("gender", gender);

                firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid())
                        .set(profileset, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원 정보가 저장되었습니다.");
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

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("users")
                .document(firebaseUser.getUid());

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
                            if(gender !=null && gender.equals("female")) gender2.setChecked(true);

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
