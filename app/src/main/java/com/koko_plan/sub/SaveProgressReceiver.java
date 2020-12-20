package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;
import com.koko_plan.main.RecyclerAdapter;
import com.koko_plan.main.Todo;
import com.koko_plan.member.MemberActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.curtime;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.roomdb;
import static com.koko_plan.main.MainActivity.todaydate;
import static com.koko_plan.main.RecyclerAdapter.items;

public class SaveProgressReceiver extends BroadcastReceiver {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private Date newdate;
    private Calendar calendar;
    private SimpleDateFormat dateformat;
    private String newtoday;

    @Override
    public void onReceive(Context context, Intent intent) {

        // 현재 날짜 구하기
        newdate = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(newdate);
        //날짜 표시 형식 지정
        dateformat = new SimpleDateFormat("yyyy-MM-dd");
        newtoday = dateformat.format(newdate);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        saveprogresstofirebase();

        saveDairyprogresstofirebase();
    }

    private void saveprogresstofirebase() {
        SaveprogressThread thread = new SaveprogressThread();
        thread.start();
    }
    class SaveprogressThread extends Thread {
        public void run() {

            for(int i=0; i < roomdb.todoDao().search(todaydate).size(); i++ ) {

                Log.e(TAG, "SaveprogressThread : "+ roomdb.todoDao().search(todaydate).size());

                String date = roomdb.todoDao().search(todaydate).get(i).getDate();
                Log.e(TAG, "SaveprogressThread : " + date);
                String habbit = roomdb.todoDao().search(todaydate).get(i).getTitle();
                int curtime = roomdb.todoDao().search(todaydate).get(i).getCurtime();
                int curcount = roomdb.todoDao().search(todaydate).get(i).getCount();
                int totalsec = roomdb.todoDao().search(todaydate).get(i).getTotalsec();

                Map<String, Object> todayprogresslist = new HashMap<>();
                todayprogresslist.put("habbit", habbit);
                todayprogresslist.put("curtime", curtime);
                todayprogresslist.put("curcount", curcount);
                todayprogresslist.put("totalsec", totalsec);



                if (firebaseUser != null) {
                    firebaseFirestore.collection("users")
                            .document(firebaseUser.getUid())
                            .collection("dates")
                            .document(todaydate)
                            .collection("habbits")
                            .document(habbit)
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
                roomdb.todoDao().search(todaydate).get(i).setDate(newtoday);
                roomdb.todoDao().update(roomdb.todoDao().search(todaydate).get(i));
            }
        }
    }

    private void saveDairyprogresstofirebase() {
        DairyprogresstofirebaseThread thread = new DairyprogresstofirebaseThread();
        thread.start();
    }
    class DairyprogresstofirebaseThread extends Thread {
        public void run() {

            for(int i=0; i < roomdb.todoDao().search(todaydate).size(); i++ ) {

                String date = roomdb.todoDao().search(todaydate).get(i).getDate();
                String habbit = roomdb.todoDao().search(todaydate).get(i).getTitle();
                int curtime = roomdb.todoDao().search(todaydate).get(i).getCurtime();
                int curcount = roomdb.todoDao().search(todaydate).get(i).getCount();
                int totalsec = roomdb.todoDao().search(todaydate).get(i).getTotalsec();

                Map<String, Object> todayprogresslist = new HashMap<>();
                todayprogresslist.put("habbit", habbit);
                todayprogresslist.put("curtime", curtime);
                todayprogresslist.put("curcount", curcount);
                todayprogresslist.put("totalsec", totalsec);
            }




        }
    }
}
