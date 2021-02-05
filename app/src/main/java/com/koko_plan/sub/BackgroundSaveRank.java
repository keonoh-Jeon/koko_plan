package com.koko_plan.sub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import static com.koko_plan.main.MainActivity.pref;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
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
import static android.content.Context.MODE_PRIVATE;
import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.pref;

public class BackgroundSaveRank extends Worker {

    private static final String TAG = "BackgroundSaveRank";
    public static ArrayList<TodoList_Item> todoListItems = null;
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

    @NonNull
    @Override
    public Result doWork() {

        String name = getInputData().getString("name");
        Log.e(TAG, "doWork 확인"+ name);

        new Thread(() -> {
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
                                        Log.e(TAG, "doWork 확인"+ myrank);
                                    }

                                    output = new Data.Builder()
                                            .putFloat("eventscore", (float) (myrank/(double)todoListItems.size()*100.0))
                                            .build();

                                    Log.e(TAG, "doWork 확인"+ (float) (myrank/(double)todoListItems.size()*100.0));

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }).start();

        // Indicate whether the work finished successfully with the Result
        return Result.success(output);
    }
}
