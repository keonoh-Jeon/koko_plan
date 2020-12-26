package com.koko_plan.server.goodtext;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class SetMsgToUsers {

    public static void send(String text, String touser) {

        int randomnum = RandomGoodText.getTextnum();

        new Thread(() -> {
            //파이어베이스 저장 (리스트 목록 만들기)
            Map<String, Object> GoodTextList = new HashMap<>();
            GoodTextList.put("date", todaydate);
            GoodTextList.put("text", text);
            GoodTextList.put("randomnum", randomnum);

            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users")
                        .document(touser)
                        .collection("dates")
                        .document(todaydate)
                        .collection("messages")
                        .document(text)
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
