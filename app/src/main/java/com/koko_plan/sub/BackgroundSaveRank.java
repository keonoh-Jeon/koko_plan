package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.koko_plan.main.TodoList_Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;

public class BackgroundSaveRank extends Worker {

    private static final String TAG = "BackgroundSaveRank";
    public static ArrayList<TodoList_Item> todoListItems = null;

    public BackgroundSaveRank(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        todoListItems = new ArrayList<>();
        todoListItems.clear();
    }

    @NonNull
    @Override
    public Result doWork() {

        new Thread(() -> {
            if (firebaseUser != null) {
                Log.e(TAG, "doWork 확인"+ firebaseUser.getUid());
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
                                            Log.e(TAG, "doWork 확인"+ myrank);
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

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}
