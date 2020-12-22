package com.koko_plan.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koko_plan.R;
import com.koko_plan.sub.MySoundPlayer;

import java.util.ArrayList;

public class Ranking_Adapter extends RecyclerView.Adapter<Ranking_Adapter.ViewHolder>
{
    private Context context = null;
    private ArrayList<Ranking_Item> rankingItems = null;
    private Ranking_ViewListener rankingViewListener = null;
    private Context activity;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public static String itemclub, itemddate, itemlocation;
    public static int itemloft, itemhole;
    public static float itemdist;

    private String strClubtitle, strLocation;
    private int strSet, straver, strLoft;


    public Ranking_Adapter(ArrayList<Ranking_Item> items, Context context, Ranking_ViewListener listener)
    {
        this.rankingItems = items;
        this.context = context;
        this.rankingViewListener = listener;

        firebaseFirestore = FirebaseFirestore.getInstance();
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

        viewHolder.clubview.setText(rankingItems.get(i).getClub()+"");
        viewHolder.clubloft.setText("loft"+"\n"+rankingItems.get(i).getLoft()+"");
        if(rankingItems.get(i).getLoft() == 0) viewHolder.clubloft.setText("");
        viewHolder.clubaver.setText(String.format("%.1f", rankingItems.get(i).getAverage()));
        viewHolder.clubdist.setText(rankingItems.get(i).getSet()+"");
    }

    @Override
    public int getItemCount()
    {
        // 목록화 할 아이템의 개수 확인
        return rankingItems.size();
    }

    void addItem(Ranking_Item clubItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        rankingItems.add(clubItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // 리스트 목록의 초기화 .... 미연의 충돌 방지
        TextView clubview = null;
        TextView clubloft = null;
        TextView clubaver = null;
        TextView clubdist = null;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            clubview = (TextView)view.findViewById(R.id.ci_title);
            clubloft = (TextView)view.findViewById(R.id.ci_clubloft);
            clubaver = (TextView)view.findViewById(R.id.ci_clubavdistance);
            clubdist = (TextView)view.findViewById(R.id.ci_clubdistance);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    MySoundPlayer.play(MySoundPlayer.CLICK);

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        if(rankingViewListener != null) {
                            rankingViewListener.onItemClick(v, pos);
                        }

                        itemclub = rankingItems.get(pos).getClub();
                        itemloft = rankingItems.get(pos).getLoft();
                        itemdist = rankingItems.get(pos).getSet();

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