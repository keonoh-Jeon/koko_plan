package com.koko_plan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder>
{
    private Context context = null;
    private ArrayList<Todo_list_Item> todoListItems = null;

    public TodoListAdapter(ArrayList<Todo_list_Item> items, Context context, TodoListViewListener listener)
    {
        this.todoListItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main, viewGroup, false);
        final ViewHolder holder = new ViewHolder(v);
        v.findViewById(R.id.todo_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*MySoundPlayer.play(MySoundPlayer.CLICK);
                showPopup(v, holder.getAdapterPosition());*/
            }
        });

        return holder;
    }

    //뷰 홀더 :리스트에 나타내는 항목의 내용을 세팅
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tvdate.setText(todoListItems.get(i).getDate()+"");
        viewHolder.tvset.setText("set: "+todoListItems.get(i).getSet()+"");
        viewHolder.tvhole.setText("H: "+todoListItems.get(i).getHole()+"");
        viewHolder.tvshotheight.setText(String.format("%.1f", todoListItems.get(i).getShotheight()) + "h");
        viewHolder.tvgreenresult.setText(todoListItems.get(i).getGreen());
    }

    @Override
    public int getItemCount()
    {
        return todoListItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tvdate;
        TextView tvset;
        TextView tvhole;
        TextView tvshotheight;
        TextView tvdistance;
        TextView tvgreenresult;

        // 뷰홀더의 텍스트 및 이미지 연결 : xml 연결
        ViewHolder(View view) {
            super(view);

            tvdate = (TextView)view.findViewById(R.id.chitv_date);
            tvset = (TextView)view.findViewById(R.id.chitv_set);
            tvhole = (TextView)view.findViewById(R.id.chitv_holecount);
            tvshotheight = (TextView)view.findViewById(R.id.chitv_shotheight);
            tvdistance = (TextView)view.findViewById(R.id.chitv_distance);
            tvgreenresult = (TextView)view.findViewById(R.id.chitv_green);
        }

        @Override
        public void onClick(View v) {

        }
    }

    /*private void showPopup(View v, final int position){

        @SuppressLint("RestrictedApi") PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.post8, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.delete:
                        firebaseFirestore
                                .collection("users")
                                .document(firebaseUser.getUid())
                                .collection(clubHistoryItems.get(position).getClub())
                                .document(clubHistoryItems.get(position).getDate() + "(" + clubHistoryItems.get(position).getSet() + "-" + clubHistoryItems.get(position).getHole() + "-" + clubHistoryItems.get(position).getShot() + ")")

                                .delete()

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        clubHistoryItems.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, clubHistoryItems.size());
                                        Toast.makeText(context,"club history deleted.",Toast.LENGTH_SHORT).show();
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"club history not be deleted.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }*/
}