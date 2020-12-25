package com.koko_plan.server.goodtext;

import android.view.View;

public interface GoodText_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}