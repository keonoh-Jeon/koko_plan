package com.koko_plan.main;

import android.view.View;

public interface TodoList_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}