package com.koko_plan.sub;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
}
