package com.koko_plan.server.totalhabbits;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.koko_plan.R;
import com.koko_plan.server.detailhabbit.DetailHabbit;
import com.koko_plan.main.MainActivity;
import com.koko_plan.sub.ItemTouchHelperCallback;
import com.koko_plan.sub.MySoundPlayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class TotalHabbitsList_list extends AppCompatActivity implements TotalHabbitsList_ViewListener, TotalHabbitsListReady_ViewListener {

    private static final String TAG = "TotalHabbitList";
    public static ArrayList<TotalHabbitsList_Item> totalhabbitlist_items = null;
    public static ArrayList<TotalHabbitsListReady_Item> totalHabbitsListReady_items = null;

    private TotalHabbitsList_Adapter totalHabbitsList_adapter = null;
    private TotalHabbitsListReady_Adapter totalHabbitsListReady_adapter = null;

    private ItemTouchHelper helper, helper2;
    private RecyclerView recyclerView, recyclerView2;

    private Calendar calendar;

    ImageView mcplushabbit;
    private TextView tvpercentage, tvtodaytarget;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_totalhabbit_list);

        // 뷰 초기화
        initView();

        tvpercentage = findViewById(R.id.tv_percentage);
        tvtodaytarget = findViewById(R.id.tv_todaytarget);

        findViewById(R.id.iv_back).setOnClickListener(OnClickListener);

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
            }
        }
    };

    private void myStartActivity(Class c) {

        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void myStartActivity2(String detailhabbit, Class c) {

        Intent intent = new Intent(this, c);
        intent.putExtra("detailhabbit", detailhabbit);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
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


        totalHabbitsListReady_items = new ArrayList<>();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setStackFromEnd(true);

        recyclerView2 = findViewById(R.id.rv_totlahabbitlistforready);

        totalHabbitsListReady_adapter = new TotalHabbitsListReady_Adapter(totalHabbitsListReady_items, this, this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(totalHabbitsListReady_adapter);

        //리스트 스와이프 기능 초기화
        initSwipe2();

    }

    @Override
    protected void onPause() {
        super.onPause();

        totalhabbitlist_items.removeAll(totalhabbitlist_items);
        totalHabbitsListReady_items.removeAll(totalHabbitsListReady_items);
    }
    @Override
    protected void onResume() {

        super.onResume();
        listenerDoc();
        listenerDoc2();
    }


    //스와이프 기능 헬퍼 연결
    private void initSwipe() {
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
                    myStartActivity2(totalhabbitlist_items.get(position).getHabbittitle(), DetailHabbit.class);
                                            
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

                        Paint p = new Paint();
                        p.setColor(Color.parseColor("#B1624E"));
                        RectF background = new RectF((float) itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "상세 보기";
                        p2.setColor(Color.parseColor("#DAA03D"));
                        p2.setTextSize(20);
                        p2.setAntiAlias(true);
                        //텍스트 높이
                        Rect bounds = new Rect();
                        p2.getTextBounds(text, 0, text.length(), bounds);
                        int textheight = bounds.height();
                        //비트맵
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.historyicon);
                        c.drawText(text, background.centerX()-p2.measureText(text)/2, background.centerY() + (float)(bmp.getWidth()/2) + (float)textheight , p2);
                        c.drawBitmap(bmp, background.centerX() - (float)(bmp.getWidth()/2), background.centerY() - (float)(bmp.getHeight()/2), null);

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
                        p2.setTextSize(20);
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

    //스와이프 기능 헬퍼 연결
    private void initSwipe2() {
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
                                            .document(totalHabbitsListReady_items.get(position).getHabbittitle())
                                            .delete()

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e(TAG, "onSuccess: "  + totalHabbitsListReady_items.get(position).getHabbittitle());
                                                    totalHabbitsListReady_items.remove(position);
                                                    totalHabbitsListReady_adapter.notifyItemRemoved(position);
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
                    myStartActivity2(totalHabbitsListReady_items.get(position).getHabbittitle(), DetailHabbit.class);

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

                        Paint p = new Paint();
                        p.setColor(Color.parseColor("#B1624E"));
                        RectF background = new RectF((float) itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "상세 보기";
                        p2.setColor(Color.parseColor("#DAA03D"));
                        p2.setTextSize(20);
                        p2.setAntiAlias(true);
                        //텍스트 높이
                        Rect bounds = new Rect();
                        p2.getTextBounds(text, 0, text.length(), bounds);
                        int textheight = bounds.height();
                        //비트맵
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.historyicon);
                        c.drawText(text, background.centerX()-p2.measureText(text)/2, background.centerY() + (float)(bmp.getWidth()/2) + (float)textheight , p2);
                        c.drawBitmap(bmp, background.centerX() - (float)(bmp.getWidth()/2), background.centerY() - (float)(bmp.getHeight()/2), null);

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
                        p2.setTextSize(20);
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

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper2.attachToRecyclerView(recyclerView2);

        //ItemTouchHelper 생성
        helper2 = new ItemTouchHelper(new ItemTouchHelperCallback(totalHabbitsListReady_adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper2.attachToRecyclerView(recyclerView2);
    }

    private void listenerDoc(){

        //아이템을 리스트에 추가
        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                .document(firebaseUser.getUid())
                .collection("total")
                .whereEqualTo(getdayofweek(), true) // 복합 쿼리 순서 주의! -- 색인에 가서 복합 색인 추가
//                .orderBy("doing", Query.Direction.DESCENDING)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                totalhabbitlist_items.add(0, document.toObject(TotalHabbitsList_Item.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        totalHabbitsList_adapter.notifyDataSetChanged();
                    }
                });

        //특정 필드 값 가져오기
        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            long todaytarget = (long) Objects.requireNonNull(document.getData()).get("todaytarget");
                            long hour = ((long) todaytarget / 3600) % 24;
                            long minute = ((long) todaytarget / 60) % 60;
                            long second = (long) todaytarget % 60;

                            double percentage = todaytarget/(double)86400 * 100.0;
                            tvpercentage.setText(String.format("%.2f", percentage) + "%");
                            tvtodaytarget.setText(String.format("%02d:%02d:%02d", hour, minute, second)+"");

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void listenerDoc2() {
        //아이템을 리스트에 추가
        firebaseFirestore
                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                .document(firebaseUser.getUid())
                .collection("total")
                .whereEqualTo(getdayofweek(), false) // 복합 쿼리 순서 주의! -- 색인에 가서 복합 색인 추가
//                .orderBy("doing", Query.Direction.DESCENDING)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => 확인 " + document.getData());
                                totalHabbitsListReady_items.add(document.toObject(TotalHabbitsListReady_Item.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        totalHabbitsListReady_adapter.notifyDataSetChanged();
                    }
                });
    }

    private String getdayofweek() {
        calendar = Calendar.getInstance();
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        String day = "";

        switch (dayNum) {
            case 1:
                day = "sunday";
                break;
            case 2:
                day = "monday";
                break;
            case 3:
                day = "tuesday";
                break;
            case 4:
                day = "wednesday";
                break;
            case 5:
                day = "thursday";
                break;
            case 6:
                day = "friday";
                break;
            case 7:
                day = "saturday";
                break;
        }
        return day;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}