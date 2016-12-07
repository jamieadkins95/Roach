package com.jamieadkins.gwent.cardui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.jgaw.card.BaseApiResult;
import com.jamieadkins.jgaw.card.CardDetails;

import java.util.List;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {
    private List<CardDetails> mResults;
    private Detail mDetail;

    public enum Detail {
        SMALL,
        LARGE
    }

    public BaseApiResult getValueAt(int position) {
        return mResults.get(position);
    }

    public CardRecyclerViewAdapter(List<CardDetails> items, Detail detail) {
        mResults = items;
        mDetail = detail;
    }

    public CardRecyclerViewAdapter(List<CardDetails> items) {
        this(items, Detail.SMALL);
    }

    @Override
    public BaseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (mDetail) {
            case SMALL:
                return new SimpleCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_card_small, parent, false));
            case LARGE:
                return new LargeCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_card_large, parent, false));
            default:
                throw new RuntimeException("Detail level has not been implemented.");
        }
    }

    @Override
    public void onBindViewHolder(final BaseCardViewHolder holder, int position) {
        holder.bindCard(mResults.get(position));
    }

    @Override
    public int getItemCount() {
        if (mResults == null) {
            return 0;
        } else {
            return mResults.size();
        }
    }
}
