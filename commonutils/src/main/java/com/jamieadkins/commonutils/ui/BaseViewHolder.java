package com.jamieadkins.commonutils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Holds base logic for recycler view holders.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private final View mView;
    private T mBoundItem;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void bindItem(T item) {
        mBoundItem = item;
    }

    public T getBoundItem() {
        return mBoundItem;
    }

    public View getView() {
        return mView;
    }
}
