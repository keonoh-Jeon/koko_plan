package com.koko_plan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Memo> items = new ArrayList<>();
    private Context mContext;
    private MemoDatabase db;

    public RecyclerAdapter(MemoDatabase db) {
        this.db = db;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Memo> getItems() {return items;}

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        mContext = viewGroup.getContext();
        final ViewHolder holder = new ViewHolder(v);
        v.findViewById(R.id.tvCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MySoundPlayer.play(MySoundPlayer.CLICK);
                showPopup(v, holder.getAdapterPosition());
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.onBind(items.get(position),position);

    }

    private void showPopup(View v, final int position){

        @SuppressLint("RestrictedApi") PopupMenu popup= new PopupMenu(mContext.getApplicationContext(), v);//v는 클릭된 뷰를 의미
        popup.getMenuInflater().inflate(R.menu.cycle_menu, popup.getMenu());

        final TextView clubtitle = (TextView) v.findViewById(R.id.tvCycle);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.once:
                        clubtitle.setText("한번");
                        new Thread(() -> {
                            items.get(position).setCycle("한번");
                            db.memoDao().update(items.get(position));
                        }).start();
                        Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.everytime:

                        break;
                    case R.id.everyday:

                        break;
                    case R.id.everyweek:

                        break;
                    case R.id.everymonth:

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvContents;
        private Button btnSave;
        private int index;

        public ViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvContents = view.findViewById(R.id.tvContents);
            btnSave = view.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(v -> editData(tvContents.getText().toString()));

        }
        public void onBind(Memo memo, int position){
            index = position;
            tvTitle.setText(memo.getTitle());
            tvContents.setText(memo.getContents());
        }

        public void editData(String contents){
            new Thread(() -> {
                items.get(index).setContents(contents);
                db.memoDao().update(items.get(index));
            }).start();
            Toast.makeText(mContext,"저장완료", Toast.LENGTH_SHORT).show();
        }
    }

    public void setItem(List<Memo> data) {
        items = data;
        notifyDataSetChanged();
    }

}