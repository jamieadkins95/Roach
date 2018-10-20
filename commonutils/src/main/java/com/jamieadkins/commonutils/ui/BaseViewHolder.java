package com.jamieadkins.commonutils.ui;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Holds base logic for recycler view holders.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private RecyclerViewItem mBoundItem;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void bindItem(RecyclerViewItem item) {
        mBoundItem = item;
    }

    public RecyclerViewItem getBoundItem() {
        return mBoundItem;
    }

    public View getView() {
        return mView;
    }
}
