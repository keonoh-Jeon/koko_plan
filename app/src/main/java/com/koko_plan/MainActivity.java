package com.koko_plan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.koko_plan.RecyclerAdapter.items;
import static com.koko_plan.RecyclerAdapter.timerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    private TodoDatabase db;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    private View btnPlus;
    @SuppressLint("StaticFieldLeak")
    public static Button btnsavelist;

    public static int lastsec, timegap, totalprogress;

    private TextView tvTodayProgress;

    int cur, total;

    private String day;
    private SimpleDateFormat dateformat;

    ItemTouchHelper helper;
    private String selecteddata;
    private Calendar today;

    @SuppressLint({"CommitPrefEdits", "SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateformat = new SimpleDateFormat("yyyy-MM/dd");

        //가로 달력 추가


        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        day = dateformat.format(date);

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Calendar date, int position) {
                Log.e(TAG, "onDateSelected: " + dateformat.format(date.getTime()));
                selecteddata = dateformat.format(date.getTime());
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        btnPlus = findViewById(R.id.btnPlus);
        btnsavelist = findViewById(R.id.btn_savelist);

        db = TodoDatabase.getDatabase(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(db);
        recyclerView.setAdapter(adapter);

        tvTodayProgress = findViewById(R.id.tv_todayprogress);

        initSwipe();

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        timegap =0;

        btnPlus.setOnClickListener(v -> {
            myStartActivity(EditHabbit.class);
        });

        btnsavelist.setVisibility(View.INVISIBLE);
        btnsavelist.setOnClickListener(v -> {
            timegap =0;
            resetId();
            btnsavelist.setVisibility(View.INVISIBLE);
        });

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.todoDao().getAll().observe(this, new Observer<List<Todo>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Todo> data) {
                adapter.setItem(data);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //습관 입력창 복귀 : EditHabbit에서 돌아와서 처리
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String habbittitle = data.getStringExtra("habbittitle");
                int count = data.getIntExtra("count", 0);
                int hour = data.getIntExtra("hour", 0);
                int min = data.getIntExtra("min", 0);
                int sec = data.getIntExtra("sec", 0);
                boolean isrunning = data.getBooleanExtra("isrunning", false);

                @SuppressLint("DefaultLocale")
                int totalsec = hour*60*60+min*60+sec;

                new Thread(() -> {
                    int size = items.size()+1;
                    Todo todo = new Todo(size+1, day, habbittitle, 0,0, count, hour, min, sec, totalsec, isrunning);
                    db.todoDao().insert(todo);
                }).start();
            }
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);

        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    private void resetId() {

        if(items.size() > 0) {
            new Thread(() -> {
                for(int i=0; i < adapter.getItems().size() ; i++){
                    Log.e(TAG, "resetId: get" + adapter.getItems().get(i).getNum());
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    adapter.getItems().get(i).setNum((i+1));
                    Log.e(TAG, "resetId: set" + adapter.getItems().get(i).getNum());
                    db.todoDao().update(adapter.getItems().get(i));
                }
            }).start();
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onResume() {
        super.onResume();

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        long stoptime = pref.getLong("stoptime", 0);
        int itemsize = pref.getInt("itemsize", 0);

        if(stoptime != 0){
            timegap = (int)((now-stoptime)/1000);
            Log.e(TAG, "onResume: run timegap 존재 " + timegap);
        } else {
            timegap = 0;
            Log.e(TAG, "onResume: run timegap 존재 없슴 " + timegap);
        }

        if(itemsize > 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cur = 0;
                            for(int i=0 ; i < adapter.getItemCount() ; i++) {
                                if(adapter.getItems().get(i).getCurtime()>0){
                                    int curtime = (int) ((double)adapter.getItems().get(i).getCurtime() / ((double)adapter.getItems().get(i).getTotalsec()) * 100.0);
                                    cur += curtime;
                                } else {
                                    int count = (int) ((double)adapter.getItems().get(i).getCurcount() / ((double)adapter.getItems().get(i).getCount()) * 100.0);
                                    cur += count;
                                }
                            }
                            tvTodayProgress.setText("오늘의 실행율 : " + cur/itemsize+ "%" );
                        }
                    });
                }
            }).start();
        }

        PieChart pieChart = findViewById(R.id.piechart);

        if(adapter.getItemCount() > 0){
            ArrayList NoOfTotalsec = new ArrayList();
            total = 0;
            for(int i=0 ; i < adapter.getItemCount() ; i++) {
                NoOfTotalsec.add(new Entry(adapter.getItems().get(i).getTotalsec(), 0));
                total += items.get(i).getTotalsec();
            }

            //하루 24 남은 시간
            NoOfTotalsec.add(new Entry(24*3600-total, 0));
            PieDataSet dataSet = new PieDataSet(NoOfTotalsec, "... Hour Of habbits");

            //차트 항목 타이틀 지정
            ArrayList title = new ArrayList();
            for(int i=0 ; i < adapter.getItemCount() ; i++) {
                title.add(adapter.getItems().get(i).getTitle());
            }
            title.add("하루");

            PieData data = new PieData(title, dataSet); // MPAndroidChart v3.X 오류 발생
            pieChart.setData(data);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieChart.animateXY(2000, 2000);
            pieChart.setCenterText("습관\n비중\n"+String.format("%.2f", total/(24*3600.0)*100)+"%");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        long now = System.currentTimeMillis();

        editor.putLong("stoptime", 0);
        editor.apply();

        new Thread(() -> {
            for(int i=0 ; i < adapter.getItemCount() ; i++) {
                if(items.get(i).getIsrunning()) {
                    items.get(i).setCurtime(lastsec-1);
                    Log.e(TAG, "onPause: run 현재 저장  " + items.get(i).getCurtime());
                    db.todoDao().update(adapter.getItems().get(i));
                    editor.putLong("stoptime", now);
                    editor.apply();
                }
            }
        }).start();

        editor.putInt("itemsize", adapter.getItemCount());
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(timerTask != null)
        {
            Log.e(TAG, "onPause: run");
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /* | ItemTouchHelper.RIGHT */) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            db.todoDao().delete(adapter.getItems().get(position));
                        }
                    }).start();
                }else {
                    //오른쪽으로 밀었을때.
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE ) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        //오른쪽으로 밀었을 때

                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        /*
                         * icon 추가할 수 있음.
                         */
                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_png); //vector 불가!
                        // RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        //c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}