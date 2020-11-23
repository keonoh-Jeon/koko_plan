package com.koko_plan;

import android.view.View;

public interface TodoListViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}