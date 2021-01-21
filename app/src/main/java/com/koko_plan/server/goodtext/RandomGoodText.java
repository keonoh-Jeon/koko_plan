package com.koko_plan.server.goodtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.koko_plan.sub.CustomToastMaker;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.pref;

public class RandomGoodText {

    private static final String TAG = "RandomGoodText" ;
    private static String text;
    private static int randomNum;

    public static void make(Context context, String userid, String day, String time){

        int ramdomtextsize = pref.getInt("ramdomtextsize",0);
        randomNum = (int)(Math.random() * ramdomtextsize);
        String randomnum = String.valueOf(randomNum);

        DocumentReference documentReference = firebaseFirestore
                .collection("randomsource")
                .document("goodtexts");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            text = (String) document.get(randomnum);
                            CustomToastMaker.show(context, text);
                            SetMsgToUsers.send(day, time, text, userid);
                            SetMsgToUsers2.send(userid);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static int getTextnum(){
        return randomNum;
    }
}
