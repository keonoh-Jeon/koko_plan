package com.koko_plan.server.ranking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView ivrank;

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
        ivrank = findViewById(R.id.iv_rank);

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
        System.gc();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listenerDoc(){

        if (firebaseUser != null) {
            firebaseFirestore
                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                    .orderBy("progress", Query.Direction.DESCENDING)
                    .orderBy("getcount", Query.Direction.DESCENDING)
                    .orderBy("todaytarget", Query.Direction.DESCENDING)

                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        private int myrank;

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint({"SetTextI18n", "DefaultLocale"})
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

                                tvtotalranker.setText("( 전체 "+ ranking_items.size()+"명 중, 상위 " + String.format("%.2f", myrank/(double)ranking_items.size()*100.0)+"% 에 속합니다. )");
                                putrankimage(myrank/(double)ranking_items.size()*100.0);
                                editor.putLong("myranking", (long) (myrank/(double)ranking_items.size()*100.0));
                                editor.apply();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                        private void putrankimage(double rankscore) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        private Drawable drawable;

                                        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                                        @Override
                                        public void run() {
                                            if(99.94 < rankscore && rankscore <= 100) {
                                                tvranking.setText("Iron 4");
                                                drawable = getResources().getDrawable(R.drawable.iron);
                                                editor.putString("rank", "Iron 4");
                                            } else if(99.64 < rankscore && rankscore <= 99.94) {
                                                tvranking.setText("Iron 3");
                                                drawable = getResources().getDrawable(R.drawable.iron);
                                                editor.putString("rank", "Iron 3");
                                            } else if (98.94 < rankscore && rankscore <= 99.64) {
                                                tvranking.setText("Iron 2");
                                                drawable = getResources().getDrawable(R.drawable.iron);
                                                editor.putString("rank", "Iron 2");
                                            } else if (97.93 < rankscore && rankscore <= 98.94) {
                                                tvranking.setText("Iron 1");
                                                drawable = getResources().getDrawable(R.drawable.iron);
                                                editor.putString("rank", "Iron 1");
                                            } else if (95.53 < rankscore && rankscore <= 97.93) {
                                                tvranking.setText("Bronze 4");
                                                drawable = getResources().getDrawable(R.drawable.bronze);
                                                editor.putString("rank", "Bronze 4");
                                            } else if (92.78 < rankscore && rankscore <= 95.53) {
                                                tvranking.setText("Bronze 3");
                                                drawable = getResources().getDrawable(R.drawable.bronze);
                                                editor.putString("rank", "Bronze 3");
                                            } else if (88.73 < rankscore && rankscore <= 92.78) {
                                                tvranking.setText("Bronze 2");
                                                drawable = getResources().getDrawable(R.drawable.bronze);
                                                editor.putString("rank", "Bronze 2");
                                            } else if (82.76 < rankscore && rankscore <= 88.73) {
                                                tvranking.setText("Bronze 1");
                                                drawable = getResources().getDrawable(R.drawable.bronze);
                                                editor.putString("rank", "Bronze 1");
                                            } else if (73.61 < rankscore && rankscore <= 82.76) {
                                                tvranking.setText("Silver 4");
                                                drawable = getResources().getDrawable(R.drawable.silver);
                                                editor.putString("rank", "Silver 4");
                                            } else if (66.31 < rankscore && rankscore <= 73.61) {
                                                tvranking.setText("Silver 3");
                                                drawable = getResources().getDrawable(R.drawable.silver);
                                                editor.putString("rank", "Silver 3");
                                            } else if (57.53 < rankscore && rankscore <= 66.31) {
                                                tvranking.setText("Silver 2");
                                                drawable = getResources().getDrawable(R.drawable.silver);
                                                editor.putString("rank", "Silver 2");
                                            } else if (50.21 < rankscore && rankscore <= 57.53) {
                                                tvranking.setText("Silver 1");
                                                drawable = getResources().getDrawable(R.drawable.silver);
                                                editor.putString("rank", "Silver 1");
                                            } else if (36.76 < rankscore && rankscore <= 50.21) {
                                                tvranking.setText("Gold 4");
                                                drawable = getResources().getDrawable(R.drawable.gold);
                                                editor.putString("rank", "Gold 4");
                                            } else if (29.14 < rankscore && rankscore <= 36.76) {
                                                tvranking.setText("Gold 3");
                                                drawable = getResources().getDrawable(R.drawable.gold);
                                                editor.putString("rank", "Gold 3");
                                            } else if (22.53 < rankscore && rankscore <= 29.14) {
                                                tvranking.setText("Gold 2");
                                                drawable = getResources().getDrawable(R.drawable.gold);
                                                editor.putString("rank", "Gold 2");
                                            } else if (18.36 < rankscore && rankscore <= 22.53) {
                                                tvranking.setText("Gold 1");
                                                drawable = getResources().getDrawable(R.drawable.gold);
                                                editor.putString("rank", "Gold 1");
                                            } else if (10.58 < rankscore && rankscore <= 18.36) {
                                                tvranking.setText("Platinum 4");
                                                drawable = getResources().getDrawable(R.drawable.platinum);
                                                editor.putString("rank", "Platinum 4");
                                            } else if (7.58 < rankscore && rankscore <= 10.58) {
                                                tvranking.setText("Platinum 3");
                                                drawable = getResources().getDrawable(R.drawable.platinum);
                                                editor.putString("rank", "Platinum 3");
                                            } else if (5.59 < rankscore && rankscore <= 7.58) {
                                                tvranking.setText("Platinum 2");
                                                drawable = getResources().getDrawable(R.drawable.platinum);
                                                editor.putString("rank", "Platinum 2");
                                            } else if (3.67 < rankscore && rankscore <= 5.59) {
                                                tvranking.setText("Platinum 1");
                                                drawable = getResources().getDrawable(R.drawable.platinum);
                                                editor.putString("rank", "Platinum 1");
                                            } else if (1.45 < rankscore && rankscore <= 3.67) {
                                                tvranking.setText("Diamond 4");
                                                drawable = getResources().getDrawable(R.drawable.diamond);
                                                editor.putString("rank", "Diamond 4");
                                            } else if (0.68 < rankscore && rankscore <= 1.45) {
                                                tvranking.setText("Diamond 3");
                                                drawable = getResources().getDrawable(R.drawable.diamond);
                                                editor.putString("rank", "Diamond 3");
                                            } else if (0.31 < rankscore && rankscore <= 0.68) {
                                                tvranking.setText("Diamond 2");
                                                drawable = getResources().getDrawable(R.drawable.diamond);
                                                editor.putString("rank", "Diamond 2");
                                            } else if (0.11 < rankscore && rankscore <= 0.31) {
                                                tvranking.setText("Diamond 1");
                                                drawable = getResources().getDrawable(R.drawable.diamond);
                                                editor.putString("rank", "Diamond 1");
                                            } else if (0.06 < rankscore && rankscore <= 0.11) {
                                                tvranking.setText("Master");
                                                drawable = getResources().getDrawable(R.drawable.master);
                                                editor.putString("rank", "Master");
                                            } else if (0.02 < rankscore && rankscore <= 0.06) {
                                                tvranking.setText("G_Master");
                                                drawable = getResources().getDrawable(R.drawable.g_master);
                                                editor.putString("rank", "G_Master");
                                            } else if (0 < rankscore && rankscore <= 0.02) {
                                                tvranking.setText("Challenger");
                                                drawable = getResources().getDrawable(R.drawable.challenger);
                                                editor.putString("rank", "Challenger");
                                            }
                                            editor.putFloat("rankscore", (float) rankscore);
                                            ivrank.setImageDrawable(drawable);
                                            editor.apply();
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
        }

    }

    private void realtimelistenerDoc() {
        if (firebaseUser != null) {
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
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}