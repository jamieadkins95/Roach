package com.jamieadkins.gwent.card;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CardRecyclerViewAdapter extends BaseRecyclerViewAdapter<CardDetails> {
    private Detail mDetail;

    public enum Detail {
        SMALL,
        LARGE
    }

    public CardRecyclerViewAdapter(Detail detail) {
        super();
        mDetail = detail;
    }

    public CardRecyclerViewAdapter() {
        super();
        mDetail = Detail.SMALL;
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
}
