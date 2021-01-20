package com.koko_plan.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.server.detailhabbit.DetailHabbit;
import com.koko_plan.server.goodtext.GoodText_Adapter;
import com.koko_plan.server.goodtext.GoodText_Item;
import com.koko_plan.server.goodtext.GoodText_ViewListener;
import com.koko_plan.server.goodtext.RandomGoodText;
import com.koko_plan.server.habbitlist.HabbitList_ViewListener;
import com.koko_plan.server.ranking.Ranking_list;
import com.koko_plan.server.totalhabbits.TotalHabbitsList_list;
import com.koko_plan.sub.ItemTouchHelperCallback;
import com.koko_plan.R;
import com.koko_plan.member.MemberActivity;
import com.koko_plan.member.MemberEditActivity;
import com.koko_plan.member.Profile_Item;
import com.koko_plan.member.Singup;
import com.koko_plan.sub.MySoundPlayer;
import com.koko_plan.sub.RequestReview;
import com.koko_plan.sub.SaveProgressReceiver;
import com.koko_plan.sub.Utils;
import com.koko_plan.sub.subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.koko_plan.main.RecyclerAdapter.timerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TodoList_ViewListener, GoodText_ViewListener, HabbitList_ViewListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_APP_UPDATE = 600;

    //파이어베이스 사용
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore firebaseFirestore;
    public static FirebaseUser firebaseUser;

    @SuppressLint("StaticFieldLeak")
    public static Button btnsavelist;

    public static String name, email;
    private String inputname;
    public static String photourl;
    public static Bitmap profile;

    private RecyclerView recyclerView, recyclerView3;

    private ScrollView scrollview;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    private View btnPlus;

    public static int lastsec, timegap, totalprogress;

    private TextView tvTodayProgress;
    private TextView nav_header_name_text;

    private SimpleDateFormat timeformat, dayformat;

    private String time, day;

    private int total;

    public static String todaydate;
    private SimpleDateFormat dateformat;

    ItemTouchHelper helper, helper2;
    public static String selecteddata;
    private Date date, today, selected;
    private AppUpdateManager appUpdateManager;
    private ImageView nav_header_photo_image, ivTrophy;
    private Calendar calendar;
    private HorizontalCalendar horizontalCalendar;
    private PieChart pieChart;

    private int today_progress;
    public static int todayitemsize;

    public static ArrayList<TodoList_Item> todoListItems = null;
    private RecyclerAdapter adapter;
    public static ArrayList<GoodText_Item> goodText_items = null;
    private GoodText_Adapter goodText_adapter = null;

    private TextView goodtextsize;

    private List<EventDay> events;
    private CalendarView calendarView;

    private LineDataSet dataset;

    private float entries0;
    private FirebaseCrashlytics crashlytics;
    private int todaysgoodtextsize;
    private int showcount = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_main);

        Utils.setStatusBarColor(this, Utils.StatusBarColorType.GREEN_STATUS_BAR);

        // 회원정보 확인 후, 상세 정보 없으면 정보 기입으로 이동
        initprofile();

        //어플 업데이트 매니저
        Appupdatemanager();

        // 저장 위치 초기화
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        //파이어베이스 초기화
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.log("my message");
        crashlytics.log("E/TAG: my message");
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        // 파이어베이스 회원 정보 모으기
        getprofile();

        //객체 초기화
        InitializeView();

        // 현재 날짜 구하기
        gettodaydate();

        initSwipe2();

//        HabbitTodayListmaker();
//        HabbitListmaker();

        listenerhabbitlistDoc2();

        // 할일 목록 만들기(리사이클러뷰)
        HabbitTodayListInit();

        // 가로 달력 구현
        horizontalCalendarmaker(calendar);

        //하단 프로세스 달력추가
        EventCalendarMaker(calendar);

        GoodTextListmaker();

        if(showcount >= 10) RequestReview.show(this);

        saveProgressAlarm(this);
    }

    private void listenerhabbitlistDoc2() {

        if(firebaseUser!=null){

            firebaseFirestore
                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                    .document(firebaseUser.getUid())
                    .collection("total")
                    .orderBy("num", Query.Direction.ASCENDING)

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
//                                        todoListItems.add(0, dc.getDocument().toObject(TodoList_Item.class));
                                        break;
                                    case MODIFIED:
                                        Log.w("MODIFIED","Data: " + dc.getDocument().getData());
//                                    rankingAdapter.notifyDataSetChanged();
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case REMOVED:
                                        Log.w("REMOVED", "Data: " + dc.getDocument().getData());
                                        break;
                                }
                            }
                        }
                    });
        }
    }

    @SuppressLint("Range")
    private void LineChartMaker() {

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();

        for(int i=0 ; i < 10; i++){
            entries.add(new Entry(pref.getFloat("entries"+i, 0), i));
        }

        dataset = new LineDataSet(entries, "하루 중, 습관 비중 추이");

        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0 ; i < 10; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i-9);
            labels.add(dateformat.format(cal.getTime()));
        }

        LineData data = new LineData(labels, dataset);
        dataset.setColors(Collections.singletonList(Color.parseColor("#A4193D"))); //
        /*dataset.setDrawCubic(true); //선 둥글게 만들기*/
        dataset.setDrawFilled(true); //그래프 밑부분 색칠
        dataset.setFillColor(Color.parseColor("#FFDFB9")); //아래 채워질 색상
        dataset.setCircleColor(Color.parseColor("#A4193D")); //포인트 색상

        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);//하단 왼쪽에 설정
//        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // 레전드 컬러 설정

        lineChart.setData(data);
        lineChart.setDescription("");
//        lineChart.animateY(3000);
    }

    @SuppressLint("SimpleDateFormat")
    private void gettodaydate() {
        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        //날짜 표시 형식 지정
        dateformat = new SimpleDateFormat("yyyy-MM-dd");
        todaydate = dateformat.format(date);
        selecteddata = todaydate;
    }

    private String getdayofweek() {
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

    private void HabbitTodayListInit() {

        todoListItems = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) findViewById(R.id.rv_view);

        adapter = new RecyclerAdapter(todoListItems, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        initSwipe();
    }

    private void GoodTextListmaker() {

        goodText_items = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        goodText_adapter = new GoodText_Adapter(goodText_items, this, this);
        recyclerView3.setLayoutManager(layoutManager);
        recyclerView3.setAdapter(goodText_adapter);

        //ItemTouchHelper 생성
        helper2 = new ItemTouchHelper(new ItemTouchHelperCallback(goodText_adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper2.attachToRecyclerView(recyclerView3);
    }

    private void horizontalCalendarmaker(Calendar calendar) {
        //가로 달력 추가
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 12);

        //가로 달력 구현
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .defaultSelectedDate(calendar)
                .datesNumberOnScreen(7)
                .build();
//todo
        //가로 달력 구동시 리스너
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Calendar date, int position) {
                selecteddata = dateformat.format(date.getTime());
                try {
                    today = dateformat.parse(todaydate);
                    selected= dateformat.parse(selecteddata);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int compare = today.compareTo(selected);

                if(!selecteddata.equals(todaydate) && compare > 0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    setdidlist();
                                    tvTodayProgress.setText("실행한 습관이 없슴");

                                    //달력추가
                                    EventCalendarMaker(date);
                                    LineChartMaker();
                                }
                                private void setdidlist() {
                                    todoListItems.clear();
                                    if (firebaseUser != null) {
                                        firebaseFirestore
                                                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                                                .document(firebaseUser.getUid())
                                                .collection("dates")
                                                .document(selecteddata)
                                                .collection("habbits")
                                                .orderBy("curtime", Query.Direction.DESCENDING)

                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @SuppressLint("SetTextI18n")
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        long cur = 0;
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                                todoListItems.add(document.toObject(TodoList_Item.class));
                                                                long total = (long) document.getData().get("totalsec");
                                                                cur += (long) ((long) document.getData().get("curtime") / (double) total * 100.0);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                        if(todoListItems.size()>0) today_progress = (int) (cur/todoListItems.size());
                                                        tvTodayProgress.setText("오늘의 실행율 : " + today_progress+ "%" );

                                                        piechartmaker();
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }).start();

                } else {
                    if(selecteddata.equals(todaydate)) {
                        listenerhabbitlistDoc();
                    }
                    if(compare < 0)startToast("해당일에는 실행한 습관이 없습니다.");
                }
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                Log.e(TAG, "onCalendarScroll: "+ "스크롤");
                timegap = 0;
                /*if(timerTask != null)
                {
                    timerTask.cancel();
                    timerTask = null;
                }*/
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logoutButton) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            FirebaseAuth.getInstance().signOut();
            myStartActivity(Singup.class);

        } else if (id == R.id.profile) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(MemberEditActivity.class);

        } else if (id == R.id.recommend) {
            MySoundPlayer.play(MySoundPlayer.CLICK);

            Intent msg = new Intent(Intent.ACTION_SEND);
            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=KOKO_HABBIT");
            msg.putExtra(Intent.EXTRA_TITLE, "코코 습관 추천하기");
            msg.setType("text/plain");
            startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"));

        } else if (id == R.id.totallisticon) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(TotalHabbitsList_list.class);

        } else if (id == R.id.primium) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(subscribe.class);

        }/*else if (id == R.id.nav_history) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(History_list.class);

        } else if (id == R.id.nav_clublist) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(Club_list.class);

        } else if (id == R.id.nav_help) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(Help.class);*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initprofile() {
        new Thread(() -> {
            if (firebaseUser == null) {
                myStartActivity(Singup.class);
            } else {
                DocumentReference documentReference = firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists() && Objects.requireNonNull(document.getData()).get("name")!=null) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Profile_Item profileItem = document.toObject(Profile_Item.class);
                                    assert profileItem != null;
                                    inputname = profileItem.getName();
                                    nav_header_name_text.setText(inputname+"");
                                } else {
                                    Log.d(TAG, "No such document");
                                    myStartActivity2(MemberActivity.class);
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        }).start();
    }

    //화면 객체 초기화
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void InitializeView() {

        scrollview = findViewById(R.id.scrollview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);
        nav_header_name_text = nav_header_view.findViewById(R.id.nhtv_name);
        nav_header_photo_image = nav_header_view.findViewById(R.id.nhtv_image);
        TextView nav_header_mail_text = nav_header_view.findViewById(R.id.nhtv_mail);
        nav_header_name_text.setText(name + " ");
        nav_header_mail_text.setText(email + " ");
        probitmap();

        ivTrophy = findViewById(R.id.iv_trophy);

        pieChart = findViewById(R.id.piechart);

        btnPlus = findViewById(R.id.btnPlus);
        btnsavelist = findViewById(R.id.btn_savelist);
        tvTodayProgress = findViewById(R.id.tv_todayprogress);

        //배너 광고 표기
        AdView adBanner = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adBanner.loadAd(adRequest);

        AdView adBanner2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adBanner2.loadAd(adRequest2);

//        recyclerView.setVisibility(View.GONE);
//        recyclerView2 = findViewById(R.id.rv_habbitview2);
//        recyclerView2.setVisibility(View.GONE);
        recyclerView3 = findViewById(R.id.rv_msg);

        goodtextsize = findViewById(R.id.tv_goodtextsize);
        calendarView = findViewById(R.id.calendarView2);

        //효과음 초기화
        MySoundPlayer.initSounds(getApplicationContext());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void probitmap() {
        Profilebitmap thread = new Profilebitmap();
        thread.start();
        try{
            thread.join();
            nav_header_photo_image.setImageBitmap(profile);
            nav_header_photo_image.setBackground(new ShapeDrawable(new OvalShape()));
            nav_header_photo_image.setClipToOutline(true);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    class Profilebitmap extends Thread {
        public void run() {

            try{
                URL url = new URL(photourl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                profile = BitmapFactory.decodeStream(is);

            } catch (MalformedURLException ee) {
                ee.printStackTrace();
                nav_header_photo_image.setVisibility(View.INVISIBLE);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onModify() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    //어플 업데이트 관리
    private void Appupdatemanager() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            // Checks that the platform will allow the specified type of update.
            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE))
            {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            IMMEDIATE,
                            this,
                            REQUEST_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //하단 달력 설정
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void EventCalendarMaker(Calendar calendar){

        // https://github.com/Applandeo/Material-Calendar-View

        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        CalendarEvents(calendar);
//or
        /*events.add(new EventDay(calendar2, new Drawable()));
//or if you want to specify event label color
        events.add(new EventDay(calendar2, R.drawable.sample_icon, Color.parseColor("#228B22")));*/


        //달력클릭
        calendarView.setOnDayClickListener(new OnDayClickListener() {

            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                horizontalCalendar.selectDate(clickedDayCalendar, true);
                selecteddata = dateformat.format(clickedDayCalendar.getTime());
                scrollview.scrollTo(0,0);
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChange() {
                showcalendardayprogress();
            }
        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                showcalendardayprogress();
            }
        });
    }

    private void showcalendardayprogress() {
        events = new ArrayList<>();
        int year = calendarView.getCurrentPageDate().get(Calendar.YEAR);
        int month = calendarView.getCurrentPageDate().get(Calendar.MONTH)+1;
        // 해당 달력 마지막 날짜
        int lastday = calendarView.getCurrentPageDate().getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //변수를 동적으로 만드는 방법
        Map<String, Calendar> map = new HashMap<String, Calendar>();
        for (int i = 1; i <= lastday; i++) {
            map.put("calendar" + i, Calendar.getInstance());
            @SuppressLint("DefaultLocale") String str = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", i);
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            Objects.requireNonNull(map.get("calendar" + i)).setTime(date);

            if (firebaseUser != null) {

                DocumentReference documentReference = firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid());

                int finalI = i;
                documentReference
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        if (document.exists() && document.get(str) != null) {
                                            Log.d(TAG, "DocumentSnapshot data: " + map.get("calendar" + finalI));
                                            events.add(new EventDay(map.get("calendar" + finalI), new Drawable() {
                                                @SuppressLint("CanvasSize")
                                                @Override
                                                public void draw(@NonNull Canvas canvas) {
                                                    //캔버스 바탕 설정
                                                    Paint pnt = new Paint();
                                                    //가로로 설정
                                                    pnt.setAntiAlias(true);
                                                    pnt.setColor(Color.LTGRAY);
                                                    String st = document.get(str) + "%";
                                                    canvas.drawRect(0, 0, canvas.getWidth() * Integer.parseInt(String.valueOf(document.get(str))) / 100, 53, pnt);

                                                    Paint pnt2 = new Paint();
                                                    pnt2.setAntiAlias(true);
                                                    pnt2.setColor(Color.RED);
                                                    pnt2.setTextSize(30);
                                                    canvas.drawText(st, 0, canvas.getHeight() - 10, pnt2);
                                                }

                                                @Override
                                                public void setAlpha(int i) {
                                                }

                                                @Override
                                                public void setColorFilter(@Nullable ColorFilter colorFilter) {
                                                }

                                                /**
                                                 * @deprecated
                                                 */
                                                @SuppressLint("WrongConstant")
                                                @Override
                                                public int getOpacity() {
                                                    return 0;
                                                }
                                            }));
                                            calendarView.setEvents(events);
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
        }
    }
    private void CalendarEvents(Calendar calendar) {
        showcalendardayprogress();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        timegap =0;

        /*roomdb.todoDao().getAll(todaydate).observe(this, new Observer<List<Todo>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Todo> data) {
                adapter.setItem(data);
            }
        });*/

        btnPlus.setOnClickListener(v -> {
            myStartActivity(EditHabbit.class);
        });

        ivTrophy.setOnClickListener(v -> myStartActivity2(Ranking_list.class));

        btnsavelist.setVisibility(View.INVISIBLE);
        btnsavelist.setOnClickListener(v -> {
            timegap =0;
            resetId();
            btnsavelist.setVisibility(View.INVISIBLE);
        });

        listenerDoc();
    }

    private void listenerhabbitlistDoc() {
        new Thread(() -> {
            todoListItems.clear();
            if (firebaseUser != null) {
                firebaseFirestore
                        .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                        .document(firebaseUser.getUid())
                        .collection("total")
                        .whereEqualTo(getdayofweek(), true) // 복합 쿼리 순서 주의! -- 색인에 가서 복합 색인 추가
                        .orderBy("num", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                long cur = 0;
                                long today = 0;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        todoListItems.add(document.toObject(TodoList_Item.class));
                                        long total = (long) document.getData().get("totalsec");
                                        cur += (long) ((long) document.getData().get("curtime") / (double) total * 100.0);
                                        adapter.notifyDataSetChanged();
                                        today += total;
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                if(todoListItems.size()>0) today_progress = (int) (cur/todoListItems.size());
                                tvTodayProgress.setText("오늘의 실행율 : " + today_progress+ "%" );

                                Map<String, Object> dairyInfo = new HashMap<>();
                                dairyInfo.put(todaydate, today_progress);
                                dairyInfo.put("todaytarget", today);

                                firebaseFirestore
                                        .collection("users")
                                        .document(firebaseUser.getUid())

                                        .set(dairyInfo, SetOptions.merge())
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
                                piechartmaker();
                            }
                        });
            }
        }).start();

        LineChartMaker();
    }

    private void listenerDoc(){

        if(firebaseUser != null) {
            firebaseFirestore
                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                    .document(firebaseUser.getUid())
                    .collection("messages")
                    .orderBy("time", Query.Direction.DESCENDING) // 파이어베이스 쿼리 정렬
//                    .whereEqualTo("day", selecteddata) // 파이어베이스 쿼리 해당 필드 필터

                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("listen:error", e);
                                return;
                            }

                            assert snapshots != null;
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {

                                switch (dc.getType()) {
                                    case ADDED:
                                        Log.w("ADDED", "Data: " + dc.getDocument().getData());
                                        goodText_items.add(goodText_items.size(), dc.getDocument().toObject(GoodText_Item.class));
                                        goodtextsize.setText(goodText_items.size() + "개");
                                        if(Objects.equals(dc.getDocument().getData().get("day"), todaydate)) {
                                            todaysgoodtextsize ++;
                                            Log.e(TAG, "onEvent: " + "오늘 받은 명언" + todaysgoodtextsize);
                                        }
//                                    rankingAdapter.notifyDataSetChanged();
                                        break;
                                    case MODIFIED:
                                        Log.w("MODIFIED", "Data: " + dc.getDocument().getData());
//                                    rankingAdapter.notifyDataSetChanged();
                                        break;
                                    case REMOVED:
                                        Log.w("REMOVED", "Data: " + dc.getDocument().getData());
//                                    clubAdapter.notifyDataSetChanged();
                                    goodtextsize.setText(goodText_items.size()-1 + "개");
                                        break;
                                }
                            }
                            goodText_adapter.notifyDataSetChanged();

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //습관 입력창 복귀 : EditHabbit에서 돌아와서 처리
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                startToast("새 습관이 추가되었습니다.");
            }
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    private void myStartActivity2(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void resetId() {

        if(todoListItems.size() > 0) {
            new Thread(() -> {
                for(int i=0; i < adapter.getItems().size() ; i++){
                    if(todoListItems.get(i).getIsrunning()) {
                        todoListItems.get(i).setCurtime(lastsec-1);
                        Map<String, Object> data = new HashMap<>();
                        data.put("curtime", lastsec-1);
                        firebaseFirestore
                                .collection("users").document(firebaseUser.getUid()).collection("total").document(todoListItems.get(i).getHabbittitle())
                                .set(data, SetOptions.merge());
                    }
                    Map<String, Object> data = new HashMap<>();
                    adapter.getItems().get(i).setNum((i+1));
                    data.put("num", (i+1));

                    firebaseFirestore
                            .collection("users").document(firebaseUser.getUid()).collection("total").document(todoListItems.get(i).getHabbittitle())
                            .set(data, SetOptions.merge());

                }
            }).start();
        }
    }

    //원형 차트 만들기
    @SuppressLint("DefaultLocale")
    private void piechartmaker() {

        pieChart.setData(null);
        pieChart.clear();

        if(adapter.getItemCount() > 0) {

            ArrayList NoOfTotalsec = new ArrayList();
            total = 0;
            for (int i = 0; i < todoListItems.size(); i++) {
                if(adapter.getItems().get(i).getTotalsec()!=0){
                    NoOfTotalsec.add(new Entry(adapter.getItems().get(i).getTotalsec(), 0));
                }
                total += todoListItems.get(i).getTotalsec();
            }
            double percentage = total/(24*3600.0)*100;

            //하루 24 남은 시간
            NoOfTotalsec.add(new Entry(24 * 3600 - total, 0));
            PieDataSet dataSet = new PieDataSet(NoOfTotalsec, "");

            //차트 항목 타이틀 지정
            ArrayList title = new ArrayList();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if(adapter.getItems().get(i).getHabbittitle()!=null) {
                    title.add(adapter.getItems().get(i).getHabbittitle());
                } else {
                    startToast("해당일에는 실행한 습관이 없습니다.");
                }
            }
            title.add("하루");

            Log.e(TAG, "piechartmaker: "+ title +  dataSet);

            if(NoOfTotalsec.size() == title.size()) {
                PieData data = new PieData(title, dataSet); // MPAndroidChart v3.X 오류 발생
                pieChart.setData(data);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//            pieChart.animateXY(2000, 2000);
                pieChart.setDescription("");
                pieChart.setAlpha(0.8f);
                pieChart.setCenterText("습관\n비중\n" + String.format("%.2f", percentage) + "%");
            }

            savefieldtofirebase(percentage);
        }
    }

    private void savefieldtofirebase(double percentage) {
        if(name!=null){
            //파이어베이스 저장
            new Thread(() -> {
                if(firebaseUser != null) {
                    Map<String, Object> percentInfo = new HashMap<>();
                    percentInfo.put("percentage", percentage);

                    firebaseFirestore
                            .collection("users")
                            .document(firebaseUser.getUid())
                            .collection("dates")
                            .document(todaydate)

                            .set(percentInfo)
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
            }).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timegap = 0;
        showcount++;

        //해당 항목(오늘 날짜에 해당되는 아이템 리스트)을 어뎁터에 부어버림
//        adapter.setItem(roomdb.todoDao().search(todaydate));
        todayitemsize = todoListItems.size();

        editor.putInt("showcount", showcount);
        editor.putLong("stoptime", 0);
        editor.putInt("todayitemsize", todayitemsize);
        editor.apply();

        //오늘 날짜의 플레이중인 항목의 진행 상황과 스톱 시간을 저장
        new Thread(() -> {
            for(int i=0 ; i < todayitemsize ; i++) {
                if(todoListItems.get(i).getIsrunning()) {
                    todoListItems.get(i).setCurtime(lastsec-1);
                    Map<String, Object> data = new HashMap<>();
                    data.put("curtime", lastsec-1);
                    firebaseFirestore
                            .collection("users")
                            .document(firebaseUser.getUid())
                            .collection("total")
                            .document(todoListItems.get(i).getHabbittitle())
                            .set(data, SetOptions.merge());

                    //현재의 밀리세컨 구함
                    long now = System.currentTimeMillis();
                    editor.putLong("stoptime", now);
                    editor.apply();
                }
            }
            goodText_items.removeAll(goodText_items);
            todoListItems.removeAll(todoListItems);

        }).start();

        savehabbitportion();
    }

    private void savehabbitportion() {

        new Thread(() -> {
        for(int i=0 ; i < 10; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i - 9);

            if (firebaseUser != null) {
                DocumentReference documentReference = firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid())
                        .collection("dates")
                        .document(dateformat.format(cal.getTime()));

                int finalI = i;
                documentReference
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        if (document.exists()) {
                                            Log.e(TAG, "LineChartMaker: document" + document.get("percentage"));
                                            double percent = (double) document.get("percentage");
                                            editor.putFloat("entries" + finalI, (float) percent);
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
            }
        }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //현재의 밀리세컨 구함
        long now = System.currentTimeMillis();
        editor.putLong("stoptime", now);
        editor.apply();

//플레이 중이면 스레드 중지
        if(timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onResume() {
        super.onResume();

        //업데이트 가능 시, 연결해서 업데이트
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            IMMEDIATE,
                            this,
                            REQUEST_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        todaysgoodtextsize = 0;

        //가로 달력 시작시, 선택날짜와 이동
        horizontalCalendar.selectDate(calendar, true);

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.

        todayitemsize = pref.getInt("todayitemsize", 0);
        long stoptime = pref.getLong("stoptime", 0);
        showcount = pref.getInt("showcount", 0);

        timegap = (int)((now-stoptime)/1000);
        Log.e(TAG, "onResume: timegap "  + timegap );
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
                                            .document(todoListItems.get(position).getHabbittitle())
                                            .delete()

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e(TAG, "onSuccess: "  + todoListItems.get(position).getHabbittitle());
                                                    todoListItems.remove(position);
                                                    adapter.notifyItemRemoved(position);
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
                }else {
                    //오른쪽으로 밀었을때.
                    myStartActivity3(todoListItems.get(position).getHabbittitle(), DetailHabbit.class);
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
                        Paint p = new Paint();
                        p.setColor(Color.parseColor("#B1624E"));
                        RectF background = new RectF((float) itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "상세 보기";
                        p2.setColor(Color.parseColor("#DAA03D"));
                        p2.setTextSize(30);
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
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
    }

    private void myStartActivity3(String detailhabbit, Class c) {

        Intent intent = new Intent(this, c);
        intent.putExtra("detailhabbit", detailhabbit);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
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
                            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    firebaseFirestore
                                            .collection("users")
                                            .document(firebaseUser.getUid())
                                            .collection("messages")
                                            .document(goodText_items.get(position).getTime())
                                            .delete()

                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    goodText_items.remove(position);
                                                    goodText_adapter.notifyItemRemoved(position);
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
                    if(!goodText_items.get(position).getFromid().equals(firebaseUser.getUid())){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                                runOnUiThread(new Runnable() {
                                    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
                                    @Override
                                    public void run() {

                                        // 현재 날짜 구하기
                                        date = new Date();
                                        //날짜 표시 형식 지정
                                        timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        dayformat = new SimpleDateFormat("yyyy-MM-dd");
                                        time = timeformat.format(date);
                                        day = dayformat.format(date);

                                        RandomGoodText.make(getApplicationContext(), goodText_items.get(position).getFromid(), day, time);
                                        goodText_adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();
                    } else {
                        startToast("본인에게는 선물이 불가합니다.");
                        goodText_adapter.notifyDataSetChanged();
                    }
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
                        //배경
                        Paint p = new Paint();
                        p.setColor(Color.parseColor("#B1624E"));
                        RectF background = new RectF((float) itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "명언 답장";
                        p2.setColor(Color.parseColor("#5CC8D7"));
                        p2.setTextSize(30);
                        p2.setAntiAlias(true);
                        //텍스트 높이
                        Rect bounds = new Rect();
                        p2.getTextBounds(text, 0, text.length(), bounds);
                        int textheight = bounds.height();
                        //비트맵
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.shareicon);
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
        itemTouchHelper.attachToRecyclerView(recyclerView3);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private void getprofile() {
        if (firebaseUser != null) {
            for (UserInfo profile : firebaseUser.getProviderData()) {
                name = profile.getDisplayName();
                email = profile.getEmail();
                Uri photo = profile.getPhotoUrl();
                photourl = String.valueOf(photo);
            }
        }
    }

    //자정마다 실행 (리시버)
    void saveProgressAlarm(Context context){
        //알람 매니저 생성
        AlarmManager saveProgressAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //리시브 받을 클래스 연결
        Intent saveProgressIntent = new Intent(context, SaveProgressReceiver.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, saveProgressIntent, 0);

        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());

        resetCal.set(Calendar.HOUR_OF_DAY, 0);
        resetCal.set(Calendar.MINUTE, -1);
        resetCal.set(Calendar.SECOND, 0);

//        long reserve = resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY ;

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        saveProgressAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY
                , AlarmManager.INTERVAL_DAY, resetSender);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM-dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));

        Log.e(TAG, "saveProgressAlarm: " + setResetTime);
    }
}