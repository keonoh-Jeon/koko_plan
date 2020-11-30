package com.koko_plan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.app.Fragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class MyTimerFragment extends Fragment {

    Timer mTimer;
    Handler mHandler = new Handler();
    private TextView mTextView;
    long mStartTime;

    private MemoDatabase db;
    private List<Memo> items = new ArrayList<>();

    CustomAdapter ca;
    private Context context;

    class CustomAdapter extends BaseAdapter {

        public void setItem(List<Memo> data) {
            items = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @SuppressLint("ViewHolder")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Activity root = getActivity(); //이 클래스가 프레그먼트이기 때문에 액티비티 정보를 얻는다.
            Toast.makeText(root,"getView" , Toast.LENGTH_SHORT).show();

            //커스텀뷰에 있는 객체들 가져오기
            convertView = getLayoutInflater().inflate(R.layout.fragment_main,null);
            TextView tName = (TextView)convertView.findViewById(R.id.tvTitle);
            /*TextView tDate = (TextView)convertView.findViewById(R.id.textView_date);
            TextView tContent = (TextView)convertView.findViewById(R.id.textView_content);*/

            tName.setText((CharSequence) items.get(position));
            /*tDate.setText(dates.get(position));
            tContent.setText(contents.get(position));*/
            return convertView;
        }
    }



    //제일 먼저 호출
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayTodos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container,false);
        final TextView tvTitle = rootView.findViewById(R.id.tvTitle);

        if(savedInstanceState != null){
            tvTitle.setText(savedInstanceState.getString("title"));
        }

        rootView.findViewById(R.id.tvTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);

                MyAlertDialogFragment dialog = MyAlertDialogFragment.newInstance(new MyAlertDialogFragment.TitleInputListener() {
                    @Override
                    public void onTitleInputComplete(String name) {
                        if(name != null) {
                        tvTitle.setText(name);
                        }
                    }
                });
                         dialog.show(getFragmentManager(), "addDialog");
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        mTextView = (TextView)view.findViewById(R.id.textView1);

        mStartTime = SystemClock.elapsedRealtime();

        mTimer = new Timer(true);
        mTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        long total = SystemClock.elapsedRealtime() - mStartTime;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat f = new SimpleDateFormat("mm:ss");
                        String s = f.format(new Date(total)) + "." + ((int)(total % 1000) / 100);
                        mTextView.setText(s);
                    }
                });
            }
        }, 0, 50);

        Button button = (Button)view.findViewById(R.id.buttonStopDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimer != null){
                    mTimer.cancel();
                    mTimer = null;
                }
                else{
                    MyTimerFragment.this.getFragmentManager().beginTransaction().remove(MyTimerFragment.this).commit();
                }
            }
        });
    }

    //감사 리스트 호출 함수
    public void displayTodos(){

        db = MemoDatabase.getDatabase(context);

        db.memoDao().getAll().observe((LifecycleOwner) this, new Observer<List<Memo>>() {
            @Override
            public void onChanged(List<Memo> data) {
                ca.setItem(data);
            }
        });
    }
}
