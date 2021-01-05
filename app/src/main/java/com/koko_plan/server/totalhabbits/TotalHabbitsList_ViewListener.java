package com.koko_plan.server.totalhabbits;

import android.view.View;

public interface TotalHabbitsList_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}