package com.koko_plan.server.detailhabbit;

import android.view.View;

public interface Detailhabbit_ViewListener
{
    void onItemClick(View view, int position);

    void onDelete();
    void onModify();


}