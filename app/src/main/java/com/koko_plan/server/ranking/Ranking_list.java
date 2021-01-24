package com.koko_plan.server.ranking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;
import com.koko_plan.main.TodoList_Item;
import com.koko_plan.member.MemberActivity;
import com.koko_plan.member.Profile_Item;
import com.koko_plan.member.Singup;
import com.koko_plan.server.goodtext.GoodText_ViewListener;
import com.koko_plan.sub.MySoundPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.photourl;
import static com.koko_plan.main.MainActivity.todaydate;

public class Ranking_list extends AppCompatActivity implements Ranking_ViewListener, GoodText_ViewListener
{
    private static final String TAG = "TotalHabbitList";
    TextView tvtotalranker, tvmyrank, tvranking;
    private Context context = null;
    public static ArrayList<Ranking_Item> ranking_items = null;
    private Ranking_Adapter rankingAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);

        // 뷰 초기화
        initView();

        findViewById(R.id.iv_back).setOnClickListener(OnClickListener);
        findViewById(R.id.mc_menu).setOnClickListener(OnClickListener);

        AdView adBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adBanner.loadAd(adRequest);

        realtimelistenerDoc();

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    myStartActivity(MainActivity.class);
                    break;

                case R.id.mc_menu:
                    MySoundPlayer.play(MySoundPlayer.CLICK);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {

        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        listenerDoc();

        //파이어베이스 필드 검색
        new Thread(() -> {
            DocumentReference documentReference = firebaseFirestore
                    .collection("randomsource")
                    .document("goodtexts");

                    documentReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        private int i = 0;

                        @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData().size());
                                int ramdomtextsize = document.getData().size();
                                editor.putInt("ramdomtextsize", ramdomtextsize);
                                editor.apply();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }).start();
    }

    @Override
    public void onItemClick(View view, int position) {    }

    @Override
    public void onDelete() {
        Log.e("로그: ", "삭제" );
    }

    @Override
    public void onModify() {
        Log.e("로그: ", "수정" );
    }

    @SuppressLint("SetTextI18n")
    private void initView()
    {
        ranking_items = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.rv_rankinglist);
        recyclerView.setLayoutManager(layoutManager);

        rankingAdapter = new Ranking_Adapter(ranking_items, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rankingAdapter);


        tvmyrank = findViewById(R.id.tv_myrank);
        tvranking = findViewById(R.id.tv_ranking);
        tvtotalranker = findViewById(R.id.tv_totalranker);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ranking_items.removeAll(ranking_items);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listenerDoc(){

        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                .orderBy(todaydate, Query.Direction.DESCENDING)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    private int myrank;

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ranking_items.add(document.toObject(Ranking_Item.class));

                                if(Objects.equals(document.getData().get("name"), name)) {
                                    tvmyrank.setText(ranking_items.size()+"위");
                                    myrank = ranking_items.size();
                                }
                                rankingAdapter.notifyDataSetChanged();
                            }

                            tvtotalranker.setText("( 전체 "+ ranking_items.size()+"명 중에서 상위 " + myrank/(double)ranking_items.size()*100.0+"%에 속합니다. )");
                            putrankimage(myrank/(double)ranking_items.size()*100.0);
                            editor.putLong("myranking", (long) (myrank/(double)ranking_items.size()*100.0));
                            editor.apply();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    private void putrankimage(double v) {
                        new Thread(() -> {
                            tvranking.setText("Iron 4");
                            editor.putString("rank", "Iron 4");
                            if(v<=99.94) {
                                tvranking.setText("Iron 3");
                                editor.putString("rank", "Iron 3");
                                if (v <= 99.64) {
                                    tvranking.setText("Iron 2");
                                    editor.putString("rank", "Iron 2");
                                    if (v <= 98.94) {
                                        tvranking.setText("Iron 1");
                                        editor.putString("rank", "Iron 1");
                                        if (v <= 97.93) {
                                            tvranking.setText("Bronze 4");
                                            editor.putString("rank", "Bronze 4");
                                            if (v <= 95.53) {
                                                tvranking.setText("Bronze 3");
                                                editor.putString("rank", "Bronze 3");
                                                if (v <= 92.78) {
                                                    tvranking.setText("Bronze 2");
                                                    editor.putString("rank", "Bronze 2");
                                                    if (v <= 88.73) {
                                                        tvranking.setText("Bronze 1");
                                                        editor.putString("rank", "Bronze 1");
                                                        if (v <= 82.76) {
                                                            tvranking.setText("Silver 4");
                                                            editor.putString("rank", "Silver 4");
                                                            if (v <= 73.61) {
                                                                tvranking.setText("Silver 3");
                                                                editor.putString("rank", "Silver 3");
                                                                if (v <= 66.31) {
                                                                    tvranking.setText("Silver 2");
                                                                    editor.putString("rank", "Silver 2");
                                                                    if (v <= 57.53) {
                                                                        tvranking.setText("Silver 1");
                                                                        editor.putString("rank", "Silver 1");
                                                                        if (v <= 50.21) {
                                                                            tvranking.setText("Gold 4");
                                                                            editor.putString("rank", "Gold 4");
                                                                            if (v <= 36.76) {
                                                                                tvranking.setText("Gold 3");
                                                                                editor.putString("rank", "Gold 3");
                                                                                if (v <= 29.14) {
                                                                                    tvranking.setText("Gold 2");
                                                                                    editor.putString("rank", "Gold 2");
                                                                                    if (v <= 22.53) {
                                                                                        tvranking.setText("Gold 1");
                                                                                        editor.putString("rank", "Gold 1");
                                                                                        if (v <= 18.36) {
                                                                                            tvranking.setText("Platinum 4");
                                                                                            editor.putString("rank", "Platinum 4");
                                                                                            if (v <= 10.58) {
                                                                                                tvranking.setText("Platinum 3");
                                                                                                editor.putString("rank", "Platinum 3");
                                                                                                if (v <= 7.58) {
                                                                                                    tvranking.setText("Platinum 2");
                                                                                                    editor.putString("rank", "Platinum 2");
                                                                                                    if (v <= 5.59) {
                                                                                                        tvranking.setText("Platinum 1");
                                                                                                        editor.putString("rank", "Platinum 1");
                                                                                                        if (v <= 3.67) {
                                                                                                            tvranking.setText("Diamond 4");
                                                                                                            editor.putString("rank", "Diamond 4");
                                                                                                            if (v <= 1.45) {
                                                                                                                tvranking.setText("Diamond 3");
                                                                                                                editor.putString("rank", "Diamond 3");
                                                                                                                if (v <= 0.68) {
                                                                                                                    tvranking.setText("Diamond 2");
                                                                                                                    editor.putString("rank", "Diamond 2");
                                                                                                                    if (v <= 0.31) {
                                                                                                                        tvranking.setText("Diamond 1");
                                                                                                                        editor.putString("rank", "Diamond 1");
                                                                                                                        if (v <= 0.11) {
                                                                                                                            tvranking.setText("Master");
                                                                                                                            editor.putString("rank", "Master");
                                                                                                                            if (v <= 0.06) {
                                                                                                                                tvranking.setText("G_Master");
                                                                                                                                editor.putString("rank", "G_Master");
                                                                                                                                if (v <= 0.02) {
                                                                                                                                    tvranking.setText("Challenger");
                                                                                                                                    editor.putString("rank", "Challenger");
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            editor.apply();
                        }).start();
                    }
                });
    }

    private void realtimelistenerDoc() {
        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
//                .orderBy(todaydate, Query.Direction.DESCENDING)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("listen:error", e);
                            return;                        }
                        assert snapshots != null;
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.w("ADDED","Data: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.w("MODIFIED","Data: " + dc.getDocument().getData());
                                    rankingAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.w("REMOVED", "Data: " + dc.getDocument().getData());
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }

                    }
                });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}