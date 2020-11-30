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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mTodoEditText;
    private TextView mResultTextView, tvcycle, tvPlayTime;

    private Paint p = new Paint();

    ItemTouchHelper helper;
    private MemoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        container.setLayoutTransition(new LayoutTransition());

        View btnPlus = findViewById(R.id.btnPlus);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_view);

        /*// title 입력 다이얼로그를 호출한다.
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
            builder.show();*/
        btnPlus.setOnClickListener(this::buttonMethodAdd);

        db = MemoDatabase.getDatabase(this);
    }

    public void buttonMethodAdd(View v){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.add(R.id.container, new MyTimerFragment());
        t.commit();
    }
}