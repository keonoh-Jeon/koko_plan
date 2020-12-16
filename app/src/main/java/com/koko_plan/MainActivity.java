package com.koko_plan;

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
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
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

import static com.koko_plan.RecyclerAdapter.items;
import static com.koko_plan.RecyclerAdapter.timerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_APP_UPDATE = 600;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public static String name, email;
    private String inputname;
    private String photourl;
    public static Bitmap profile;

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    private TodoDatabase roomdb;

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    private View btnPlus;
    @SuppressLint("StaticFieldLeak")
    public static Button btnsavelist;

    public static int lastsec, timegap, totalprogress;

    private TextView tvTodayProgress;
    private TextView nav_header_name_text;

    int cur, total;

    private String day;
    private SimpleDateFormat dateformat;

    ItemTouchHelper helper;
    private String selecteddata;
    private Calendar today;
    private Date date;
    private AppUpdateManager appUpdateManager;
    private ImageView nav_header_photo_image;

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

        //날짜 표시 형식 지정
        dateformat = new SimpleDateFormat("yyyy-MM/dd");

        //객체 초기화
        InitializeView();

        init();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebasedb = FirebaseFirestore.getInstance();

        roomdb = TodoDatabase.getDatabase(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(roomdb);
        recyclerView.setAdapter(adapter);



        date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        day = dateformat.format(date);

        //가로 달력 추가
        // 현재 날짜 구하기


        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        //가로 달력 구현
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();
        //달력 구동시 리스너
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
                                // 해당 작업을 처리함
                                adapter.setItem(roomdb.todoDao().search(selecteddata));
                                int selecteditemsize = roomdb.todoDao().search(selecteddata).size();
                                for(int i=0 ; i < selecteditemsize ; i++){
                                    Log.e(TAG, "onDateSelected: " + roomdb.todoDao().search(selecteddata).get(i).getDate());
                                }
                                piechartmaker();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

        initSwipe();

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

        //달력추가
        EventCalendarMaker();
    }

    private void init() {
        Inits thread = new Inits();
        thread.start();
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
                                    nav_header_name_text.setText(inputname + " ");

                                } else {
                                    Log.d(TAG, "No such document");
                                    myStartActivity(MemberActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void InitializeView() {
        btnPlus = findViewById(R.id.btnPlus);
        btnsavelist = findViewById(R.id.btn_savelist);
        tvTodayProgress = findViewById(R.id.tv_todayprogress);

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

        probitmap();

        nav_header_name_text.setText(name + " ");
        nav_header_mail_text.setText(email + " ");

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

    private void Appupdatemanager() {
        //어플 업데이트 관리
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

    private void EventCalendarMaker() {

        List<EventDay> events = new ArrayList<>();

        /*Calendar calendar2 = Calendar.getInstance();
        events.add(new EventDay(calendar2, R.drawable.sample_icon));
//or
        events.add(new EventDay(calendar2, new Drawable()));
//or if you want to specify event label color
        events.add(new EventDay(calendar2, R.drawable.sample_icon, Color.parseColor("#228B22")));*/

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarView.setEvents(events);
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
                Log.e(TAG, "onStart onChanged: "  + data );
                adapter.setItem(data);
            }
        });

        /*db.todoDao().search(selecteddata)(List<Todo> data) {

            public String toString(List<Todo> data) {
                return super.toString();
                adapter.setItem(data);
            }
        });*/

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
    }

    private void piechartmaker() {

        PieChart pieChart = findViewById(R.id.piechart);

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
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieChart.animateXY(2000, 2000);
            pieChart.setCenterText("습관\n비중\n" + String.format("%.2f", total / (24 * 3600.0) * 100) + "%");
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
                    roomdb.todoDao().update(adapter.getItems().get(i));
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