package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.main.MainActivity;
import com.koko_plan.main.TodoList_Item;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.lastsec;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.todaydate;
import static com.koko_plan.main.MainActivity.todayitemsize;
import static com.koko_plan.main.MainActivity.todoListItems;

public class SaveProgressReceiver extends BroadcastReceiver {

    private Calendar calendar = Calendar.getInstance();

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            if(firebaseUser != null){
                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                        .document(firebaseUser.getUid())
                        .collection("total")
                        .whereEqualTo(getdayofweek(), true)

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String habbit = (String) document.getData().get("habbittitle");
                                        Boolean isrun = (Boolean) document.getData().get("isrunning");
                                        int curtime = Integer.parseInt(String.valueOf(Objects.requireNonNull(document.getData()).get("curtime")));
                                        int curcount = Integer.parseInt(String.valueOf(Objects.requireNonNull(document.getData()).get("count")));
                                        int totalsec = Integer.parseInt(String.valueOf(Objects.requireNonNull(document.getData()).get("totalsec")));
                                        int curtimesum = Integer.parseInt(String.valueOf(Objects.requireNonNull(document.getData()).get("curtimesum"))) + curtime;

                                        //저장 리스트 목록 만들기
                                        Map<String, Object> todayprogresslist = new HashMap<>();
                                        if(isrun) {
                                            long now = System.currentTimeMillis();
                                            long stoptime = pref.getLong("stoptime", 0);
                                            int timegap = (int)((now-stoptime)/1000);
                                            todayprogresslist.put("curtime", curtime + timegap);
                                        } else {
                                            todayprogresslist.put("curtime", curtime);
                                        }
                                        todayprogresslist.put("date", todaydate);
                                        todayprogresslist.put("habbittitle", habbit);
                                        todayprogresslist.put("count", curcount);
                                        todayprogresslist.put("totalsec", totalsec);

                                        assert habbit != null;
                                        firebaseFirestore
                                                .collection("users")
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

                                        Map<String, Object> sum = new HashMap<>();
                                        sum.put("curtimesum", curtimesum);

                                        firebaseFirestore
                                                .collection("users")
                                                .document(firebaseUser.getUid())
                                                .collection("total")
                                                .document(habbit)

                                                .set(sum, SetOptions.merge())
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
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }).start();
    }

    private String getdayofweek() {
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        String day = "";

        switch (dayNum) {
            case 1:
                day = "sunday";
                break;
            case 2:
                day = "monday";
                break;
            case 3:
                day = "tuesday";
                break;
            case 4:
                day = "wednesday";
                break;
            case 5:
                day = "thursday";
                break;
            case 6:
                day = "friday";
                break;
            case 7:
                day = "saturday";
                break;
        }
        return day;
    }
}
