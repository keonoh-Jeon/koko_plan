package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.main.TodoList_Item;

import java.util.ArrayList;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class BackgroundSaveRank extends Worker {

    private static final String TAG = "BackgroundSaveRank";
    public static ArrayList<TodoList_Item> todoListItems = null;
    private int myrank = 0;
    private float eventscore;
    private Data output;

    public BackgroundSaveRank(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        todoListItems = new ArrayList<>();
        todoListItems.clear();
    }

    // WorkManager를 호출한 액티비티로 데이터 반환
    private Data createOutputData(float eventscore) {

        Log.e(TAG, "createOutputData 확인" + eventscore);

        return new Data.Builder()
                .putFloat("eventscore", eventscore)
                .build();
    }

    @NonNull
    @Override
    public Result doWork() {
            if (firebaseUser != null) {
                String name = getInputData().getString("name");

                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint({"SetTextI18n", "DefaultLocale"})
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        todoListItems.add(document.toObject(TodoList_Item.class));
                                        if (Objects.equals(document.getData().get("name"), name)) {
                                            myrank = todoListItems.size();
                                            Log.e(TAG, "myrank 확인" + myrank);
                                        }
                                        eventscore = (float) ((float) myrank / (double) todoListItems.size() * 100.0);
                                    }
                                    createOutputData((long) eventscore);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
            return Result.success();
    }
}

