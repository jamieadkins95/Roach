package com.jamieadkins.gwent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.jgaw.card.BaseApiResult;
import com.jamieadkins.jgaw.card.Card;

import java.util.List;

/**
 * RecyclerViewAdapter that shows a list of cards
 */

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<SimpleCardViewHolder> {
    private List<BaseApiResult> mResults;

    public BaseApiResult getValueAt(int position) {
        return mResults.get(position);
    }

    public CardRecyclerViewAdapter(List<BaseApiResult> items) {
        mResults = items;
    }

    @Override
    public SimpleCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_simple, parent, false);
        return new SimpleCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleCardViewHolder holder, int position) {
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
