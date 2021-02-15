package com.koko_plan.server.ranking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.koko_plan.R;
import com.koko_plan.main.TodoList_Item;
import com.koko_plan.sub.MySoundPlayer;
import com.koko_plan.server.goodtext.RandomGoodText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.editor;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.lastsec;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.todaydate;

public class Ranking_Adapter extends RecyclerView.Adapter<Ranking_Adapter.ViewHolder> implements Filterable
{
    private Context context ;
    private ArrayList<Ranking_Item> unfilterList;
    private ArrayList<Ranking_Item> filterList;
    private Ranking_ViewListener rankingViewListener;

    private ProgressDialog pd;
    private int myrank;

    public Ranking_Adapter(ArrayList<Ranking_Item> unfilterList, Context context, Ranking_ViewListener listener)
    {
        this.unfilterList  = unfilterList;
        this.filterList  = unfilterList;
        this.context = context;
        this.rankingViewListener = listener;

        //로딩 화면 만들기
        pd = null;
        pd = ProgressDialog.show(context, "순위 불러 오는 중......", "잠시만 기다려 주세요.");
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
                    ArrayList<Ranking_Item> filteringList = new ArrayList<>();

                    for (Ranking_Item item : unfilterList) {
                        if(item.getName().toLowerCase().contains(str))
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
                filterList = (ArrayList<Ranking_Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_ranking_item, viewGroup, false);
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

        viewHolder.numberview.setText((i+1)+"");
        showrankername((i+1)/(double)filterList.size()*100.0, viewHolder);

        viewHolder.nameview.setText(filterList.get(i).getName());
        long target = (long) filterList.get(i).getTodaytarget();
        long second = (long) target % 60;
        long minute = ((long) target / 60) % 60;
        long hour = ((long) target / 3600) % 24;
        viewHolder.totalsecview.setText(String.format("%02d:%02d:%02d", hour, minute, second));
        viewHolder.tvgetcountview.setText(filterList.get(i).getGetcount()+"");
        viewHolder.progressview.setText(filterList.get(i).getProgress()+"%");

        if (pd!= null) pd.dismiss();
    }

    @SuppressLint("SetTextI18n")
    private void showrankername(double rankscore, ViewHolder viewHolder) {
        if(99.94 < rankscore && rankscore <= 100) {
            viewHolder.tvrankerview.setText("Iron IV");
        } else if(99.64 < rankscore && rankscore <= 99.94) {
            viewHolder.tvrankerview.setText("Iron III");
        } else if (98.94 < rankscore && rankscore <= 99.64) {
            viewHolder.tvrankerview.setText("Iron II");
        } else if (97.93 < rankscore && rankscore <= 98.94) {
            viewHolder.tvrankerview.setText("Iron I");
        } else if (95.53 < rankscore && rankscore <= 97.93) {
            viewHolder.tvrankerview.setText("Bronze IV");
        } else if (92.78 < rankscore && rankscore <= 95.53) {
            viewHolder.tvrankerview.setText("Bronze III");
        } else if (88.73 < rankscore && rankscore <= 92.78) {
            viewHolder.tvrankerview.setText("Bronze II");
        } else if (82.76 < rankscore && rankscore <= 88.73) {
            viewHolder.tvrankerview.setText("Bronze I");
        } else if (73.61 < rankscore && rankscore <= 82.76) {
            viewHolder.tvrankerview.setText("Silver IV");
        } else if (66.31 < rankscore && rankscore <= 73.61) {
            viewHolder.tvrankerview.setText("Silver III");
        } else if (57.53 < rankscore && rankscore <= 66.31) {
            viewHolder.tvrankerview.setText("Silver II");
        } else if (50.21 < rankscore && rankscore <= 57.53) {
            viewHolder.tvrankerview.setText("Silver I");
        } else if (36.76 < rankscore && rankscore <= 50.21) {
            viewHolder.tvrankerview.setText("Gold IV");
        } else if (29.14 < rankscore && rankscore <= 36.76) {
            viewHolder.tvrankerview.setText("Gold III");
        } else if (22.53 < rankscore && rankscore <= 29.14) {
            viewHolder.tvrankerview.setText("Gold II");
        } else if (18.36 < rankscore && rankscore <= 22.53) {
            viewHolder.tvrankerview.setText("Gold I");
        } else if (10.58 < rankscore && rankscore <= 18.36) {
            viewHolder.tvrankerview.setText("Platinum IV");
        } else if (7.58 < rankscore && rankscore <= 10.58) {
            viewHolder.tvrankerview.setText("Platinum III");
        } else if (5.59 < rankscore && rankscore <= 7.58) {
            viewHolder.tvrankerview.setText("Platinum II");
        } else if (3.67 < rankscore && rankscore <= 5.59) {
            viewHolder.tvrankerview.setText("Platinum I");
        } else if (1.45 < rankscore && rankscore <= 3.67) {
            viewHolder.tvrankerview.setText("Diamond IV");
        } else if (0.68 < rankscore && rankscore <= 1.45) {
            viewHolder.tvrankerview.setText("Diamond III");
        } else if (0.31 < rankscore && rankscore <= 0.68) {
            viewHolder.tvrankerview.setText("Diamond II");
        } else if (0.11 < rankscore && rankscore <= 0.31) {
            viewHolder.tvrankerview.setText("Diamond I");
        } else if (0.06 < rankscore && rankscore <= 0.11) {
            viewHolder.tvrankerview.setText("Master");
        } else if (0.02 < rankscore && rankscore <= 0.06) {
            viewHolder.tvrankerview.setText("G_Master");
        } else if (0 < rankscore && rankscore <= 0.02) {
            viewHolder.tvrankerview.setText("Challenger");
        }
    }

    @Override
    public int getItemCount()
    {   // 목록화 할 아이템의 개수 확인
        return filterList.size();
    }

    void addItem(Ranking_Item ranking_item) {
        // 외부에서 item을 추가시킬 함수입니다.
        filterList.add(ranking_item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지
        TextView numberview = null;
        TextView tvrankerview = null;
        TextView nameview = null;
        TextView totalsecview = null;
        TextView progressview = null;
        TextView tvgetcountview = null;
        ImageView ivlikeitview = null;

        TextView clubaver = null;
        TextView clubdist = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            numberview = (TextView)view.findViewById(R.id.rk_number);
            tvrankerview = (TextView)view.findViewById(R.id.tv_ranker);
            nameview = (TextView)view.findViewById(R.id.rk_name);
            totalsecview = (TextView)view.findViewById(R.id.rk_totalsec);
            progressview = (TextView)view.findViewById(R.id.rk_progress);
            tvgetcountview = (TextView)view.findViewById(R.id.tv_getcount);
            ivlikeitview = (ImageView) view.findViewById(R.id.iv_cheerup);

            ivlikeitview.setOnClickListener(new View.OnClickListener() {

                private Date date;
                private String time, day;
                private SimpleDateFormat timeformat, dayformat;

                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(View v) {

                    MySoundPlayer.play(MySoundPlayer.POP);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(rankingViewListener != null) {
                            rankingViewListener.onItemClick(v, pos);
                        }

                        if(!filterList.get(pos).getId().equals(firebaseUser.getUid())) {
                            // 현재 날짜 구하기
                            date = new Date();
                            //날짜 표시 형식 지정
                            timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dayformat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                            time = timeformat.format(date);
                            day = dayformat.format(date);

                            RandomGoodText.make(context, filterList.get(pos).getId(), day, time, name);
                            filterList.get(pos).setGetcount(filterList.get(pos).getGetcount()+1);

                            Map<String, Object> data = new HashMap<>();
                            data.put("getcount", filterList.get(pos).getGetcount());
                            firebaseFirestore
                                    .collection("users")
                                    .document(filterList.get(pos).getId())
                                    .set(data, SetOptions.merge());

                            getItemId();
                        } else {
                            Toast.makeText(context, "본인에게는 선물이 불가합니다.", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
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

}