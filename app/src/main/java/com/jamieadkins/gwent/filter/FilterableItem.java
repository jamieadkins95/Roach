package com.jamieadkins.gwent.filter;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;

public class FilterableItem<F> implements RecyclerViewItem {
    private F mFilterable;
    private boolean mChecked;

    public FilterableItem(F filterable, boolean checked) {
        mFilterable = filterable;
        mChecked = checked;
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public int getItemType() {
        return FilterRecyclerViewAdapter.TYPE_FILTER;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FilterableItem && mFilterable.equals(((FilterableItem) obj).mFilterable);
    }

    public F getFilterable() {
        return mFilterable;
    }

    @Override
    public boolean areContentsTheSame(RecyclerViewItem other) {
        return equals(other);
    }
}
