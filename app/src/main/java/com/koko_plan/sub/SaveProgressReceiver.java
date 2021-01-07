package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koko_plan.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.todaydate;
import static com.koko_plan.main.MainActivity.todayitemsize;
import static com.koko_plan.main.MainActivity.todoListItems;

public class SaveProgressReceiver extends BroadcastReceiver {

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        saveprogresstofirebase();
    }

    private void saveprogresstofirebase() {
        /*if(todayitemsize > 0){
            new Thread(() -> {
                for(int i=0; i < todoListItems.size(); i++ ) {

                    String date = todoListItems.get(i).getDay();
                    String habbit = todoListItems.get(i).getHabbittitle();
                    int curtime = todoListItems.get(i).getCurtime();
                    int curcount = todoListItems.get(i).getCount();
                    int totalsec = todoListItems.get(i).getTotalsec();

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
                                .collection("users")
                                .document(firebaseUser.getUid())
                                .collection("totalhabbits")
                                .document(habbit)
                                .collection("dates")
                                .document(todaydate)

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
        }*/
    }
}
