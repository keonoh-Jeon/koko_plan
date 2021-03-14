package com.koko_plan.sub;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.main.MainActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class BackgroundSetzero extends Worker {

    private static final String TAG = "BackgroundSetzero";

    public BackgroundSetzero(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {
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
                                        curtime.put("curcount", 0);

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
                getcount.put("progress", 0);

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

        initWorkManagersetzero();

        MainActivity.setzeroavailable = true;
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    private void initWorkManagersetzero() {
        long zero = getTimeUsingInWorkRequest(0,0,0);
        Log.e(TAG, "initWorkManagersetzero:  확인 재설정" + zero);
        WorkRequest setzeroWorkRequest = new OneTimeWorkRequest
                .Builder(BackgroundSetzero.class)
                .setInitialDelay(zero, TimeUnit.MILLISECONDS)
                .addTag("notify_setzero")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(setzeroWorkRequest);
    }

    private long getTimeUsingInWorkRequest(int i, int i1, int i2) {

        //현재 시간을 밀리세컨으로 받음
        Calendar currentDate = Calendar.getInstance();

        //현재 시간, 분, 초로 표기시 사용
        Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR_OF_DAY, i);
        dueDate.set(Calendar.MINUTE, i1);
        dueDate.set(Calendar.SECOND, i2);

        if(dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }
        return dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
    }
}