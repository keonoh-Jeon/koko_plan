package com.koko_plan.server.goodtext;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.main.MainActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.koko_plan.member.MemberActivity;
import com.koko_plan.member.Profile_Item;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.todaydate;

public class RandomGoodText {

    private static final String TAG = "RandomGoodText" ;
    private static String text;
    private static int randomNum;

    public static String make(){

        int ramdomtextsize = pref.getInt("ramdomtextsize",0);
        randomNum = (int)(Math.random() * ramdomtextsize);
        Log.e(TAG, "onSuccess make: " + ramdomtextsize);
        String randomnum = String.valueOf(randomNum);
        Log.e(TAG, "onSuccess make: " + randomnum);

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
                            Log.e(TAG, "onComplete: make " + document.get(randomnum));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return text;
    }

    public static int getTextnum(){
        return randomNum;
    }
}
