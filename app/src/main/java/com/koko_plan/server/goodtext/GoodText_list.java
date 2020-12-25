package com.koko_plan.server.goodtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
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

public class GoodText_list extends AppCompatActivity implements GoodText_ViewListener, TextWatcher
{
    private static final String TAG = "Ranking_list";
    private Context context = null;
    public static ArrayList<GoodText_Item> goodText_items = null;
    private GoodText_Adapter goodText_adapter = null;

    private String drive = null;
    private String uid = null;

    public static String date, progress;
    public static int strSet, set, strLoft;

    private TextView clubtitle, clubnumber;
    private EditText clubset, clubloft;
    private EditText searchnickname;

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
        searchnickname = findViewById(R.id.et_searchnickname);
        searchnickname.addTextChangedListener(this);

        goodText_items = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.view_msg);
        recyclerView.setLayoutManager(layoutManager);

        goodText_adapter = new GoodText_Adapter(goodText_items, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(goodText_adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        goodText_items.removeAll(goodText_items);
//        ranking_items = new ArrayList<>();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listenerDoc(){

        Log.e(TAG, "listenerDoc: "+ name);

        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
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
                                    goodText_items.add(goodText_items.size(), dc.getDocument().toObject(GoodText_Item.class));
                                    Log.e(TAG, "onEvent: " +  dc.getDocument().get(todaydate));
//                                    rankingAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    Log.w("MODIFIED","Data: " + dc.getDocument().getData());
//                                    maketoast("this club is already exist.");
//                                    rankingAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.w("REMOVED", "Data: " + dc.getDocument().getData());
//                                    clubAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                        goodText_adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        goodText_adapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}