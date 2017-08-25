package com.jamieadkins.commonutils.ui;

/**
 * Created by jamiea on 25/02/17.
 */

public interface RecyclerViewItem {
    int getItemType();

    boolean areContentsTheSame(RecyclerViewItem other);
}
