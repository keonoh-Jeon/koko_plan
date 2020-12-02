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
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    private MemoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View btnPlus = findViewById(R.id.btnPlus);

        recyclerView = (RecyclerView) findViewById(R.id.rv_view);

        initSwipe();

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

        });


        db = MemoDatabase.getDatabase(this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(db);
        recyclerView.setAdapter(adapter);

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.memoDao().getAll().observe(this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(List<Memo> data) {
                adapter.setItem(data);
            }
        });
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
                            db.memoDao().delete(adapter.getItems().get(position));
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