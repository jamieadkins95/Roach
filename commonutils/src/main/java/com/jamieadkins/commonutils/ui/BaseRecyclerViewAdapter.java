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
            case SubHeader.TYPE_SUB_HEADER:
                return new SubHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_subheader, parent, false));
            case GoogleNowSubHeader.TYPE_GOOGLE_NOW_SUB_HEADER:
                return new GoogleNowSubHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_google_subheader, parent, false));
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
            notifyItemInserted(mItems.size() - 1);
        } else {
            updateItem(item);
        }
    }

    public void addItem(int position, RecyclerViewItem item) {
        if (!mItems.contains(item)) {
            mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void replaceItem(int position, RecyclerViewItem newItem) {
        mItems.set(position, newItem);
        notifyItemChanged(position);
    }

    public void updateItem(RecyclerViewItem updatedItem) {
        int index = mItems.indexOf(updatedItem);
        if (index != -1) {
            replaceItem(index, updatedItem);
        } else {
            addItem(updatedItem);
        }
    }

    public void removeItem(RecyclerViewItem itemToRemove) {
        if (mItems.contains(itemToRemove)) {
            int index = mItems.indexOf(itemToRemove);
            removeItem(index);
        }
    }

    public void removeItem(int index) {
        mItems.remove(index);
        notifyItemRemoved(index);
    }

    public boolean isAnItemAt(int index) {
        return mItems.size() > index;
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public List<RecyclerViewItem> getItems() {
        return mItems;
    }
}
