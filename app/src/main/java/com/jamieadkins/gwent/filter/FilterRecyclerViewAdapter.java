package com.jamieadkins.gwent.filter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;

/**
 * Created by jamiea on 01/03/17.
 */

public class FilterRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    protected static final int TYPE_FILTER = 100;
    private FilterBottomSheetDialogFragment.FilterUiListener mListener;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FILTER:
                return new FilterViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_checkable, parent, false), mListener);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    protected void setFilterListener(FilterBottomSheetDialogFragment.FilterUiListener listener) {
        mListener = listener;
    }
}
