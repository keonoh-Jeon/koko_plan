package com.koko_plan.server.totalhabbits;

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
import com.koko_plan.sub.ItemTouchHelperListener;

import java.util.ArrayList;
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
        TextView timeview = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);
            tvtitle = (TextView)view.findViewById(R.id.tv_totalhabbitlistTitle);

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
                .collection("totalhabbits")
                .document(totalHabbitsList_items.get(i).getHabbittitle());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            text = (String) Objects.requireNonNull(document.getData()).get("habbittitle");
                            viewHolder.tvtitle.setText(text);
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