package com.koko_plan.server.goodtext;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;
import java.util.Map;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.photourl;
import static com.koko_plan.main.MainActivity.profile;
import static com.koko_plan.main.MainActivity.todaydate;

public class SetMsgToUsers {

    public static void send(String day, String time, String text, String touser) {

        int randomnum = RandomGoodText.getTextnum();

        new Thread(() -> {
            //파이어베이스 저장 (리스트 목록 만들기)
            Map<String, Object> GoodTextList = new HashMap<>();
            GoodTextList.put("day", day);
            GoodTextList.put("time", time);
            GoodTextList.put("randomnum", randomnum);
            GoodTextList.put("from", photourl);
            GoodTextList.put("fromid", firebaseUser.getUid());

            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users")
                        .document(touser)
                        .collection("messages")
                        .document(time)
                        .set(GoodTextList)

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
