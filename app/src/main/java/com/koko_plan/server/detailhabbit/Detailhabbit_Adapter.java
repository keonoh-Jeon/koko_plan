package com.koko_plan.server.detailhabbit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koko_plan.R;
import com.koko_plan.server.goodtext.RandomGoodText;
import com.koko_plan.server.ranking.Ranking_Item;
import com.koko_plan.server.ranking.Ranking_ViewListener;
import com.koko_plan.sub.MySoundPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.todaydate;

public class Detailhabbit_Adapter extends RecyclerView.Adapter<Detailhabbit_Adapter.ViewHolder>
{
    private Context context ;
    private ArrayList<Detailhabbit_Item> detailhabbitItems;
    private Detailhabbit_ViewListener detailhabbitViewListener;

    private ProgressDialog pd;

    public Detailhabbit_Adapter(ArrayList<Detailhabbit_Item> detailhabbitItems, Context context, DetailHabbit listener)
    {
        this.detailhabbitItems  = detailhabbitItems;
        this.context = context;
        this.detailhabbitViewListener = listener;

        //로딩 화면 만들기
        pd = null;
        pd = ProgressDialog.show(context, "타임라인 불러 오는 중......", "잠시만 기다려 주세요.");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detailhabbits_item_firebase, viewGroup, false);
        final ViewHolder holder = new ViewHolder(v);

        /*v.findViewById(R.id.ci_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySoundPlayer.play(MySoundPlayer.CLICK);
                showPopup(v, holder.getAdapterPosition());
            }
        });*/

        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지

        TextView habbitdate = null;
        TextView tvcountsum = null;
        TextView tvcurtimesum = null;
        TextView tvroutine = null;
        TextView tvstartdate = null;
        TextView tv1count = null;
        TextView tv1counttime = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            habbitdate = (TextView)view.findViewById(R.id.tv_habbitdate);
            tvcountsum = (TextView)view.findViewById(R.id.tv_countsum);
            tvcurtimesum = (TextView)view.findViewById(R.id.tv_curtimesum);
            tvroutine = (TextView)view.findViewById(R.id.tv_routine);
            tvstartdate = (TextView)view.findViewById(R.id.tv_startdate);
            tv1count = (TextView)view.findViewById(R.id.tv_1count);
            tv1counttime = (TextView)view.findViewById(R.id.tv_1counttime);
        }

        @Override
        public void onClick(View v) {

        }
    }

    //뷰 홀더 :리스트에 나타내는 항목의 내용을 세팅
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.habbitdate.setText(detailhabbitItems.get(i).getDate());

        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid())
                .collection("total")
                .document(detailhabbitItems.get(i).getHabbittitle());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            viewHolder.tvstartdate.setText("시작 일자: "+ document.getData().get("start"));
                            viewHolder.tvcountsum.setText("총 "+ document.getData().get("countsum")+"회");

                            long curtimesum = (long) document.getData().get("curtimesum");
                            long second = curtimesum % 60;
                            long minute = (curtimesum / 60) % 60;
                            long hour = (curtimesum / 3600) % 24;
                            viewHolder.tvcurtimesum.setText(String.format("%02d:%02d:%02d", hour, minute, second));

                            long count = (long) document.getData().get("count");
                            viewHolder.tv1count.setText(Long.toString(count)+"회 씩");

                            long h = (long) document.getData().get("hour");
                            long m = (long) document.getData().get("min");
                            long s = (long) document.getData().get("sec");
                            viewHolder.tv1counttime.setText(String.format("%02d:%02d:%02d", h, m, s));



                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        if (pd!= null) pd.dismiss();
    }

    @Override
    public int getItemCount()
    {   // 목록화 할 아이템의 개수 확인
        return detailhabbitItems.size();
    }

    void addItem(Detailhabbit_Item detailhabbitItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        detailhabbitItems.add(detailhabbitItem);
    }

}