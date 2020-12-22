package com.koko_plan.server;

import android.view.View;

public interface Ranking_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}