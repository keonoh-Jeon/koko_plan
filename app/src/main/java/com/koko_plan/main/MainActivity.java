package com.koko_plan.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.github.mikephil.charting.components.Legend;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.firebase.FirebaseApp;
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
import com.koko_plan.sub.AlarmReceiver;
import com.koko_plan.sub.BackgroundSaveRank;
import com.koko_plan.sub.BackgroundSetzero;
import com.koko_plan.sub.DeviceBootReceiver;
import com.koko_plan.sub.GoodtextAlarmReceiver;
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
import java.security.MessageDigest;
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
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.koko_plan.main.RecyclerAdapter.timerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TodoList_ViewListener, GoodText_ViewListener, HabbitList_ViewListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_APP_UPDATE = 600;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    //파이어베이스 사용
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore firebaseFirestore;
    public static FirebaseUser firebaseUser;

    @SuppressLint("StaticFieldLeak")
    public static Button btnsavelist;
    @SuppressLint("StaticFieldLeak")
    public static View like, trophy;

    public static String name, email;
    public static InterstitialAd  mInterstitialAd;
    public static String inputname;
    public static String photourl;
    public static Bitmap profile;

    private RecyclerView recyclerView, recyclerView3;

    private ScrollView scrollview;
    private RelativeLayout relativeview;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    private View btnPlus;

    public static int lastsec, timegap, totalprogress;

    private TextView tvTodayProgress;
    private TextView nav_header_name_text, nav_header_mail_text;
    private TextView tvgetcount, tvtodayget;

    private SimpleDateFormat timeformat, dayformat;

    private String time, day;

    public static String todaydate;
    private SimpleDateFormat dateformat;

    public static Vibrator vibrator;

    ItemTouchHelper helper, helper2;
    public static String selecteddata;
    private Date date, today, selected;
    private AppUpdateManager appUpdateManager;
    private ImageView nav_header_photo_image, ivTrophy, ivgetcount, ivdownarrow, ivuparrow;
    private Calendar calendar;
    private HorizontalCalendar horizontalCalendar;
    private PieChart pieChart;

    private long today_progress;
    public static int todayitemsize;

    public static ArrayList<TodoList_Item> todoListItems = null;
    private RecyclerAdapter adapter;
    public static ArrayList<GoodText_Item> goodText_items = null;
    private GoodText_Adapter goodText_adapter = null;

    private TextView goodtextsize;

    private List<EventDay> events;
    private CalendarView calendarView;

    private ProgressDialog pd;

    private int ShowCount = 0;
    private TextView tvmyrankscore;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvrankereffect;
    @SuppressLint("StaticFieldLeak")
    public static TextView tveventeffect;
    private long Back_Key_Before_Time = 0;
    private LinearLayout veffectintro, veffectstandby, vhabbitportion;
    private View vblur1, vblur2, vblur3;

    private AdView adBanner, adBanner2, adBanner3, adBanner4;

    public static boolean subscribing = false;
    public static boolean fulladview;
    public static boolean adview1;
    public static boolean adview2;
    public static boolean adview3;
    public static boolean adview4;
    public static boolean adview5;
    public static boolean adview6;
    public static boolean adview7;
    public static boolean adview8;
    public static boolean adview9;
    public static boolean adview10;
    public static boolean blurview1;
    public static boolean blurview2;
    public static boolean blurview3;
    public static boolean blurview4;
    private TextView tvnewhabbits;
    private TextView tvplus1;
    public static int adloadcount;
    public static boolean setrankavailable = true;
    public static boolean setzeroavailable = true;
    private boolean goodTextReceiver;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_main);

        Utils.setStatusBarColor(this, Utils.StatusBarColorType.GREEN_STATUS_BAR);

        //객체 초기화
        InitializeView();

        //어플 업데이트 매니저
        Appupdatemanager();

        // 저장 위치 초기화
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        //파이어베이스 초기화
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.log("my message");
        crashlytics.log("E/TAG: my message");
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        initprofile();

        initSwipe2();
        // 가로 달력 구현
        horizontalCalendarmaker(calendar);

        listenerhabbitlistDoc2();

        // 할일 목록 만들기(리사이클러뷰)
        HabbitTodayListInit();
        getdocforgetlike();

        GoodTextListmaker();

        if(ShowCount >= 10) RequestReview.show(this);

        saveProgressAlarm(this);

        initWorkManagersetzero();

        //전면광고 로드
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6976973682401259/5609646607");

        //해시 키 확인
        getAppKeyHash();
    }

    private void initWorkManagersetzero() {

        if(setzeroavailable){
            long zero = getTimeUsingInWorkRequest(0, 0,0);
            WorkRequest setzeroWorkRequest = new OneTimeWorkRequest
                    .Builder(BackgroundSetzero.class)
                    .setInitialDelay(zero, TimeUnit.MILLISECONDS)
                    .addTag("notify_day_by_day")
                    .build();
            WorkManager.getInstance(this).enqueue(setzeroWorkRequest);
        }
        setzeroavailable = false;
    }

    @SuppressLint("SimpleDateFormat")
    private void initWorkManagersaverank() {

        if(setrankavailable){
            date = new Date();
            dateformat = new SimpleDateFormat("yyyy-MM-dd"/*, Locale.KOREA*/);
            todaydate = dateformat.format(date);
            long interval = getTimeUsingInWorkRequest(0,-2,0);

            Data myData = new Data.Builder()
                    .putString("name", inputname)
                    .putString("todaydate", todaydate)
                    .putLong("interval", interval)
                    .build();

            WorkRequest saverankWorkRequest = new OneTimeWorkRequest
                    .Builder(BackgroundSaveRank.class)
                    .setInputData(myData)
                    .setInitialDelay((interval), TimeUnit.MILLISECONDS)
                    .addTag("notify_saverank")
                    .build();

            WorkManager.getInstance(this).enqueue(saverankWorkRequest);
        }
    }

    private long getTimeUsingInWorkRequest(int i, int i1, int i2) {

        //현재 시간을 밀리세컨으로 받음
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());

        //현재 시간, 분, 초로 표기시 사용
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTimeInMillis(System.currentTimeMillis());
        dueDate.set(Calendar.HOUR_OF_DAY, i);
        dueDate.set(Calendar.MINUTE, i1);
        dueDate.set(Calendar.SECOND, i2);

        if(dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }

        return dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
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
                                        break;
                                    case MODIFIED:
                                        Log.w("MODIFIED","Data: " + dc.getDocument().getData());
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

    private void listenergoodtextlistDoc() {
        if(firebaseUser!=null){
            firebaseFirestore
                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                    .document(firebaseUser.getUid())
                    .collection("messages")

                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("listen:error", e);
                                return;                        }


                            assert snapshots != null;
                            String namefrom = "좋은사람";
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Log.w("ADDED","Data: " + dc.getDocument().getData());
                                        namefrom = (String) dc.getDocument().getData().get("namefrom");
                                        break;
                                    case MODIFIED:
                                        Log.w("MODIFIED","Data: " + dc.getDocument().getData());
                                        break;
                                    case REMOVED:
                                        Log.w("REMOVED", "Data: " + dc.getDocument().getData());
                                        break;
                                }
                            }
                            if(namefrom != null && goodTextReceiver) createNotification(namefrom);
                        }

                        private void createNotification(String namefrom) {
                            PackageManager pm = getApplicationContext().getPackageManager();
                            ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
                            //리시버 클래스 지정
                            Intent alarmIntent = new Intent(getApplicationContext(), GoodtextAlarmReceiver.class);
                            alarmIntent.putExtra("namefrom", namefrom);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
                            //알람 매니저
                            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                            // 사용자가 매일 알람을 허용했다면
                                if (alarmManager != null) {
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                }
                                // 부팅 후 실행되는 리시버 사용가능하게 설정
                                pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
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

        LineDataSet dataset = new LineDataSet(entries, "하루 중, 습관 비중 추이");
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0 ; i < 10; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i-9);
            labels.add(dateformat.format(cal.getTime()));
        }

        LineData data = new LineData(labels, dataset);
        dataset.setColors(Collections.singletonList(Color.parseColor("#A4193D"))); //
        dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠
        dataset.setFillColor(Color.parseColor("#FFDFB9")); //아래 채워질 색상
        dataset.setCircleColor(Color.parseColor("#A4193D")); //포인트 색상

        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);//하단 왼쪽에 설정

        lineChart.setData(data);
        lineChart.setDescription("");
    }

    @SuppressLint("SimpleDateFormat")
    private void gettodaydate() {

        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        //날짜 표시 형식 지정
        dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        todaydate = dateformat.format(date);
        selecteddata = todaydate;

        //가로 달력 시작시, 선택날짜와 이동
        horizontalCalendar.selectDate(calendar, true);

        Date yester = new Date();
        yester = new Date(yester.getTime()+(1000*60*60*24*-1));
        String yesterday = dateformat.format(yester);

        //날짜 바뀌면 실행
        if(!todaydate.equals(pref.getString("yesterday", yesterday))) {

                new Thread(() -> {
                    if(firebaseUser != null) {
                        initWorkManagersaverank();
                        firebaseFirestore
                                .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                                .document(firebaseUser.getUid())
                                .collection("total")

                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String habbit = (String) document.getData().get("habbittitle");

                                                Map<String, Object> curtime = new HashMap<>();
                                                curtime.put("curtime", 0);
                                                curtime.put("curcount", 0);

                                                assert habbit != null;
                                                firebaseFirestore
                                                        .collection("users")
                                                        .document(firebaseUser.getUid())
                                                        .collection("total")
                                                        .document(habbit)

                                                        .set(curtime, SetOptions.merge())
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
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        Map<String, Object> getcount = new HashMap<>();
                        getcount.put("getcount", 0);
                        getcount.put("progress", 0);

                        firebaseFirestore
                                .collection("users")
                                .document(firebaseUser.getUid())
                                .set(getcount, SetOptions.merge())
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

        tvnewhabbits = (TextView) findViewById(R.id.tv_newhabbits);
        vhabbitportion = (LinearLayout) findViewById(R.id.view_habbitportion);
        vhabbitportion.setVisibility(View.GONE);
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

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .defaultSelectedDate(calendar)
                .datesNumberOnScreen(7)
                .build();

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
                                    //달력추가
                                    EventCalendarMaker(date);
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
                                                                todoListItems.add(document.toObject(TodoList_Item.class));
                                                                long total = (long) document.getData().get("totalsec");
                                                                cur += (long) ((long) document.getData().get("curtime") / (double) total * 100.0);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                        if(todoListItems.size()>0) today_progress = (long) (cur/todoListItems.size());
                                                        tvTodayProgress.setText(today_progress+ "%" );

                                                        piechartmaker();

                                                        if(pd!= null) pd.dismiss();
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
//                timegap = 0;
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
            LoginManager.getInstance().logOut();
            myStartActivity(Singup.class);

        } else if (id == R.id.profile) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            myStartActivity(MemberEditActivity.class);

        } else if (id == R.id.recommend) {
            MySoundPlayer.play(MySoundPlayer.CLICK);
            Intent msg = new Intent(Intent.ACTION_SEND);
            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.koko_plan");
            msg.putExtra(Intent.EXTRA_TITLE, "코빗(습관 형성 어플) 추천하기");
            msg.setType("text/plain");
            startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"));

        } else if (id == R.id.totallisticon) {
            myStartActivity(TotalHabbitsList_list.class);

        } else if (id == R.id.primium) {
            myStartActivity(subscribe.class);

        } else if (id == R.id.ranker) {
        MySoundPlayer.play(MySoundPlayer.CLICK);
        myStartActivity(Ranking_list.class);
        }

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

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                                    probitmap();
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
        relativeview = findViewById(R.id.view_relativeview);

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
        nav_header_mail_text = nav_header_view.findViewById(R.id.nhtv_mail);

        ivgetcount = findViewById(R.id.iv_getcount);
        ivTrophy = findViewById(R.id.iv_trophy);
        ivdownarrow = findViewById(R.id.iv_downarrow);
        ivuparrow = findViewById(R.id.iv_uparrow);

        veffectintro = findViewById(R.id.v_effectintro);
        veffectintro.setVisibility(View.GONE);
        veffectstandby = findViewById(R.id.v_effectstanby);
        veffectstandby.setVisibility(View.GONE);

        vblur1= findViewById(R.id.v_blur1);
        vblur1.setVisibility(View.GONE);
        vblur2= findViewById(R.id.v_blur2);
        vblur2.setVisibility(View.GONE);
        vblur3= findViewById(R.id.v_blur3);
        vblur3.setVisibility(View.GONE);

        tvmyrankscore = findViewById(R.id.tv_myrankscore);
        tvrankereffect = findViewById(R.id.tv_rankereffect);
        tveventeffect = findViewById(R.id.tv_eventeffect);

        tvgetcount= findViewById(R.id.tv_getcount);
        tvtodayget= findViewById(R.id.tv_todayget);

        pieChart = findViewById(R.id.piechart);

        btnPlus = findViewById(R.id.btnPlus);
        btnsavelist = findViewById(R.id.btn_savelist);
        like = findViewById(R.id.view_like);
        trophy = findViewById(R.id.view_trophy);

        tvTodayProgress = findViewById(R.id.tv_todayprogress);

        //배너 광고 표기
        adBanner = findViewById(R.id.ad_View1);
        adBanner2 = findViewById(R.id.ad_View2);
        adBanner3 = findViewById(R.id.ad_View3);
        adBanner4 = findViewById(R.id.ad_View4);
        recyclerView3 = findViewById(R.id.rv_msg);

        tvplus1 = findViewById(R.id.tv_plus1);
        tvplus1.setVisibility(View.GONE);

        goodtextsize = findViewById(R.id.tv_goodtextsize);
        calendarView = findViewById(R.id.calendarView2);

        //효과음 초기화
        MySoundPlayer.initSounds(getApplicationContext());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        TextView tvletscheer = findViewById(R.id.tv_letscheer);
        tvletscheer.setSelected(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void probitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void run() {
                            nav_header_photo_image.setImageBitmap(profile);
                            nav_header_photo_image.setBackground(new ShapeDrawable(new OvalShape()));
                            nav_header_photo_image.setClipToOutline(true);
                            nav_header_name_text.setText(inputname + " ");
                            nav_header_mail_text.setText(email + " ");
                            initWorkManagersaverank();
                            setrankavailable = false;
                    }
                });
            }
        }).start();

        new Thread(() -> {
            Profilebitmap thread = new Profilebitmap();
            thread.start();

        }).start();
    }

    class Profilebitmap extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        System.gc();
        super.onDestroy();
    }

    //어플 업데이트 관리
    private void Appupdatemanager() {
        new Thread(() -> {
            //어플 업데이트 관리
            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(
                    appUpdateInfo -> {
                        // Checks that the platform will allow the specified type of update.
                        if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                        {
                            // Request the update.
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        this,
                                        REQUEST_APP_UPDATE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }).start();
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
        new Thread(() -> {
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
                                                        pnt2.setTextSize(canvas.getWidth()*1/3);
                                                        canvas.drawText(st, canvas.getWidth()*1/6, canvas.getHeight() - 10, pnt2);
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
        }).start();
    }
    private void CalendarEvents(Calendar calendar) {
        showcalendardayprogress();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        btnPlus.setOnClickListener(v -> {
            myStartActivity(EditHabbit.class);
        });

        ivTrophy.setOnClickListener(v -> myStartActivity2(Ranking_list.class));
        ivgetcount.setOnClickListener(v -> {
            MySoundPlayer.play(MySoundPlayer.POP);
            scrollview.smoothScrollTo(0, relativeview.getTop());
            showfullad(this);
        });

        ivdownarrow.setOnClickListener(v ->  {
            veffectintro.setVisibility(View.VISIBLE);
            ivdownarrow.setVisibility(View.INVISIBLE);
        });

        ivuparrow.setOnClickListener(v -> {
            veffectintro.setVisibility(View.GONE);
            ivdownarrow.setVisibility(View.VISIBLE);
        });

        btnsavelist.setVisibility(View.GONE);

        btnsavelist.setOnClickListener(v -> {
            timegap = 0;
            resetId();
            btnsavelist.setVisibility(View.GONE);
            like.setVisibility(View.VISIBLE);
            trophy.setVisibility(View.VISIBLE);
        });

        listenerDoc();
    }

    public static void showfullad(Context context){

        if (fulladview && pref.getInt("adloadcount", 0)%5==0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    ((MainActivity)context).runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    // 시간 지난 후 실행할 코딩
                                    mInterstitialAd.show();
                                }}, 500); // 0.5초후
                        }
                    });
                }
            }).start();
            adloadcount ++;
            editor.putInt("adloadcount" , adloadcount);
            editor.apply();
        }else{
            adloadcount ++;
            editor.putInt("adloadcount" , adloadcount);
            editor.apply();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //습관 입력창 복귀 : EditHabbit에서 돌아와서 처리
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                startToast("새 습관이 추가되었습니다.");
            }
        }
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
                                        todoListItems.add(document.toObject(TodoList_Item.class));
                                        long total = (long) document.getData().get("totalsec");
                                        cur += (long) ((long) document.getData().get("curtime") / (double) total * 100.0);
                                        adapter.notifyDataSetChanged();
                                        today += total;
                                    }
                                    piechartmaker();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                                if(todoListItems.size()>0) today_progress = (long) (cur/todoListItems.size());
                                tvTodayProgress.setText(today_progress+ "%" );

                                Map<String, Object> dairyInfo = new HashMap<>();
                                dairyInfo.put(todaydate, today_progress);
                                dairyInfo.put("progress", today_progress);
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
                                if(pd!= null) pd.dismiss();
                            }
                        });
            }
        }).start();
    }

    private void listenerDoc(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void run() {
                        if (firebaseUser != null) {
                            firebaseFirestore
                                    .collection("users") // 목록화할 항목을 포함하는 컬렉션까지 표기
                                    .document(firebaseUser.getUid())
                                    .collection("messages")
                                    .orderBy("time", Query.Direction.DESCENDING) // 파이어베이스 쿼리 정렬

                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    goodText_items.add(document.toObject(GoodText_Item.class));
                                                    goodtextsize.setText(goodText_items.size() + "개");
                                                    if(Objects.equals(document.getData().get("day"), todaydate)) {
                                                    }
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                            goodText_adapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });
            }
        }).start();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 2);
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
            int total = 0;
            for (int i = 0; i < todoListItems.size(); i++) {
                if(todoListItems.get(i).getTotalsec()!=0){
                    NoOfTotalsec.add(new Entry(todoListItems.get(i).getTotalsec(), 0));
                }
                total += todoListItems.get(i).getTotalsec();
            }
            double percentage = total /(24*3600.0)*100;

            //하루 24 남은 시간
            NoOfTotalsec.add(new Entry(24 * 3600 - total, 0));
            PieDataSet dataSet = new PieDataSet(NoOfTotalsec, "");

            //차트 항목 타이틀 지정
            ArrayList title = new ArrayList();
            for (int i = 0; i < todoListItems.size(); i++) {
                if(todoListItems.get(i).getHabbittitle()!=null) {
                    title.add(todoListItems.get(i).getHabbittitle());
                } else {
                    startToast("해당일에는 실행한 습관이 없습니다.");
                }
            }
            title.add("하루");

            if(NoOfTotalsec.size() == title.size()) {
                PieData data = new PieData(title, dataSet); // MPAndroidChart v3.X 오류 발생
                pieChart.setData(data);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//            pieChart.animateXY(2000, 2000);
                pieChart.setDescription("");
                pieChart.setAlpha(0.8f);
                pieChart.setCenterText("습관\n비중\n" + String.format("%.2f", percentage) + "%");
                pieChart.setCenterTextSize(20);
                pieChart.setCenterTextColor(Color.parseColor("#A4193D"));
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
        ShowCount++;

        todayitemsize = todoListItems.size();

        //오늘 날짜의 플레이중인 항목의 진행 상황과 스톱 시간을 저장
        new Thread(() -> {
            editor.putInt("showcount", ShowCount);
            editor.putLong("stoptime", System.currentTimeMillis());
            editor.putInt("todayitemsize", todayitemsize);
            editor.putString("yesterday", todaydate);
            editor.putString("name", todaydate);
            editor.apply();

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
        System.gc();
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

//플레이 중이면 스레드 중지
        if(timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }

        goodTextReceiver = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onResume() {
        super.onResume();

        scrollview.smoothScrollTo(0, 0);

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

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

        veffectintro.setVisibility(View.GONE);

        // 파이어베이스 회원 정보 모으기
        getprofile();

        // 현재 날짜 구하기
        gettodaydate();

        todayitemsize = pref.getInt("todayitemsize", 0);

        if(todayitemsize>0){
            //로딩 화면 만들기
            pd = null;
            pd = ProgressDialog.show(this, "리스트 불러 오는 중......", "잠시만 기다려 주세요.");
            vhabbitportion.setVisibility(View.VISIBLE);
            veffectstandby.setVisibility(View.VISIBLE);

            if(todayitemsize > 1) {
                tvnewhabbits.setVisibility(View.GONE);
            }

            LineChartMaker();
        }

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.

        long stoptime = pref.getLong("stoptime", now);
        String rank = pref.getString("rank", "");
        float rankscore = pref.getFloat("rankscore", 100);
        tvmyrankscore.setText(rank +"");
        settrophyimage(rankscore);
        ShowCount = pref.getInt("showcount", 0);
        timegap = (int)((now-stoptime)/1000);

        //하단 프로세스 달력추가
        EventCalendarMaker(calendar);

        goodTextReceiver = false;
        listenergoodtextlistDoc();
    }

    @SuppressLint("SetTextI18n")
    private void SetRank(double rankscore) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void run() {
                        if(subscribing) {
                            fulladview = false;
                            adview1 = false;
                            adview2 = false;
                            adview3 = false;
                            adview4 = false;
                            adview5 = false;
                            adview6 = false;
                            adview7 = false;
                            adview8 = false;
                            adview9 = false;
                            adview10 = false;
                            blurview1 = false;
                            blurview2 = false;
                            blurview3 = false;
                            blurview4 = false;

                            veffectintro.setVisibility(View.GONE);
                            veffectstandby.setVisibility(View.GONE);

                            tvrankereffect.setText("구매 쿠폰 사용 중");
                            tveventeffect.setText(" - 모든 흐림 효과, 광고 제거");

                            adBanner.setVisibility(View.GONE);
                            adBanner2.setVisibility(View.GONE);
                            adBanner3.setVisibility(View.GONE);
                            adBanner4.setVisibility(View.GONE);

                        } else {
                            fulladview = true;
                            adview1 = true;
                            adview2 = true;
                            adview3 = true;
                            adview4 = true;
                            adview5 = true;
                            adview6 = true;
                            adview7 = true;
                            adview8 = true;
                            adview9 = true;
                            adview10 = true;
                            blurview1 = true;
                            blurview2 = true;
                            blurview3 = true;
                            blurview4 = true;

                            if (99.94 < rankscore && rankscore <= 100.0) {
                                tvrankereffect.setText("Iron IV 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(습관을 실천하세요)");

                            } else if (99.64 < rankscore && rankscore <= 99.94) {
                                tvrankereffect.setText("Iron III 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(습관을 실천하세요)");

                            } else if (98.94 < rankscore && rankscore <= 99.64) {
                                tvrankereffect.setText("Iron II 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(습관을 실천하세요)");

                            } else if (97.93 < rankscore && rankscore <= 98.94) {
                                tvrankereffect.setText("Iron I 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(습관을 실천하세요)");

                            } else if (95.53 < rankscore && rankscore <= 97.93) {
                                tvrankereffect.setText("Bronze IV 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(조금 더 노력하세요)");

                            } else if (92.78 < rankscore && rankscore <= 95.53) {
                                tvrankereffect.setText("Bronze III 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(조금 더 노력하세요)");

                            } else if (88.73 < rankscore && rankscore <= 92.78) {
                                tvrankereffect.setText("Bronze II 보상 없슴");
                                tveventeffect.setText(" - 해당 보상 없슴(조금 더 노력하세요)");

                            } else if (82.76 < rankscore && rankscore <= 88.73) {
                                tvrankereffect.setText("Bronze I 보상 발동 중");
                                tveventeffect.setText(" - 해당 보상 없슴(조금 더 노력하세요)");

                            } else if (73.61 < rankscore && rankscore <= 82.76) {
                                tvrankereffect.setText("Silver IV 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = true;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (66.31 < rankscore && rankscore <= 73.61) {
                                tvrankereffect.setText("Silver III 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = true;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (57.53 < rankscore && rankscore <= 66.31) {
                                tvrankereffect.setText("Silver II 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = true;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (50.21 < rankscore && rankscore <= 57.53) {
                                tvrankereffect.setText("Silver I 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = true;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (36.76 < rankscore && rankscore <= 50.21) {
                                tvrankereffect.setText("Gold IV 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관상세] 상단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = false;
                                adview9 = true;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (29.14 < rankscore && rankscore <= 36.76) {
                                tvrankereffect.setText("Gold III 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = true;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (22.53 < rankscore && rankscore <= 29.14) {
                                tvrankereffect.setText("Gold II 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = true;
                                adview5 = true;
                                adview6 = true;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;

                            } else if (18.36 < rankscore && rankscore <= 22.53) {
                                tvrankereffect.setText("Gold I 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = true;
                                adview4 = false;
                                adview5 = true;
                                adview6 = true;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (10.58 < rankscore && rankscore <= 18.36) {
                                tvrankereffect.setText("Platinum IV 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = false;
                                adview4 = false;
                                adview5 = true;
                                adview6 = true;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (7.58 < rankscore && rankscore <= 10.58) {
                                tvrankereffect.setText("Platinum III 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = false;
                                adview4 = false;
                                adview5 = true;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (5.59 < rankscore && rankscore <= 7.58) {
                                tvrankereffect.setText("Platinum II 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거+" +
                                        "\n - [전체 습관] 하단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (3.67 < rankscore && rankscore <= 5.59) {
                                tvrankereffect.setText("Platinum I 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (1.45 < rankscore && rankscore <= 3.67) {
                                tvrankereffect.setText("Diamond IV 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = true;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (0.68 < rankscore && rankscore <= 1.45) {
                                tvrankereffect.setText("Diamond III 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거");
                                fulladview = true;
                                adview1 = true;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (0.31 < rankscore && rankscore <= 0.68) {
                                tvrankereffect.setText("Diamond II 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거" +
                                        "\n - [오늘의 습관] 배너광고 제거");
                                fulladview = true;
                                adview1 = false;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = true;
                            } else if (0.11 < rankscore && rankscore <= 0.31) {
                                tvrankereffect.setText("Diamond I 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거" +
                                        "\n - [오늘의 습관] 배너광고 제거" +
                                        "\n - [상세 습관] 보이기");
                                fulladview = true;
                                adview1 = false;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = true;
                                blurview2 = true;
                                blurview3 = false;
                                blurview4 = false;
                            } else if (0.06 < rankscore && rankscore <= 0.11) {
                                tvrankereffect.setText("Master 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거" +
                                        "\n - [오늘의 습관] 배너광고 제거" +
                                        "\n - [상세 습관] 보이기" +
                                        "\n - [월간 성취율] 보이기");
                                fulladview = true;
                                adview1 = false;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = true;
                                blurview2 = false;
                                blurview3 = false;
                                blurview4 = false;
                            } else if (0.02 < rankscore && rankscore <= 0.06) {
                                tvrankereffect.setText("G_Master 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거" +
                                        "\n - [오늘의 습관] 배너광고 제거" +
                                        "\n - [상세 습관] 보이기" +
                                        "\n - [월간 성취율] 보이기" +
                                        "\n - [습관 비중] 보이기");
                                fulladview = true;
                                adview1 = false;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = false;
                                blurview2 = false;
                                blurview3 = false;
                                blurview4 = false;
                            } else if (0 < rankscore && rankscore <= 0.02) {
                                tvrankereffect.setText("Challenger 보상 발동 중");
                                tveventeffect.setText(" - [선물받은 명언] 보이기" +
                                        "\n - [습관 상세] 상단 배너광고 제거" +
                                        "\n - [전체 습관] 상단 배너광고 제거" +
                                        "\n - [달성 순위] 상단 배너광고 제거" +
                                        "\n - [선물받은 명언] 배너광고 제거" +
                                        "\n - [월간 성취율] 배너광고 제거" +
                                        "\n - [상세 습관] 하단 배너광고 제거" +
                                        "\n - [전체 습관] 하단 배너광고 제거" +
                                        "\n - [달성 순위] 하단 배너광고 제거" +
                                        "\n - [습관 비중] 배너광고 제거" +
                                        "\n - [오늘의 습관] 배너광고 제거" +
                                        "\n - [상세 습관] 보이기" +
                                        "\n - [월간 성취율] 보이기" +
                                        "\n - [습관 비중] 보이기" +
                                        "\n - 전면 광고 제거");
                                fulladview = false;
                                adview1 = false;
                                adview2 = false;
                                adview3 = false;
                                adview4 = false;
                                adview5 = false;
                                adview6 = false;
                                adview7 = false;
                                adview8 = false;
                                adview9 = false;
                                adview10 = false;
                                blurview1 = false;
                                blurview2 = false;
                                blurview3 = false;
                                blurview4 = false;
                            }

                            adBanner.setVisibility(View.GONE);
                            adBanner2.setVisibility(View.GONE);
                            adBanner3.setVisibility(View.GONE);
                            adBanner4.setVisibility(View.GONE);

                            if (adview1) {
                                AdRequest adRequest = new AdRequest.Builder().build();
                                adBanner.setVisibility(View.VISIBLE);
                                adBanner.loadAd(adRequest);
                            }
                            if (adview2) {
                                AdRequest adRequest2 = new AdRequest.Builder().build();
                                adBanner2.setVisibility(View.VISIBLE);
                                adBanner2.loadAd(adRequest2);
                            }
                            if (adview3) {
                                AdRequest adRequest3 = new AdRequest.Builder().build();
                                adBanner3.setVisibility(View.VISIBLE);
                                adBanner3.loadAd(adRequest3);
                            }
                            if (adview4) {
                                AdRequest adRequest4 = new AdRequest.Builder().build();
                                adBanner4.setVisibility(View.VISIBLE);
                                adBanner4.loadAd(adRequest4);
                            }

                            if (blurview1) vblur1.setVisibility(View.VISIBLE);
                            if (blurview2) vblur2.setVisibility(View.VISIBLE);
                            if (blurview3) vblur3.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    private void settrophyimage(float rankscore) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    private Drawable drawable;

                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void run() {

                        if (97.93 < rankscore && rankscore <= 100) {
                            drawable = getResources().getDrawable(R.drawable.iron);
                        } else if (82.76 < rankscore && rankscore <= 97.93) {
                            drawable = getResources().getDrawable(R.drawable.bronze);
                        } else if (50.21 < rankscore && rankscore <= 82.76) {
                            drawable = getResources().getDrawable(R.drawable.silver);
                        } else if (18.36 < rankscore && rankscore <= 50.21) {
                            drawable = getResources().getDrawable(R.drawable.gold);
                        } else if (3.67 < rankscore && rankscore <= 18.36) {
                            drawable = getResources().getDrawable(R.drawable.platinum);
                        } else if (0.11 < rankscore && rankscore <= 3.67) {
                            drawable = getResources().getDrawable(R.drawable.diamond);
                        } else if (0.06 < rankscore && rankscore <= 0.11) {
                            drawable = getResources().getDrawable(R.drawable.master);
                        } else if (0.02 < rankscore && rankscore <= 0.06) {
                            drawable = getResources().getDrawable(R.drawable.g_master);
                        } else if (0 < rankscore && rankscore <= 0.02) {
                            drawable = getResources().getDrawable(R.drawable.challenger);
                        }
                        ivTrophy.setImageDrawable(drawable);
                    }
                });
            }
        }).start();
    }

    private void getdocforgetlike() {

        new Thread(() -> {
            if (firebaseUser != null) {
                DocumentReference documentReference = firebaseFirestore
                        .collection("users")
                        .document(firebaseUser.getUid());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                    tvgetcount.setText(Objects.requireNonNull(document.getData()).get("getcount")+ "");
                                    tvtodayget.setText(Objects.requireNonNull(document.getData()).get("getcount")+ "");
                                    String eventscore = (String) String.valueOf(document.getData().get("eventscore"));
                                    SetRank(Double.parseDouble(eventscore));
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        }).start();
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
                    MySoundPlayer.play(MySoundPlayer.TEAR);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    if(firebaseUser != null) {
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

                                }
                            });
                        }
                    }).start();
                }else {
                    //오른쪽으로 밀었을때.
                    MySoundPlayer.play(MySoundPlayer.PAGE);
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
                        p2.setTextSize(20);
                        p2.setAntiAlias(true);
                        //텍스트 높이
                        Rect bounds = new Rect();
                        p2.getTextBounds(text, 0, text.length(), bounds);
                        int textheight = bounds.height();
                        //비트맵
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_history);
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
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.deleteicon2);
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
                startToast("이동 시작");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                //왼쪽으로 밀었을때.
                if (direction == ItemTouchHelper.LEFT) {
                    MySoundPlayer.play(MySoundPlayer.TEAR);
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
                                                    goodtextsize.setText(goodText_items.size()+ "개");
                                                    goodText_adapter.notifyItemRemoved(position);

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
                                                                    int getcount = Integer.parseInt(String.valueOf(Objects.requireNonNull(document.getData()).get("getcount")));
                                                                    getcount =  getcount + 1;

                                                                    tvgetcount.setText(getcount + "");
                                                                    tvtodayget.setText(getcount + "");
                                                                    tvplus1.setVisibility(View.VISIBLE);

                                                                    Handler mHandler = new Handler();
                                                                    mHandler.postDelayed(new Runnable() {
                                                                        public void run() {
                                                                            // 시간 지난 후 실행할 코딩
                                                                            tvplus1.setVisibility(View.GONE);
                                                                            }}, 500); // 0.5초후

                                                                    Map<String, Object> count = new HashMap<>();
                                                                    count.put("getcount", getcount);

                                                                    firebaseFirestore
                                                                            .collection("users")
                                                                            .document(firebaseUser.getUid())

                                                                            .set(count, SetOptions.merge())
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
                                                            } else {
                                                                Log.d(TAG, "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });

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
                    MySoundPlayer.play(MySoundPlayer.PAGE);
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
                                        dayformat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                                        time = timeformat.format(date);
                                        day = dayformat.format(date);

                                        RandomGoodText.make(getApplicationContext(), goodText_items.get(position).getFromid(), day, time, inputname);
                                        goodText_adapter.notifyItemChanged(position);

                                        DocumentReference documentReference = firebaseFirestore
                                                .collection("users")
                                                .document(goodText_items.get(position).getFromid());

                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document != null) {
                                                        if (document.exists()) {
                                                            long getcount = (long) document.get("getcount");

                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("getcount", getcount+1);
                                                            firebaseFirestore
                                                                    .collection("users")
                                                                    .document(goodText_items.get(position).getFromid())
                                                                    .set(data, SetOptions.merge());
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
                        p.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getLeft() + dX, (float) itemView.getTop(), (float) itemView.getLeft(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        //텍스트
                        Paint p2 = new Paint();
                        String text = "명언 답장";
                        p2.setColor(Color.parseColor("#5CC8D7"));
                        p2.setTextSize(20);
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
                        p.setColor(Color.parseColor("#FFFFFF"));
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
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.deleteicon2);
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
        new Thread(() -> {
            if (firebaseUser != null) {
                for (UserInfo profile : firebaseUser.getProviderData()) {
                    name = profile.getDisplayName();
                    email = profile.getEmail();
                    Uri photo = profile.getPhotoUrl();
                    photourl = String.valueOf(photo);
                }
            }
        }).start();
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

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        saveProgressAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis() +AlarmManager.INTERVAL_DAY
                , AlarmManager.INTERVAL_DAY, resetSender);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM-dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));
    }

    @Override
    public void onBackPressed() {long now = System.currentTimeMillis();

        long result = now - Back_Key_Before_Time;
        if(result < 2000)
        {
            finish();
        }
        else
        {
            Toast.makeText(MainActivity.this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            Back_Key_Before_Time = System.currentTimeMillis();
        }
    }

    //해시 값 찾기
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}