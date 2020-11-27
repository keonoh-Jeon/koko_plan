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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mTodoEditText;
    private TextView mResultTextView, tvcycle, tvPlayTime;

    private RecyclerAdapter adapter;
    private Paint p = new Paint();

    ItemTouchHelper helper;
    private MemoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View btnPlus = findViewById(R.id.btnPlus);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_view);

        btnPlus.setOnClickListener(v -> {
            // title 입력 다이얼로그를 호출한다.
            // title 입력하여 리사이클러뷰 addItem
            final EditText edittext = new EditText(this);
            edittext.setGravity(Gravity.CENTER);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("일정 추가");
            builder.setMessage("새로운 일정을 추가해주세요.");
            builder.setView(edittext);

            builder.setPositiveButton("입력",
                    (dialog, which) -> {
                        //제목 입력, DB추가
                        if (!edittext.getText().toString().isEmpty()) {
                            new Thread(() -> {
                                Memo memo = new Memo(edittext.getText().toString(), null, 0, 0);
                                db.memoDao().insert(memo);
                            }).start();

                        }

                    });
            builder.setNegativeButton("취소",
                    (dialog, which) -> {
                        //취소버튼 클릭
                    });
            builder.show();

        });

        db = MemoDatabase.getDatabase(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(db);
        recyclerView.setAdapter(adapter);

        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.memoDao().getAll().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(List<Memo> data) {
                adapter.setItem(data);
            }
        });
    }

}