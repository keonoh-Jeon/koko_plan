package com.koko_plan.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;
import com.koko_plan.sub.MySoundPlayer;

import java.util.ArrayList;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.todaydate;

public class Ranking_list extends AppCompatActivity implements Ranking_ViewListener
{
    private Context context = null;
    public static ArrayList<Ranking_Item> ranking_items = null;
    private Ranking_Adapter rankingAdapter = null;

    private String drive = null;
    private String uid = null;

    public static String date, strClubtitle;
    public static int strSet, set, strLoft;

    private TextView clubtitle, clubnumber;
    private EditText clubset, clubloft;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);

        // 뷰 초기화
        initView();

        findViewById(R.id.iv_back).setOnClickListener(OnClickListener);
        findViewById(R.id.mc_menu).setOnClickListener(OnClickListener);

        /*AdView adBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adBanner.loadAd(adRequest);*/

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
        Log.e("onStart: ", "start");

        listenerDoc();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

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
        Log.e("initView: ","재시작" );

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
//        layoutManager2.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.rv_rankinglist);
        recyclerView.setLayoutManager(layoutManager2);

        rankingAdapter = new Ranking_Adapter(ranking_items, this, this);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(rankingAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ranking_items = new ArrayList<>();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listenerDoc(){

        firebaseFirestore
                .collection("names")
                .document(name)
                .collection("dates")
                .orderBy(todaydate, Query.Direction.DESCENDING)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {

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
                                    ranking_items.add(ranking_items.size(), dc.getDocument().toObject(Ranking_Item.class));
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    Log.w("MODIFIED","Data: " + dc.getDocument().getData());
//                                    maketoast("this club is already exist.");
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.w("REMOVED", "Data: " + dc.getDocument().getData());
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                        rankingAdapter.notifyDataSetChanged();
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