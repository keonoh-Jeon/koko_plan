package com.koko_plan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemTouchHelperListener {

    private List<Todo> items = new ArrayList<>();
    private Context mContext;
    private TodoDatabase db;
    private boolean isRunning;
    private TextView tvCycle;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public RecyclerAdapter(TodoDatabase db) {
        this.db = db;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Todo todo){ items.add(todo); }

    public List<Todo> getItems() {return items;}

    public void setItem(List<Todo> data) {
        items = data;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {

        items.remove(position);
        notifyItemRemoved(position);

        new Thread(() -> {
            db.todoDao().delete(items.get(position));

        }).start();
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        mContext = viewGroup.getContext();
        final ViewHolder holder = new ViewHolder(v);

        v.findViewById(R.id.tv_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
//                showPopupcycle(v, holder.getAdapterPosition());
            }
        });
        v.findViewById(R.id.tvTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                showPopupCountTime(v, holder.getAdapterPosition());
            }
        });
        v.findViewById(R.id.tvTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                makedialogtitle(v, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.onBind(items.get(position),position);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.e(TAG, "onDetachedFromRecyclerView: ");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvCount;
        private TextView tvCurTime, tvTime;
        private Button btnSave;
        private TextView tvplayCount;
        private ImageView ivPlus, ivMinus, ivPlay, ivPause, ivStop, ivbdelete;

        private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn;
        private TextView mTimeTextView, mRecordTextView;
        private Thread timeThread = null;
        private Boolean isRunning = true;

        TimerTask timerTask;
        Timer timer = new Timer();

        private int index;

        @SuppressLint("SetTextI18n")
        public ViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvCount = view.findViewById(R.id.tv_count);
            tvCurTime = view.findViewById(R.id.tv_curtime);
            tvCurTime.setText("00:00:00");
            tvTime = view.findViewById(R.id.tvTime);

            ivPlus = view.findViewById(R.id.iv_plus);
            ivPlus.setVisibility(View.GONE);
            ivPlus.setOnClickListener(v -> editPlus());
            ivMinus = view.findViewById(R.id.iv_minus);
            ivMinus.setVisibility(View.GONE);
            ivMinus.setOnClickListener(v -> editMinus());
            ivPlay = view.findViewById(R.id.iv_play);
            ivPlay.setVisibility(View.GONE);
            ivPlay.setOnClickListener(v -> editPlay());
            ivPause = view.findViewById(R.id.iv_pause);
            ivPause.setVisibility(View.GONE);
            ivPause.setOnClickListener(v -> editPause());
            ivStop = view.findViewById(R.id.iv_stop);
            ivStop.setVisibility(View.GONE);
            ivStop.setOnClickListener(v -> editStop());

//            ivbtnreset.setOnClickListener(v -> editPlayPluscount());

            ivbdelete = view.findViewById(R.id.ivbnt_delete);
            ivbdelete.setOnClickListener(v -> itemdelete());

            /*mStartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    mStopBtn.setVisibility(View.VISIBLE);
                    mRecordBtn.setVisibility(View.VISIBLE);
                    mPauseBtn.setVisibility(View.VISIBLE);

                    timeThread.start();
                }
            });

            mStopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    mRecordBtn.setVisibility(View.GONE);
                    mStartBtn.setVisibility(View.VISIBLE);
                    mPauseBtn.setVisibility(View.GONE);
                    mRecordTextView.setText("");
                    timeThread.interrupt();
                }
            });

            mRecordBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    mRecordTextView.setText(mRecordTextView.getText() + mTimeTextView.getText().toString() + "\n");
                }
            });

            mPauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isRunning = !isRunning;
                    if (isRunning) {
                        mPauseBtn.setText("일시정지");
                    } else {
                        mPauseBtn.setText("시작");
                    }
                }
            });*/
        }

        private void startTimerTask()
        {
            stopTimerTask();
            timerTask = new TimerTask()
            {
                int count = 0;

                @Override
                public void run()
                {
                    count++;

                    /*new Thread(() -> {
                        items.get(index).setTotalsec(count);
                        db.todoDao().update(items.get(index));
                    }).start();*/

                    long second = count % 60;
                    long minute = (count / 60) % 60;
                    long hour = (count / 3600) % 24;

                    tvTime.post(new Runnable() {
                        @SuppressLint({"SetTextI18n", "DefaultLocale"})
                        @Override
                        public void run() {
                            tvCurTime.setText(String.format("%2d:%2d:%2d", hour, minute, second));
                        }
                    });
                }
            };
            timer.schedule(timerTask,0 ,1000);
        }

        @SuppressLint("SetTextI18n")
        private void stopTimerTask()
        {
            if(timerTask != null)
            {
                tvCurTime.setText("00:00:00");
                timerTask.cancel();
                timerTask = null;
            }
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void onBind(Todo todo, int position){
            index = position;
            tvTitle.setText(todo.getTitle());
            if(todo.getCount()==0){
                tvTime.setText(String.format("%02d:%02d:%02d", todo.getHour(), todo.getMin(), todo.getSec()));
            } else {
                tvTime.setText(String.format("%d", todo.getCount()));
            }
            if(todo.getCount() == 0){
                if(!items.get(index).getIsrunning()){
                    ivPlay.setVisibility(View.VISIBLE);
                } else {
                    ivPause.setVisibility(View.VISIBLE);
                }
                ivStop.setVisibility(View.VISIBLE);
                ivPlus.setVisibility(View.GONE);
                ivMinus.setVisibility(View.GONE);
            } else {
                ivPlus.setVisibility(View.VISIBLE);
                ivMinus.setVisibility(View.VISIBLE);
                ivStop.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
            }

            if(todo.getIsrunning()) {
                startTimerTask();
            }
        }

        /*public void editData(String contents){
            new Thread(() -> {
                items.get(index).setContents(contents);
                db.memoDao().update(items.get(index));
            }).start();
            Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show();
        }*/

        @SuppressLint("SetTextI18n")
        public void itemdelete() {
            if(timerTask != null)
            {
                timerTask.cancel();
                timerTask = null;
            }
            new Thread(() -> {
                db.todoDao().delete(items.get(index));
            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editStop() {
                stopTimerTask();
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.GONE);

                isRunning = !isRunning;
                new Thread(() -> {
                    items.get(index).setIsrunning(isRunning);
                    db.todoDao().update(items.get(index));
                }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editPlay() {
            if(isRunning = false){
                startTimerTask();
                ivPause.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setVisibility(View.GONE);
            }
            isRunning = !isRunning;
            new Thread(() -> {
                items.get(index).setIsrunning(isRunning);
                db.todoDao().update(items.get(index));
            }).start();

            /*for(int i=0; i<items.size(); i++){
                if(items.get(index).getIsrunning()){
                    new Thread(() -> {
                        isRunning = !isRunning;
                        items.get(index).setIsrunning(isRunning);
                        db.todoDao().update(items.get(index));
                    }).start();
                }
            }*/
        }

        @SuppressLint("SetTextI18n")
        public void editPause() {
            if (items.get(index).getCount() > 0) {
                int playcount = items.get(index).getCount() - 1;
                tvTime.setText(playcount + "");
                new Thread(() -> {
                    items.get(index).setCount(playcount);
                    db.todoDao().update(items.get(index));
                }).start();
            } else {
                    if(timerTask != null)
                    {
                        timerTask.cancel();
                        timerTask = null;
                    }
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.GONE);
            }
            isRunning = !isRunning;
            new Thread(() -> {
                items.get(index).setIsrunning(isRunning);
                db.todoDao().update(items.get(index));
            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editPlus() {
                int curcount = items.get(index).getCurcount() + 1;
            tvCurTime.setText(curcount + "");
                new Thread(() -> {
                    items.get(index).setCurcount(curcount);
                    db.todoDao().update(items.get(index));
                }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editMinus() {
            int curcount = items.get(index).getCurcount() ;
            if(curcount > 0) {
                curcount = items.get(index).getCurcount() - 1;
            }
            tvCurTime.setText(curcount + "");
            int finalCurcount = curcount;
            new Thread(() -> {
                items.get(index).setCurcount(finalCurcount);
                db.todoDao().update(items.get(index));
            }).start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void makedialogtitle(View v, final int position){

        final TextView tvTitle = (TextView) v. findViewById(R.id.tvTitle);

        EditText edittext = new EditText(mContext);
        edittext.setText(items.get(position).getTitle());
        edittext.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("일정 수정");
        builder.setMessage("수정할 일정을 기입해주세요.");
        builder.setView(edittext);

        builder.setPositiveButton("입력",
                (dialog, which) -> {
                    //제목 입력, DB추가
                    tvTitle.setText(edittext.getText().toString());
                    new Thread(() -> {
                        items.get(position).setTitle(edittext.getText().toString());
                        db.todoDao().update(items.get(position));
                    }).start();

                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    private void runOnUiThread(Runnable runnable) {
    }

    private void showPopupCountTime(View v, final int position){

        @SuppressLint("RestrictedApi") PopupMenu popup= new PopupMenu(mContext.getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.time_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menucount:
                        makecountdialog(v, position);
                        break;
                    case R.id.menutime:
                        maketimerdialog(v, position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void DialogDatePicker(View v, final int position){
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date_selected = String.valueOf(monthOfYear+1)+ " /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
            }
        };
        DatePickerDialog alert = new DatePickerDialog(mContext,  mDateSetListener, cyear, cmonth, cday);
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    private void maketimerdialog(View v, final int position){

        final TextView textView = v.findViewById(R.id.tvTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View di = inflater.inflate(R.layout.timerinput, null);

        final NumberPicker hourPicker = (NumberPicker) di.findViewById(R.id.picker_hour);
        final NumberPicker minPicker = (NumberPicker) di.findViewById(R.id.picker_min);
        final NumberPicker secPicker = (NumberPicker) di.findViewById(R.id.picker_sec);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(99);
        hourPicker.setValue(0);

        minPicker.setMinValue(0);
        minPicker.setMaxValue(99);
        minPicker.setValue(0);

        secPicker.setMinValue(0);
        secPicker.setMaxValue(99);
        secPicker.setValue(0);

        builder.setTitle("목표 시간 입력");
        builder.setMessage("하루의 목표 시간을 설정하세요");
        builder.setView(di);

        builder.setPositiveButton("입력",
                (dialog, which) -> {

                    @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hourPicker.getValue(), minPicker.getValue(), secPicker.getValue());
                    //제목 입력, DB추가
                    Log.e(TAG, "maketimerdialog: "+ result);
                    textView.setText(result);
                    new Thread(() -> {
                        items.get(position).setHour(hourPicker.getValue());
                        items.get(position).setMin(minPicker.getValue());
                        items.get(position).setSec(secPicker.getValue());
                        items.get(position).setCount(0);
                        items.get(position).setIsrunning(true);
                        db.todoDao().update(items.get(position));
                    }).start();
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void makecountdialog(View v, final int position){

        final NumberPicker numberPicker = new NumberPicker(mContext);
        final TextView textView = v.findViewById(R.id.tvTime);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
        //현재값 설정 (dialog를 실행했을 때 시작지점)
        numberPicker.setValue(1);
        //키보드 입력을 방지
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("횟수 입력");
        builder.setMessage("하루에 몇번을 실행하실지요?");
        builder.setView(numberPicker);

        builder.setPositiveButton("입력",
                (dialog, which) -> {
                    //제목 입력, DB추가
                    textView.setText(numberPicker.getValue()+" ");
                        new Thread(() -> {
                            items.get(position).setHour(0);
                            items.get(position).setMin(0);
                            items.get(position).setSec(0);
                            items.get(position).setCount(numberPicker.getValue());
                            items.get(position).setIsrunning(false);
                            db.todoDao().update(items.get(position));
                        }).start();
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    private void showPopupcycle(View v, final int position){

        @SuppressLint("RestrictedApi")
        PopupMenu popup= new PopupMenu(mContext.getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.cycle_menu, popup.getMenu());

        final TextView tvCount = (TextView) v.findViewById(R.id.tv_count);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.once:
                        editcycle(1);
                        break;
                    case R.id.everytime:
                        editcycle(2);
                        break;
                    case R.id.everyday:
                        editcycle(3);
                        break;
                    case R.id.everyweek:
                        editcycle(4);
                        break;
                    case R.id.everymonth:
                        editcycle(5);
                        break;
                    default:
                        break;
                }
                return false;
            }

            public void editcycle(int s){
                tvCount.setText(s);
                new Thread(() -> {
                    items.get(position).setCount(s);
                    db.todoDao().update(items.get(position));
                }).start();
                Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show();
            }
        });
        popup.show();
    }


}