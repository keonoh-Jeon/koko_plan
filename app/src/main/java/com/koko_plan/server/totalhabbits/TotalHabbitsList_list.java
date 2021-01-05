package com.koko_plan.server.totalhabbits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;
import com.koko_plan.server.goodtext.GoodText_ViewListener;
import com.koko_plan.server.ranking.Ranking_Adapter;
import com.koko_plan.server.ranking.Ranking_Item;
import com.koko_plan.server.ranking.Ranking_ViewListener;
import com.koko_plan.sub.MySoundPlayer;

import java.util.ArrayList;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class TotalHabbitsList_list extends AppCompatActivity implements TotalHabbitsList_ViewListener {

    private static final String TAG = "TotalHabbitList";
    public static ArrayList<TotalHabbitsList_Item> totalhabbitlist_items = null;
    private TotalHabbitsList_Adapter totalHabbitsList_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_totalhabbit_list);

        // 뷰 초기화
        initView();

        findViewById(R.id.iv_back).setOnClickListener(OnClickListener);
        findViewById(R.id.mc_menu).setOnClickListener(OnClickListener);

        AdView adBanner = findViewById(R.id.adView_totalhabbit);
        AdRequest adRequest = new AdRequest.Builder().build();
        adBanner.loadAd(adRequest);
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
        totalhabbitlist_items = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.rv_totlahabbitlist);
        recyclerView.setLayoutManager(layoutManager);

        totalHabbitsList_adapter = new TotalHabbitsList_Adapter(totalhabbitlist_items, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(totalHabbitsList_adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        totalhabbitlist_items.removeAll(totalhabbitlist_items);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listenerDoc(){

        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                .document(firebaseUser.getUid())
                .collection("totalhabbits")
//                .orderBy("doing", Query.Direction.DESCENDING)

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
                                    Log.w("ADDED","TOTALHABBIT Data: " + dc.getDocument().getData());
                                    totalhabbitlist_items.add(totalhabbitlist_items.size(), dc.getDocument().toObject(TotalHabbitsList_Item.class));
//                                    rankingAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    Log.w("MODIFIED","Data: " + dc.getDocument().getData());
//                                    rankingAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.w("REMOVED", "Data: " + dc.getDocument().getData());
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                        totalHabbitsList_adapter.notifyDataSetChanged();

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