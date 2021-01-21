package com.koko_plan.server.goodtext;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.sub.CustomToastMaker;

import java.util.HashMap;
import java.util.Map;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.photourl;

public class SetMsgToUsers2 {

    private static long getcount;

    public static void send(String touser) {
        new Thread(() -> {
            DocumentReference documentReference = firebaseFirestore
                    .collection("users")
                    .document(touser);

            documentReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                getcount = (long) document.get("getcount");
                                getcount ++;
                            } else {

                            }
                        }
                    } else {

                    }
                }
            });

            //파이어베이스 저장 (리스트 목록 만들기)
            Map<String, Object> setcount = new HashMap<>();
            setcount.put("getcount", getcount);

            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users")
                        .document(touser)
                        .set(setcount, SetOptions.merge())

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
