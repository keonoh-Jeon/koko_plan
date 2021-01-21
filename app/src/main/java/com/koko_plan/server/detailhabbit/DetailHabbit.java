package com.koko_plan.server.detailhabbit;

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
import com.koko_plan.server.totalhabbits.TotalHabbitsList_list;
import com.koko_plan.sub.MySoundPlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class DetailHabbit extends AppCompatActivity implements Detailhabbit_ViewListener
{
    private static final String TAG = "DetailHabbit";
    private Context context = null;
    public static ArrayList<Detailhabbit_Item> detailhabbitItems = null;
    private Detailhabbit_Adapter detailhabbitAdapter = null;
    private TextView tvdetailtitle, tvduedate, tvstartdate , tvcountsum, tvcurtimesum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailhabbit_list);

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

        Intent secondIntent = getIntent();
        String detail = secondIntent.getStringExtra("detailhabbit");

        listenerDoc(detail);

        //파이어베이스 필드 검색
        new Thread(() -> {
            DocumentReference documentReference = firebaseFirestore
                    .collection("users")
                    .document(firebaseUser.getUid())
                    .collection("total")
                    .document(detail);

                    documentReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint({"SetTextI18n", "DefaultLocale"})
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    if (document.exists()) {
                                        tvstartdate.setText(""+document.getData().get("start"));

                                        String from = todaydate;
                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date todate = transFormat.parse(from);
                                            Date fromdate = transFormat.parse((String) Objects.requireNonNull(document.getData().get("start")));
                                            Calendar tocal = Calendar.getInstance();
                                            assert todate != null;
                                            tocal.setTime(todate);
                                            Calendar fromcal = Calendar.getInstance();
                                            assert fromdate != null;
                                            fromcal.setTime(fromdate);
                                            long a = (tocal.getTimeInMillis() - fromcal.getTimeInMillis())/86400000;
                                            tvduedate.setText(" +" + a + "day ");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        tvcountsum.setText(" " + document.getData().get("countsum") + "회 ");

                                        long curtimesum = (long) document.getData().get("curtimesum");
                                        long second = curtimesum % 60;
                                        long minute = (curtimesum / 60) % 60;
                                        long hour = (curtimesum / 3600) % 24;
                                        tvcurtimesum.setText(String.format("%02d:%02d:%02d", hour, minute, second)+ " ");

                                        /*long count = (long) document.getData().get("count");
                                        tv1count.setText(Long.toString(count) + "회 씩");

                                        long h = (long) document.getData().get("hour");
                                        long m = (long) document.getData().get("min");
                                        long s = (long) document.getData().get("sec");
                                        tv1counttime.setText(String.format("%02d:%02d:%02d", h, m, s));
*/

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

        tvdetailtitle = findViewById(R.id.tv_detailtitle);
        tvstartdate = findViewById(R.id.tv_startdate);
        tvduedate = findViewById(R.id.tv_duedate);

        tvcountsum = findViewById(R.id.tv_countsum);
        tvcurtimesum = findViewById(R.id.tv_curtimesum);

        detailhabbitItems = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.rv_detailhabbit);
        recyclerView.setLayoutManager(layoutManager);

        detailhabbitAdapter = new Detailhabbit_Adapter(detailhabbitItems, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailhabbitAdapter);

        //배너 광고 표기
        AdView adBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adBanner.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();

        detailhabbitItems.removeAll(detailhabbitItems);
//        ranking_items = new ArrayList<>();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void listenerDoc(String detail){

        tvdetailtitle.setText(detail + " Timeline");

        //파이어베이스 필드 검색
        new Thread(() -> {
            assert detail != null;
            firebaseFirestore
                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                    .document(firebaseUser.getUid())
                    .collection("total")
                    .document(detail)
                    .collection("dates")
                    .orderBy("date", Query.Direction.DESCENDING)

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
                                        detailhabbitItems.add(detailhabbitItems.size(), dc.getDocument().toObject(Detailhabbit_Item.class));
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
                            detailhabbitAdapter.notifyDataSetChanged();
                        }
                    });
        }).start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}