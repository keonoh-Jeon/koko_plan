package com.koko_plan.server.ranking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koko_plan.R;
import com.koko_plan.server.goodtext.SetMsgToUsers;
import com.koko_plan.sub.CustomToastMaker;
import com.koko_plan.sub.MySoundPlayer;
import com.koko_plan.server.goodtext.RandomGoodText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.koko_plan.main.MainActivity.firebaseFirestore;
import static com.koko_plan.main.MainActivity.firebaseUser;
import static com.koko_plan.main.MainActivity.name;
import static com.koko_plan.main.MainActivity.todaydate;

public class Ranking_Adapter extends RecyclerView.Adapter<Ranking_Adapter.ViewHolder> implements Filterable
{
    private Context context ;
    private ArrayList<Ranking_Item> unfilterList;
    private ArrayList<Ranking_Item> filterList;
    private Ranking_ViewListener rankingViewListener;

    public Ranking_Adapter(ArrayList<Ranking_Item> unfilterList, Context context, Ranking_ViewListener listener)
    {
        this.unfilterList  = unfilterList;
        this.filterList  = unfilterList;
        this.context = context;
        this.rankingViewListener = listener;
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

        viewHolder.numberview.setText((i+1)+".");
        viewHolder.nameview.setText(filterList.get(i).getName());

//        viewHolder.nameview.setText(filteredList.get(i).getName()+"");
        DocumentReference documentReference = firebaseFirestore
                .collection("users")
                .document(filterList.get(i).getId());
        Log.e(TAG, "onBindViewHolder: "  + firebaseUser.getUid() );

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.e(TAG, "onComplete: "+ document.get(todaydate));
                            viewHolder.progressview.setText(document.get(todaydate)+"%");
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
        return filterList.size();
    }

    void addItem(Ranking_Item clubItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        filterList.add(clubItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지
        TextView numberview = null;
        TextView nameview = null;
        TextView progressview = null;
        TextView clubaver = null;
        TextView clubdist = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            numberview = (TextView)view.findViewById(R.id.rk_number);
            nameview = (TextView)view.findViewById(R.id.rk_name);
            progressview = (TextView)view.findViewById(R.id.rk_progress);

            view.setOnClickListener(new View.OnClickListener() {

                private Date date;
                private String time;
                private SimpleDateFormat timeformat;

                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(View v) {

                    MySoundPlayer.play(MySoundPlayer.CLICK);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(rankingViewListener != null) {
                            rankingViewListener.onItemClick(v, pos);
                        }

                        // 현재 날짜 구하기
                        date = new Date();
                        //날짜 표시 형식 지정
                        timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        time = timeformat.format(date);

                        String text = RandomGoodText.make(context, filterList.get(pos).getId(), time)+ "\n- "+ name;
//                        Log.e(TAG, "onClick: make"+ text + "to " + filterList.get(pos).getId());

                        getItemId();

                        //커스텀 토스트 메시지 띄우기
//                        CustomToastMaker.show(context, text);

//                        SetMsgToUsers.send(text, filterList.get(pos).getId());

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

    private void showPopup(View v, final int position){

        /*@SuppressLint("RestrictedApi") PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.post, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.modify:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        // 다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용합니다.

                        @SuppressLint("ResourceType") View v = LayoutInflater.from(context).inflate(R.layout.edit_box3, null, false);
                        builder.setView(v);

                        final Button ButtonSubmit = (Button) v.findViewById(R.id.button_dialog_submit);

                        final TextView clubtitle = (TextView) v.findViewById(R.id.ebtv_clubtitle);
                        final EditText clubset = (EditText) v.findViewById(R.id.et_clubset);
                        final EditText clubloft = (EditText) v.findViewById(R.id.ebet_loft);
                        final EditText clubaver = (EditText) v.findViewById(R.id.et_clubaver);

                        // 6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줍니다.
                        clubtitle.setText(clubItems.get(position).getClub());
                        clubset.setText(clubItems.get(position).getSet()+"");
                        clubloft.setText(clubItems.get(position).getLoft()+"");
                        clubaver.setText(String.format("%.0f", clubItems.get(position).getAverage())+"");

                        final AlertDialog dialog = builder.create();

                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {

                            // 7. 수정 버튼을 클릭하면 현재 UI에 입력되어 있는 내용으로

                            public void onClick(View v) {

                                strClubtitle = clubtitle.getText().toString();
                                strSet = Integer.parseInt(clubset.getText().toString());

                                if(clubloft.getText().toString().equals("")) { strLoft = 0;
                                } else { strLoft = Integer.parseInt(clubloft.getText().toString()); }

                                straver = Integer.parseInt(clubaver.getText().toString());

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String, Object> set = new HashMap<>();
                                set.put("club", strClubtitle);
                                set.put("set", strSet);
                                set.put("loft", strLoft);
                                set.put("average", straver);

                                db. collection("users")
                                        .document(firebaseUser.getUid())
                                        .collection("clubs")
                                        .document(strClubtitle)
                                        .set(set)

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("data is saved", "Document success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("data is not saved", "Document Error!!");
                                            }
                                        });


                                Club_Item item = new Club_Item();
                                item.setClub(strClubtitle);
                                item.setSet(strSet);
                                item.setLoft(strLoft);
                                item.setAverage(straver);

                                // 8. ListArray에 있는 데이터를 변경하고
                                clubItems.set(position, item);
                                notifyItemChanged(position, item);

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;

                    case R.id.delete:

                        firebaseFirestore
                                .collection("users")
                                .document(firebaseUser.getUid())
                                .collection("clubs")
                                .document(clubItems.get(position).getClub())

                                .delete()

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        clubItems.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(context,"club deleted.",Toast.LENGTH_SHORT).show();

                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"club not be deleted.",Toast.LENGTH_SHORT).show();
                                    }
                                });

                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();*/
    }



}