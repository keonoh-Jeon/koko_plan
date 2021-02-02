package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.R;
import com.koko_plan.main.TodoList_Item;
import com.koko_plan.server.ranking.Ranking_Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.pref;
import static com.koko_plan.main.MainActivity.todaydate;

public class SaveRankReceiver extends BroadcastReceiver {

    private PowerManager.WakeLock sCpuWakeLock;
    public static ArrayList<TodoList_Item> todoListItems = null;

    @SuppressLint({"WakelockTimeout", "InvalidWakeLockTag","SimpleDateFormat"})
    @Override
    public void onReceive(Context context, Intent intent) {

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        new Thread(() -> {
            todoListItems = new ArrayList<>();
            todoListItems.clear();
            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            private int myrank = 0;

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint({"SetTextI18n", "DefaultLocale"})
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        todoListItems.add(document.toObject(TodoList_Item.class));
                                        if(Objects.equals(document.getData().get("name"), name)) {
                                            myrank = todoListItems.size();
                                        }
                                    }
                                    editor.putFloat("eventscore", (float) (myrank/(double)todoListItems.size()*100.0));
                                    editor.apply();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }).start();
    }
}
