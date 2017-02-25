package com.jamieadkins.commonutils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds base logic for recycler view adapters.
 */

public abstract class BaseRecyclerViewAdapter
        extends RecyclerView.Adapter<BaseViewHolder> {
    private List<RecyclerViewItem> mItems;

    public RecyclerViewItem getItemAt(int position) {
        return mItems.get(position);
    }

    public BaseRecyclerViewAdapter() {
        mItems = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getItemType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Header.TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_header, parent, false));
            default:
                throw new RuntimeException("Detail level has not been implemented.");
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    public void addItem(RecyclerViewItem item) {
        if (!mItems.contains(item)) {
            mItems.add(item);
            notifyDataSetChanged();
        }
    }

    public void addItem(int position, RecyclerViewItem item) {
        if (!mItems.contains(item)) {
            mItems.add(position, item);
            notifyDataSetChanged();
        }
    }

    public void removeItem(RecyclerViewItem itemToRemove) {
        mItems.remove(itemToRemove);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public List<RecyclerViewItem> getItems() {
        return mItems;
    }
}
