package com.koko_plan.server.habbitlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koko_plan.R;
import com.koko_plan.server.goodtext.GoodText_Item;
import com.koko_plan.server.goodtext.GoodText_ViewListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;

public class HabbitList_Adapter extends RecyclerView.Adapter<HabbitList_Adapter.ViewHolder>
{
    private Context context;
    private ArrayList<HabbitList_Item> habbitListItems;
    private HabbitList_ViewListener habbitListViewListener;
    private String text;

    public HabbitList_Adapter(ArrayList<HabbitList_Item> habbitListItems, Context context, HabbitList_ViewListener listener)
    {
        this.habbitListItems  = habbitListItems;
        this.context = context;
        this.habbitListViewListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지
        TextView tvtitle = null;
        TextView timeview = null;
        TextView clubaver = null;
        TextView clubdist = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            tvtitle = (TextView)view.findViewById(R.id.tv_Title);
            timeview = (TextView)view.findViewById(R.id.tv_time);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(habbitListViewListener != null) {
                            habbitListViewListener.onItemClick(v, pos);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_goodtexts_item, viewGroup, false);
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

//        viewHolder.backgraound.setBackgroundColor(Color.GRAY);

        viewHolder.timeview.setText(habbitListItems.get(i).getTitle());

//        String randomnum = String.valueOf(filterList.get(i).getRandomnum());
        DocumentReference documentReference = firebaseFirestore
                .collection("randomsource")
                .document("goodtexts");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            text = (String) document.get(randomnum);
//                            viewHolder.textview.setText(text);
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
        return habbitListItems.size();
    }

}