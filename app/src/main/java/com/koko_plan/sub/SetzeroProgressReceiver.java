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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.todaydate;

public class SetzeroProgressReceiver extends BroadcastReceiver {

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {

        new Thread(() -> {
            if(firebaseUser != null){
                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                        .document(firebaseUser.getUid())
                        .collection("total")

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String habbit = (String) document.getData().get("habbittitle");

                                        Map<String, Object> curtime = new HashMap<>();
                                        curtime.put("curtime", 0);

                                        assert habbit != null;
                                        firebaseFirestore
                                                .collection("users")
                                                .document(firebaseUser.getUid())
                                                .collection("total")
                                                .document(habbit)

                                                .set(curtime, SetOptions.merge())
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

                Map<String, Object> getcount = new HashMap<>();
                getcount.put("getcount", 0);

                firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid())
                        .set(getcount, SetOptions.merge())
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
        }).start();
    }
}
