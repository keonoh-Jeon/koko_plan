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
import android.widget.ProgressBar;
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
import static com.koko_plan.MainActivity.btnsavelist;
import static com.koko_plan.MainActivity.editor;
import static com.koko_plan.MainActivity.lastsec;
import static com.koko_plan.MainActivity.pref;
import static com.koko_plan.MainActivity.timegap;
import static com.koko_plan.MainActivity.totalprogress;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemTouchHelperListener {

    public static List<Todo> items = new ArrayList<>();
    public static TimerTask timerTask;
    private Context mContext;
    private TodoDatabase db;
    private boolean isRunning;
    private TextView tvCycle;

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
//todo
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

        btnsavelist.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        mContext = viewGroup.getContext();
        final ViewHolder holder = new ViewHolder(v);

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
        private TextView tvProgress;
        private TextView tvCurTime, tvTime;
        private Button btnSave;
        private TextView tvplayCount;
        private ImageView ivPlus, ivMinus, ivPlay, ivPause, ivStop, ivbdelete;

        private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn;
        private TextView mTimeTextView, mRecordTextView;
        private Thread timeThread = null;
        private Boolean isRunning = true;

        private int index, gap;

        ProgressBar progressBar;
        Timer timer = new Timer();

        @SuppressLint("SetTextI18n")
        public ViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvProgress = view.findViewById(R.id.tv_progress);
            tvCurTime = view.findViewById(R.id.tv_curtime);
            tvCurTime.setText("0");
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

            progressBar = view.findViewById(R.id.progressBar);
        }

        private void startTimerTask()
        {
            stopTimerTask();
            timerTask = new TimerTask()
            {
                int count = items.get(index).getCurtime() + timegap;

                @Override
                public void run()
                {
                    count++;
                    lastsec = count;

                    long second = count % 60;
                    long minute = (count / 60) % 60;
                    long hour = (count / 3600) % 24;

                    tvTime.post(new Runnable() {
                        @SuppressLint({"SetTextI18n", "DefaultLocale"})
                        @Override
                        public void run() {
                            if(items.get(index).getIsrunning()){
                                tvCurTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                                tvProgress.setText((int) ((double)count / ((double)items.get(index).getTotalsec()) *100.0) + " %");
                                progressBar.setProgress((int) ((double)count / ((double)items.get(index).getTotalsec()) *100.0));
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask,0 ,1000);
        }

        @SuppressLint("SetTextI18n")
        public void stopTimerTask()
        {
            if(timerTask != null)
            {
                timerTask.cancel();
                timerTask = null;
            }
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void onBind(Todo todo, int position){
            index = position;
            tvTitle.setText(todo.getTitle());

            if(todo.getCount() == 0){
                tvTime.setText(String.format("%02d:%02d:%02d", todo.getHour(), todo.getMin(), todo.getSec()));
                if(!items.get(index).getIsrunning()){
                    ivPlay.setVisibility(View.VISIBLE);
                    ivPause.setVisibility(View.GONE);
                } else {
                    ivPause.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
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

                tvTime.setText(String.format("%d", todo.getCount()));
            }

            if(todo.getCount() > 0) {
                int progress = (int) ((double)todo.getCurcount() / ((double)items.get(index).getCount()) *100.0);
                tvCurTime.setText(""+todo.getCurcount());
                tvProgress.setText(progress + " %");
                progressBar.setProgress(progress);
                /*if(progress>=100) {
                    new Thread(() -> {
                        items.get(index).setNum(getItemCount());
                        db.todoDao().update(items.get(index));
                    }).start();
                }*/
                totalprogress += progress;

            } else {
                if (todo.getCurtime() > 0) {
                    int curtime = todo.getCurtime();
                    int hour = curtime / 60 / 60;
                    int min = curtime / 60 % 60;
                    int sec = curtime % 60;
                    tvCurTime.setText(String.format("%02d:%02d:%02d", hour, min, sec));

                    int progress = (int) ((double)todo.getCurtime() / ((double)items.get(index).getTotalsec()) *100.0);
                    tvProgress.setText(progress + " %");
                    progressBar.setProgress(progress);
                    /*if(progress>=100) {
                        new Thread(() -> {
                            items.get(index).setNum(getItemCount());
                            db.todoDao().update(items.get(index));
                        }).start();
                    }*/

                } else {
                    tvCurTime.setText("00:00:00");
                    int progress = (int) ((double)todo.getCurtime() / ((double)items.get(index).getTotalsec()) *100.0);
                    tvProgress.setText(progress + " %");
                    progressBar.setProgress(progress);
                    /*if(progress>=100) {
                        new Thread(() -> {
                            items.get(index).setNum(getItemCount());
                            db.todoDao().update(items.get(index));
                        }).start();
                    }*/
                }
            }
            if(todo.getIsrunning()) startTimerTask();

        }

        @SuppressLint("SetTextI18n")
        public void editStop() {
            timegap = 0;
            if(isRunning = true) {
                stopTimerTask();
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.GONE);

                isRunning = !isRunning;
                new Thread(() -> {
                    for(int i=0; i<index; i++){
                        if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                        db.todoDao().update(items.get(i));
                    }
                    for(int i=(index+1); i <items.size() ; i++){
                        if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                        db.todoDao().update(items.get(i));
                    }
                    items.get(index).setIsrunning(isRunning);
                    items.get(index).setCurtime(0);
                    db.todoDao().update(items.get(index));
                }).start();
            }
        }

        @SuppressLint("SetTextI18n")
        public void editPlay() {
            timegap = 0;
            if(isRunning = false){
                ivPause.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setVisibility(View.GONE);
            }
            isRunning = !isRunning;
            new Thread(() -> {
                items.get(index).setIsrunning(isRunning);
                db.todoDao().update(items.get(index));

                //나머지 항목 전부 일시 정지
                for(int i=0; i<index; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    items.get(i).setIsrunning(!isRunning);
                    db.todoDao().update(items.get(i));
                }
                for(int i=(index+1); i <items.size() ; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    items.get(i).setIsrunning(!isRunning);
                    db.todoDao().update(items.get(i));
                }
            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editPause() {
            timegap = 0;
            if(timerTask != null)
            {
                new Thread(() -> {
                    items.get(index).setCurtime(lastsec);
                    db.todoDao().update(items.get(index));
                }).start();

                timerTask.cancel();
                timerTask = null;
            }

            ivPlay.setVisibility(View.VISIBLE);
            ivPause.setVisibility(View.GONE);

            isRunning = !isRunning;
            new Thread(() -> {
                items.get(index).setIsrunning(isRunning);
                db.todoDao().update(items.get(index));
            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editPlus() {
            timegap = 0;
            int curcount = items.get(index).getCurcount();
            if(items.get(index).getCurcount()<items.get(index).getCount()){
                curcount = curcount + 1;
            } else {
                curcount = 0;
            }
            int cur = curcount;

            new Thread(() -> {

                //나머지 항목 전부 일시 정지
                for(int i=0; i<index; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    db.todoDao().update(items.get(i));
                }
                for(int i=(index+1); i <items.size() ; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    db.todoDao().update(items.get(i));
                }

                items.get(index).setCurcount(cur);
                db.todoDao().update(items.get(index));

            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editMinus() {
            timegap = 0;
            int curcount = items.get(index).getCurcount() ;
            if(curcount > 0) {
                curcount = items.get(index).getCurcount() - 1;
            }
            int finalCurcount = curcount;
            new Thread(() -> {

                for(int i=0; i<index; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    db.todoDao().update(items.get(i));
                }
                for(int i=(index+1); i <items.size() ; i++){
                    if(items.get(i).getIsrunning()) items.get(i).setCurtime(lastsec-1);
                    db.todoDao().update(items.get(i));
                }

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
        hourPicker.setMaxValue(24);
        hourPicker.setValue(0);

        minPicker.setMinValue(0);
        minPicker.setMaxValue(60);
        minPicker.setValue(0);

        secPicker.setMinValue(0);
        secPicker.setMaxValue(60);
        secPicker.setValue(0);

        builder.setTitle("수정 시간 입력");
        builder.setMessage("하루의 목표 시간을 수정하세요");
        builder.setView(di);

        builder.setPositiveButton("입력",
                (dialog, which) -> {

                    @SuppressLint("DefaultLocale")
                    String result = String.format("%02d:%02d:%02d", hourPicker.getValue(), minPicker.getValue(), secPicker.getValue());
                    int totalsec = hourPicker.getValue()*60*60+minPicker.getValue()*60+secPicker.getValue();
                    //제목 입력, DB추가
                    textView.setText(result);
                    new Thread(() -> {
                        items.get(position).setHour(hourPicker.getValue());
                        items.get(position).setMin(minPicker.getValue());
                        items.get(position).setSec(secPicker.getValue());
                        items.get(position).setCount(0);
                        items.get(position).setTotalsec(totalsec);
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
        numberPicker.setMaxValue(100);
        //현재값 설정 (dialog를 실행했을 때 시작지점)
        numberPicker.setValue(1);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("횟수 수정");
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
                            db.todoDao().update(items.get(position));
                        }).start();
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    /*private void showPopupcycle(View v, final int position){

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
    }*/
}