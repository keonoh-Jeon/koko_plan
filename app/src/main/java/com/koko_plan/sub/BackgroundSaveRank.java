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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.main.MainActivity;
import com.koko_plan.main.TodoList_Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class BackgroundSaveRank extends Worker {

    private static final String TAG = "BackgroundSaveRank";
    public static ArrayList<TodoList_Item> todoListItems;
    private int myrank = 0;
    private float eventscore;

    public BackgroundSaveRank(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //        todoListItems.removeAll(todoListItems);
    }

    @NonNull
    @Override
    public Result doWork() {
            if (firebaseUser != null) {
                String name = getInputData().getString("name");
                String todaydate = getInputData().getString("todaydate");
                long interval = getInputData().getLong("interval", 0);
                todoListItems = new ArrayList<>();

                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                        .orderBy("progress", Query.Direction.DESCENDING)
                        .orderBy("getcount", Query.Direction.DESCENDING)
                        .orderBy("todaytarget", Query.Direction.DESCENDING)

                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @SuppressLint({"SetTextI18n", "DefaultLocale"})
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    todoListItems.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        todoListItems.add(document.toObject(TodoList_Item.class));
                                        if (Objects.equals(document.getData().get("name"), name)) {
                                            myrank = todoListItems.size();
                                        }
                                        eventscore = (float) ((float) myrank / (double) todoListItems.size() * 100.0);

                                        DocumentReference documentReference = firebaseFirestore
                                                .collection("users")
                                                .document(firebaseUser.getUid());

                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();

                                                    if (document.exists()) {
                                                        if(0 < (long) document.getData().get(todaydate) && interval > 0) {

                                                            Map<String, Object> seteventscore = new HashMap<>();
                                                            seteventscore.put("eventscore", eventscore);

                                                            firebaseFirestore
                                                                    .collection("users")
                                                                    .document(firebaseUser.getUid())
                                                                    .set(seteventscore, SetOptions.merge())

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
                                                        Map<String, Object> seteventscore = new HashMap<>();
                                                        seteventscore.put("eventscore", 100.0);

                                                        firebaseFirestore
                                                                .collection("users")
                                                                .document(firebaseUser.getUid())
                                                                .set(seteventscore, SetOptions.merge())

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
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                MainActivity.setrankavailable = true;
            }
            return Result.success();
    }
}