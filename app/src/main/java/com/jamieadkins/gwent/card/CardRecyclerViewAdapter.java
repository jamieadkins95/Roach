package com.jamieadkins.gwent.card;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.collection.CollectionCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CardRecyclerViewAdapter extends BaseRecyclerViewAdapter<CardDetails> {
    private Detail mDetail;

    public enum Detail {
        SMALL,
        LARGE,
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
                return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_simple_layout, parent, false));
            case LARGE:
                return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_detail_layout, parent, false));
            default:
                throw new RuntimeException("Detail level has not been implemented.");
        }
    }
}
