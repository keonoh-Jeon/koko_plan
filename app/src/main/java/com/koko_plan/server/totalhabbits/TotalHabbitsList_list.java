package com.koko_plan.server.totalhabbits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.R;
import com.koko_plan.main.MainActivity;
import com.koko_plan.server.goodtext.GoodText_ViewListener;
import com.koko_plan.server.goodtext.RandomGoodText;
import com.koko_plan.server.ranking.Ranking_Adapter;
import com.koko_plan.server.ranking.Ranking_Item;
import com.koko_plan.server.ranking.Ranking_ViewListener;
import com.koko_plan.sub.ItemTouchHelperCallback;
import com.koko_plan.sub.MySoundPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class TotalHabbitsList_list extends AppCompatActivity implements TotalHabbitsList_ViewListener {

    private static final String TAG = "TotalHabbitList";
    public static ArrayList<TotalHabbitsList_Item> totalhabbitlist_items = null;
    private TotalHabbitsList_Adapter totalHabbitsList_adapter = null;

    private ItemTouchHelper helper;
    private RecyclerView recyclerView;

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

        recyclerView = findViewById(R.id.rv_totlahabbitlist);

        totalHabbitsList_adapter = new TotalHabbitsList_Adapter(totalhabbitlist_items, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(totalHabbitsList_adapter);

        //리스트 스와이프 기능 초기화
        initSwipe();
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

    //스와이프 기능 헬퍼 연결
    private void initSwipe() {
        Log.e(TAG, "initSwipe: " + "실행");
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT  | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                //왼쪽으로 밀었을때.
                if (direction == ItemTouchHelper.LEFT) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    firebaseFirestore
                                            .collection("users")
                                            .document(firebaseUser.getUid())
                                            .collection("total")
                                            .document(totalhabbitlist_items.get(position).getHabbittitle())
                                            .delete()

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e(TAG, "onSuccess: "  + totalhabbitlist_items.get(position).getHabbittitle());
                                                    totalhabbitlist_items.remove(position);
                                                    totalHabbitsList_adapter.notifyItemRemoved(position);
                                                }
                                            })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                }
                            });
                        }
                    }).start();

                } else {
                                            
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE ) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) { //오른쪽으로 밀었을 때

                    } else if (dX < 0) {
                        //배경
                        Paint p = new Paint();
                        p.setColor(Color.parseColor("#FFE67C"));
                        RectF background = new RectF((float) itemView.getRight() , (float) itemView.getTop(), (float) itemView.getRight() + dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "삭제";
                        p2.setColor(Color.parseColor("#295F2E"));
                        p2.setTextSize(30);
                        p2.setAntiAlias(true);
                        //텍스트 높이
                        Rect bounds = new Rect();
                        p2.getTextBounds(text, 0, text.length(), bounds);
                        int textheight = bounds.height();
                        //비트맵
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.deleteicon);
                        c.drawText(text, background.centerX() - p2.measureText(text)/2, background.centerY() + (float)(bmp.getWidth()/2) + (float)textheight, p2);
                        c.drawBitmap(bmp, background.centerX() - (float)(bmp.getWidth()/2), background.centerY() - (float)(bmp.getHeight()/2), null);
                        /*
                         * icon 추가할 수 있음.
                         */
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.deleteicon); //vector 불가!
//                         RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(totalHabbitsList_adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
    }

    private void listenerDoc(){

        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                .document(firebaseUser.getUid())
                .collection("total")
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