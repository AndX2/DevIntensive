package com.softdesign.devintensive.ui.adapters;

/**
 * Created by savos on 21.07.2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
