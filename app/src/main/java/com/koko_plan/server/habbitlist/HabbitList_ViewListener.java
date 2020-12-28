package com.koko_plan.server.habbitlist;

import android.view.View;

public interface HabbitList_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}