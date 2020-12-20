package com.koko_plan.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
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

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.sub.ItemTouchHelperCallback;
import com.koko_plan.R;
import com.koko_plan.member.MemberActivity;
import com.koko_plan.member.MemberEditActivity;
import com.koko_plan.member.Profile_Item;
import com.koko_plan.member.Singup;
import com.koko_plan.sub.MySoundPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.koko_plan.main.RecyclerAdapter.items;
import static com.koko_plan.main.RecyclerAdapter.timerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_APP_UPDATE = 600;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    public static String name, email;
    private String inputname;
    private String photourl;
    public static Bitmap profile;

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    private TodoDatabase roomdb;
    private ScrollView scrollview;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    private View btnPlus;
    @SuppressLint("StaticFieldLeak")
    public static Button btnsavelist;

    public static int lastsec, timegap, totalprogress, curtime;

    private TextView tvTodayProgress;
    private TextView nav_header_name_text;

    int cur, total;

    private String todaydate;
    private SimpleDateFormat dateformat;

    ItemTouchHelper helper;
    private String selecteddata;
    private Date date;
    private AppUpdateManager appUpdateManager;
    private ImageView nav_header_photo_image;
    private Calendar calendar;
    private HorizontalCalendar horizontalCalendar;
    private PieChart pieChart;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"CommitPrefEdits", "SimpleDateFormat", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_main);

        //어플 업데이트 매니저
        Appupdatemanager();

        getprofile();

        // 저장 위치 초기화
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        init();
        //객체 초기화
        InitializeView();

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // 현재 날짜 구하기
        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        //날짜 표시 형식 지정
        dateformat = new SimpleDateFormat("yyyy-MM/dd");
        todaydate = dateformat.format(date);

        horizontalCalendarmaker(calendar);

        //리사이클러뷰 초기화
        roomdb = TodoDatabase.getDatabase(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(roomdb);
        recyclerView.setAdapter(adapter);

        //달력추가
        try {
            EventCalendarMaker();
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        initSwipe();

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
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

        //가로 달력 구동시 리스너
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Calendar date, int position) {
                selecteddata = dateformat.format(date.getTime());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {

                                items.clear();
                                adapter.setItem(roomdb.todoDao().search(selecteddata));
                                int selecteditemsize = roomdb.todoDao().search(selecteddata).size();

                                tvTodayProgress.setText("실행한 습관이 없습니다.");

                                if(selecteditemsize > 0){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    cur = 0;
                                                    for(int i=0 ; i < selecteditemsize ; i++) {
                                                        if(adapter.getItems().get(i).getCurtime()>0){
                                                            int curtime = (int) ((double)adapter.getItems().get(i).getCurtime() / ((double)adapter.getItems().get(i).getTotalsec()) * 100.0);
                                                            cur += curtime;
                                                        } else {
                                                            int count = (int) ((double)adapter.getItems().get(i).getCurcount() / ((double)adapter.getItems().get(i).getCount()) * 100.0);
                                                            cur += count;
                                                        }
                                                    }
                                                    tvTodayProgress.setText("오늘의 실행율 : " + cur/selecteditemsize+ "%" );
                                                }
                                            });
                                        }
                                    }).start();
                                }

                                /*for(int i=0 ; i < selecteditemsize ; i++){
                                    Log.e(TAG, "onDateSelected: " + roomdb.todoDao().search(selecteddata).get(i).getDate());
                                }*/

                                piechartmaker();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                Log.e(TAG, "onCalendarScroll: "+ "스크롤");
                timegap = 0;
                if(timerTask != null)
                {
                    timerTask.cancel();
                    timerTask = null;
                }
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

            // Handle the camera action
        } /*else if (id == R.id.nav_history) {
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

// 회원정보 확인 후, 상세 정보 없으면 정보 기입으로 이동.
    private void init() {
        Inits thread = new Inits();
        thread.start();
    }

    class Inits extends Thread {
        public void run() {
            if (firebaseUser == null) {
                myStartActivity(Singup.class);
            } else {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
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
        }
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

        pieChart = findViewById(R.id.piechart);

        btnPlus = findViewById(R.id.btnPlus);
        btnsavelist = findViewById(R.id.btn_savelist);
        tvTodayProgress = findViewById(R.id.tv_todayprogress);

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
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: 시작=============");
        System.gc();
        super.onDestroy();
    }

    //어플 업데이트 관리
    private void Appupdatemanager() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
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
    }

    private void EventCalendarMaker() throws OutOfDateRangeException {

        // https://github.com/Applandeo/Material-Calendar-View

        List<EventDay> events = new ArrayList<>();

        /*Calendar calendar2 = Calendar.getInstance();
        events.add(new EventDay(calendar2, R.drawable.sample_icon));
//or
        events.add(new EventDay(calendar2, new Drawable()));
//or if you want to specify event label color
        events.add(new EventDay(calendar2, R.drawable.sample_icon, Color.parseColor("#228B22")));*/

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarView.setEvents(events);
        calendarView.setDate(date);

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

        roomdb.todoDao().getAll(dateformat.format(date.getTime())).observe(this, new Observer<List<Todo>>() {
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
                    Todo todo = new Todo(size+1, todaydate, habbittitle, 0,0, count, hour, min, sec, totalsec, isrunning);
                    roomdb.todoDao().insert(todo);
                }).start();
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

        if(items.size() > 0) {
            new Thread(() -> {
                for(int i=0; i < adapter.getItems().size() ; i++){
                    Log.e(TAG, "resetId: get" + adapter.getItems().get(i).getNum());
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    adapter.getItems().get(i).setNum((i+1));
                    Log.e(TAG, "resetId: set" + adapter.getItems().get(i).getNum());
                    roomdb.todoDao().update(adapter.getItems().get(i));
                }
            }).start();
        }
    }

    //원형 차트 만들기
    private void piechartmaker() {

        pieChart.setData(null);
        pieChart.clear();

        if(adapter.getItemCount() > 0) {
            ArrayList NoOfTotalsec = new ArrayList();
            total = 0;
            for (int i = 0; i < adapter.getItemCount(); i++) {
                NoOfTotalsec.add(new Entry(adapter.getItems().get(i).getTotalsec(), 0));
                total += items.get(i).getTotalsec();
            }

            //하루 24 남은 시간
            NoOfTotalsec.add(new Entry(24 * 3600 - total, 0));
            PieDataSet dataSet = new PieDataSet(NoOfTotalsec, "... Hour Of habbits");

            //차트 항목 타이틀 지정
            ArrayList title = new ArrayList();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                title.add(adapter.getItems().get(i).getTitle());
            }
            title.add("하루");

            PieData data = new PieData(title, dataSet); // MPAndroidChart v3.X 오류 발생
            pieChart.setData(data);
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            pieChart.animateXY(2000, 2000);
            pieChart.setCenterText("습관\n비중\n" + String.format("%.2f", total / (24 * 3600.0) * 100) + "%");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timegap = 0;

        //해당 항목(오늘 날짜에 해당되는 아이템 리스트)을 어뎁터에 부어버림
        adapter.setItem(roomdb.todoDao().search(todaydate));

        editor.putLong("stoptime", 0);
        editor.apply();

        //오늘 날짜의 플레이중인 항목의 진행 상황과 스톱 시간을 저장
        new Thread(() -> {
            for(int i=0 ; i < adapter.getItemCount() ; i++) {
                if(items.get(i).getIsrunning()) {
                    items.get(i).setCurtime(lastsec-1);
                    roomdb.todoDao().update(adapter.getItems().get(i));

                    //현재의 밀리세컨 구함
                    long now = System.currentTimeMillis();
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

        calendar = Calendar.getInstance();
        horizontalCalendar.selectDate(calendar, false);

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        long stoptime = pref.getLong("stoptime", 0);
        int itemsize = pref.getInt("itemsize", 0);

        timegap = (int)((now-stoptime)/1000);
    }



    //스와이프 기능 헬퍼 연결
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

                            roomdb.todoDao().delete(adapter.getItems().get(position));
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
}