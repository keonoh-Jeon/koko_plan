package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.koko_plan.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.roomdb;
import static com.koko_plan.main.MainActivity.todaydate;
import static com.koko_plan.main.MainActivity.todayitemsize;

public class SaveProgressReceiver extends BroadcastReceiver {

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: " + "save progress" );
        saveprogresstofirebase();
    }

    private void saveprogresstofirebase() {
        if(todayitemsize > 0){
            new Thread(() -> {
                for(int i=0; i < roomdb.todoDao().search(todaydate).size(); i++ ) {
                    String date = roomdb.todoDao().search(todaydate).get(i).getDate();
                    String habbit = roomdb.todoDao().search(todaydate).get(i).getTitle();
                    int curtime = roomdb.todoDao().search(todaydate).get(i).getCurtime();
                    int curcount = roomdb.todoDao().search(todaydate).get(i).getCount();
                    int totalsec = roomdb.todoDao().search(todaydate).get(i).getTotalsec();

                    //파이어베이스 저장
                    //저장 리스트 목록 만들기
                    Map<String, Object> todayprogresslist = new HashMap<>();
                    todayprogresslist.put("date", date);
                    todayprogresslist.put("habbit", habbit);
                    todayprogresslist.put("curtime", curtime);
                    todayprogresslist.put("curcount", curcount);
                    todayprogresslist.put("totalsec", totalsec);

                    if (firebaseUser != null) {
                        firebaseFirestore
                                .collection("names")
                                .document(name)
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
                }
            }).start();
        }
    }
}
