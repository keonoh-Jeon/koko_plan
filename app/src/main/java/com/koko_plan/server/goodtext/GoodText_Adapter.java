package com.koko_plan.server.goodtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.R;
import com.koko_plan.sub.CustomToastMaker;
import com.koko_plan.sub.ItemTouchHelperListener;
import com.koko_plan.sub.MySoundPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.name;

public class GoodText_Adapter extends RecyclerView.Adapter<GoodText_Adapter.ViewHolder> implements Filterable, ItemTouchHelperListener
{
    private Context context;
    private ArrayList<GoodText_Item> unfilterList;
    private ArrayList<GoodText_Item> filterList;
    private GoodText_ViewListener goodText_viewListener;
    private String text;

    public GoodText_Adapter(ArrayList<GoodText_Item> unfilterList, Context context, GoodText_ViewListener listener)
    {
        this.unfilterList  = unfilterList;
        this.filterList  = unfilterList;
        this.context = context;
        this.goodText_viewListener = listener;
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
        View backgraound = null;
        ImageView profileview = null;
        TextView textview = null;
        TextView timeview = null;
        TextView clubaver = null;
        TextView clubdist = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            backgraound = view.findViewById(R.id.view_goodtexts);
            profileview = (ImageView)view.findViewById(R.id.iv_profileimager);
            textview = (TextView)view.findViewById(R.id.tv_text);
            timeview = (TextView)view.findViewById(R.id.tv_time);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(goodText_viewListener != null) {
                            goodText_viewListener.onItemClick(v, pos);
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

        String imageUrl = filterList.get(i).getFrom();
        Glide.with(context)
                .load(imageUrl)
                .into(viewHolder.profileview);

        viewHolder.timeview.setText(filterList.get(i).getTime());

        String randomnum = String.valueOf(filterList.get(i).getRandomnum());
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
                            text = (String) document.get(randomnum);
                            viewHolder.textview.setText(text);
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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty()) {
                    filterList = unfilterList;
                } else {
                    ArrayList<GoodText_Item> filteringList = new ArrayList<>();

                    for (GoodText_Item item : unfilterList) {
                        if(item.getText().toLowerCase().contains(str))
                            filteringList.add(item);
                    }

                    filterList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterList = (ArrayList<GoodText_Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount()
    {   // 목록화 할 아이템의 개수 확인
        return filterList.size();
    }

    void addItem(GoodText_Item clubItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        filterList.add(clubItem);
    }

}