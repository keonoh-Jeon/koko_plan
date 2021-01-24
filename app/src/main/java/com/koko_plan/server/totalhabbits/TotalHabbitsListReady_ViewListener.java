package com.koko_plan.server.totalhabbits;

import android.view.View;

public interface TotalHabbitsListReady_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}