package com.koko_plan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mTodoEditText;
    private TextView tvTitle, mResultTextView, tvcycle, tvPlayTime;

    private Paint p = new Paint();
    private int size, oldsize;
    public static MemoDatabase db;
    private List<Todo> items = new ArrayList<>();

    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        container.setLayoutTransition(new LayoutTransition());

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        db = MemoDatabase.getDatabase(this);

        /*//UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.todoDao().getAll().observe(this, new Observer<List<Todo>>() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onChanged(List<Todo> items) {
                size = items.size();
                editor.putInt("size", size);
                editor.commit();

                if (size == 0) {
                    for (int i=0 ; i<size ; i++) {
                        Fragment list = new MyTimerFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction t = fm.beginTransaction();
                        //프레그먼트 매니저 상에 새 프레그먼트 추가
                        t.add(R.id.container, list);
                        t.commit();

//            Log.d(TAG, "onChanged: size " + items.get(i));

            *//*Bundle bundle = new Bundle();
            bundle.putString("title", items.get(i).getTitle());
            list.setArguments(bundle);*//*
                    }
                }
            }
        });*/

        View btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(this::buttonMethodAdd);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.todoDao().getAll().observe(this, new Observer<List<Todo>>() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onChanged(List<Todo> items) {
                size = items.size();
                oldsize = pref.getInt("size", size);
                if(oldsize == size) {
                    for (int i=0 ; i<size ; i++) {
                        Fragment list = new MyTimerFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction t = fm.beginTransaction();
                        //프레그먼트 매니저 상에 새 프레그먼트 추가
                        t.add(R.id.container, list);
                        t.commit();

                        Bundle bundle = new Bundle();
                        bundle.putString("title", items.get(i).getTitle());
                        list.setArguments(bundle);
                    }
                }
                editor.putInt("size", size);
                editor.commit();
            }
        });
    }

    protected void onResume() {
        super.onResume();

    }

    public void buttonMethodAdd(View v){

        //일정 프레그먼트 추가 작업(텍스트 뷰 입력창 도입)
        final EditText edittext = new EditText(this);
        edittext.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("일정 추가");
        builder.setMessage("새로운 일정을 추가해주세요.");
        builder.setView(edittext);

        builder.setPositiveButton("입력",
                (dialog, which) -> {
                    //제목 입력, DB추가(빈칸이 아니면...)
                    if (!edittext.getText().toString().isEmpty()) {

                        //새로운 프레그 먼트 추가
                        Fragment list = new MyTimerFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction t = fm.beginTransaction();
                        //프레그먼트 매니저 상에 새 프레그먼트 추가
                        t.add(R.id.container, list);
                        t.commit();

                        //번들에 저장하여, 신규 프레그먼트로 값 전달
                        Bundle bundle = new Bundle();
                        bundle.putString("title", edittext.getText().toString());
                        list.setArguments(bundle);

                        //db에 신규일정 타이틀 저장
                        new Thread(() -> {
                            Todo memo = new Todo(0, edittext.getText().toString(), null, 0, 0);
                            db.todoDao().insert(memo);
                        }).start();
                    }

                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }
}