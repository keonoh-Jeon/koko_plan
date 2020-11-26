package com.koko_plan;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
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

import android.os.Handler;
import android.os.Message;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Memo> items = new ArrayList<>();
    private Context mContext;
    private MemoDatabase db;
    private boolean isRunning;

    public RecyclerAdapter(MemoDatabase db) {
        this.db = db;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Memo> getItems() {return items;}

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        mContext = viewGroup.getContext();
        final ViewHolder holder = new ViewHolder(v);
        v.findViewById(R.id.tvCycle).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                showPopupcycle(v, holder.getAdapterPosition());
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
        /*v.findViewById(R.id.ivplaybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                plusplaycount(v, holder.getAdapterPosition());
            }
        });*/

        return holder;
    }

    @SuppressLint("SetTextI18n")
    private void makedialogtitle(View v, final int position){

        final TextView tvTitle = (TextView) v. findViewById(R.id.tvTitle);

        final EditText edittext = new EditText(mContext);
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
                        db.memoDao().update(items.get(position));
                    }).start();

                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.onBind(items.get(position),position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View v_item;
        private TextView tvTitle;
        private TextView tvCycle;
        private TextView tvTime;
        private Button btnSave;
        private TextView tvplayCount;
        private ImageView ivbtnreset, ivbdelete;

        private Button mStartBtn, mStopBtn, mRecordBtn, mPauseBtn;
        private TextView mTimeTextView, mRecordTextView;
        private Thread timeThread = null;
        private Boolean isRunning = true;

        private int index;

        public ViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvCycle = view.findViewById(R.id.tvCycle);
            tvTime = view.findViewById(R.id.tvTime);
            ivbtnreset = view.findViewById(R.id.ivbnt_reset);

            btnSave = view.findViewById(R.id.btnSave);

//            ivbtnreset.setOnClickListener(v -> editPlayPluscount());

            mStartBtn = view.findViewById(R.id.btn_start);
            mStopBtn = view.findViewById(R.id.btn_stop);
            mRecordBtn = view.findViewById(R.id.btn_record);
            mPauseBtn = view.findViewById(R.id.btn_pause);
            mTimeTextView = view.findViewById(R.id.timeView);

            v_item = view.findViewById(R.id.viewitem);
            v_item.setOnClickListener(v -> editPlayMinuscount());

            ivbdelete = view.findViewById(R.id.ivbnt_delete);
            ivbdelete.setOnClickListener(v -> itemdelete());

            mStartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    mStopBtn.setVisibility(View.VISIBLE);
                    mRecordBtn.setVisibility(View.VISIBLE);
                    mPauseBtn.setVisibility(View.VISIBLE);

                    timeThread = new Thread(new timeThread());
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
            });
        }

        public class timeThread implements Runnable {

            @Override
            public void run() {
                int i = 0;

                while (true) {
                    while (isRunning) { //일시정지를 누르면 멈춤
                        Message msg = new Message();
                        msg.arg1 = i--;
                        handler.sendMessage(msg);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    mTimeTextView.setText("");
                                    mTimeTextView.setText("00:00:00:00");
                                }
                            });
                            return; // 인터럽트 받을 경우 return
                        }
                    }
                }
            }
        }

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int mSec = msg.arg1 % 100;
                int sec = (msg.arg1 / 100) % 60;
                int min = (msg.arg1 / 100) / 60;
                int hour = (msg.arg1 / 100) / 360;
                //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

                @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
                /*if (result.equals("00:01:15:00")) {
                    Toast.makeText(MainActivity.this, "1분 15초가 지났습니다.", Toast.LENGTH_SHORT).show();
                }*/
                tvTime.setText(result);
            }
        };



        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void onBind(Memo memo, int position){
            index = position;
            tvTitle.setText(memo.getTitle());
            tvCycle.setText(memo.getCycle());

            if(memo.getTime()==0){
                tvTime.setText(String.format("%02d:%02d:%02d", memo.getHour(),memo.getMin(),memo.getSec()));
            } else {
                tvTime.setText(memo.getTime()+ "");
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
            timeThread.interrupt();
            new Thread(() -> {
                db.memoDao().delete(items.get(index));
            }).start();
        }

        @SuppressLint("SetTextI18n")
        public void editPlayMinuscount() {
            if (items.get(index).getTime() > 0) {
                int playcount = items.get(index).getTime() - 1;
                tvTime.setText(playcount + "");
                new Thread(() -> {
                    items.get(index).setTime(playcount);
                    db.memoDao().update(items.get(index));
                }).start();
            } else {
                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        }
    }



    private void runOnUiThread(Runnable runnable) {
    }

    public void setItem(List<Memo> data) {
        items = data;
        notifyDataSetChanged();
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
                        db.memoDao().update(items.get(position));
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
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                /*TextView tv = findViewById(R.id.text_send);
                tv.setText(nPicker.getValue() + " m");*/
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
                            items.get(position).setTime(numberPicker.getValue());;
                            db.memoDao().update(items.get(position));
                        }).start();
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                    //취소버튼 클릭
                });
        builder.show();
    }

    private void showPopupcycle(View v, final int position){

        @SuppressLint("RestrictedApi") PopupMenu popup= new PopupMenu(mContext.getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.cycle_menu, popup.getMenu());

        final TextView cycle = (TextView) v.findViewById(R.id.tvCycle);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.once:
                        editcycle("한번");
                        break;
                    case R.id.everytime:
                        editcycle("매시");
                        break;
                    case R.id.everyday:
                        editcycle("매일");
                        break;
                    case R.id.everyweek:
                        editcycle("매주");
                        break;
                    case R.id.everymonth:
                        editcycle("매월");
                        break;
                    default:
                        break;
                }
                return false;
            }

            public void editcycle(String s){
                cycle.setText(s);
                new Thread(() -> {
                    items.get(position).setCycle(s);
                    db.memoDao().update(items.get(position));
                }).start();
                Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show();
            }
        });
        popup.show();
    }


}