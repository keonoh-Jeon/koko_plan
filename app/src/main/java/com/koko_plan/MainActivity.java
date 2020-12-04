package com.koko_plan;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mTodoEditText;
    private TextView mResultTextView, tvcycle, tvPlayTime;

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private Paint p = new Paint();

    private TodoDatabase db;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private View btnPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = TodoDatabase.getDatabase(this);

        btnPlus = findViewById(R.id.btnPlus);

        recyclerView = (RecyclerView) findViewById(R.id.rv_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(db);
        recyclerView.setAdapter(adapter);

        initSwipe();
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnPlus.setOnClickListener(v -> {
            myStartActivity(EditHabbit.class);
        });

        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.todoDao().getAll().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> data) {
                adapter.setItem(data);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //EditHabbit에서 돌아와서 처리
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String habbittitle = data.getStringExtra("habbittitle");
                int count = data.getIntExtra("count", 0);
                int hour = data.getIntExtra("hour", 0);
                int min = data.getIntExtra("min", 0);
                int sec = data.getIntExtra("sec", 0);
                boolean isrunning = data.getBooleanExtra("isrunning", false);
                Log.d(TAG, "onActivityResult: " + isrunning);

                @SuppressLint("DefaultLocale")
                int totalsec = hour*60*60+min*60+sec;

                new Thread(() -> {
                    Todo todo = new Todo(0, habbittitle, 0, count, hour, min, sec, totalsec, isrunning);
                    Log.e(TAG, "onActivityResult: " + todo);
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
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