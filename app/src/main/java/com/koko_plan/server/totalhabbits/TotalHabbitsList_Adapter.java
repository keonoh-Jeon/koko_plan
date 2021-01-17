package com.koko_plan.server.totalhabbits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koko_plan.R;
import com.koko_plan.sub.ItemTouchHelperListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;

public class TotalHabbitsList_Adapter extends RecyclerView.Adapter<TotalHabbitsList_Adapter.ViewHolder> implements ItemTouchHelperListener
{
    private Context context;
    private ArrayList<TotalHabbitsList_Item> totalHabbitsList_items;
    private TotalHabbitsList_ViewListener totalHabbitsList_viewListener;
    private String text;

    public TotalHabbitsList_Adapter(ArrayList<TotalHabbitsList_Item> totalHabbitsList_items, Context context, TotalHabbitsList_ViewListener listener)
    {
        this.totalHabbitsList_items  = totalHabbitsList_items;
        this.context = context;
        this.totalHabbitsList_viewListener = listener;
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지
        TextView tvtitle = null;
        TextView tvroutine = null;
        TextView tvstartdate = null;
        TextView tv1count = null;
        TextView tv1counttime = null;
        TextView tvcountsum = null;
        TextView tvcurtimesum = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);
            tvtitle = (TextView)view.findViewById(R.id.tv_totalhabbitlistTitle);
            tvroutine = (TextView)view.findViewById(R.id.tv_routine);
            tvstartdate = (TextView)view.findViewById(R.id.tv_startdate);
            tv1count = (TextView)view.findViewById(R.id.tv_1count);
            tv1counttime = (TextView)view.findViewById(R.id.tv_1counttime);
            tvcountsum = (TextView)view.findViewById(R.id.tv_countsum);
            tvcurtimesum = (TextView)view.findViewById(R.id.tv_curtimesum);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(totalHabbitsList_viewListener != null) {
                            totalHabbitsList_viewListener.onItemClick(v, pos);
                        }

                        /*itemclub = rankingItems.get(pos).getClub();
                        itemloft = rankingItems.get(pos).getLoft();
                        itemdist = rankingItems.get(pos).getSet();*/

                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_totalhabbitslist_item, viewGroup, false);
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

    //뷰 홀더 :리스트에 나타내는 항목의 내용을 세팅
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(firebaseUser.getUid())
                .collection("total")
                .document(totalHabbitsList_items.get(i).getHabbittitle());

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

                            viewHolder.tvtitle.setText((String) Objects.requireNonNull(document.getData()).get("habbittitle"));

                            if(Objects.equals(document.getData().get("habbitroutine"), "매일")) {
                                viewHolder.tvroutine.setText((String) document.getData().get("habbitroutine"));
                            } else {
                                String routine = "매주 ";
                                if((Boolean) document.getData().get("monday")) {
                                    routine = routine + " 월";
                                }
                                if((Boolean) document.getData().get("tuesday")) {
                                    routine = routine + " 화";
                                }
                                if((Boolean) document.getData().get("wednesday")) {
                                    routine = routine + " 수";
                                }
                                if((Boolean) document.getData().get("thursday")) {
                                    routine = routine + " 목";
                                }
                                if((Boolean) document.getData().get("friday")) {
                                    routine = routine + " 금";
                                }
                                if((Boolean) document.getData().get("saturday")) {
                                    routine = routine + " 토";
                                }
                                if((Boolean) document.getData().get("sunday")) {
                                    routine = routine + " 일";
                                }
                                viewHolder.tvroutine.setText(routine);
                            }

                            long count = (long) document.getData().get("count");
                            viewHolder.tv1count.setText(Long.toString(count)+"회");

                            long h = (long) document.getData().get("totalsec") / 60 / 60;
                            long m = (long) document.getData().get("totalsec") / 60 % 60;
                            long s = (long) document.getData().get("totalsec") % 60;
                            viewHolder.tv1counttime.setText(String.format("목표 " + "%02d:%02d:%02d", h, m, s));

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {   // 목록화 할 아이템의 개수 확인
        return totalHabbitsList_items.size();
    }

    void addItem(TotalHabbitsList_Item totalHabbitsList_item) {
        // 외부에서 item을 추가시킬 함수입니다.
        totalHabbitsList_items.add(totalHabbitsList_item);
    }



}