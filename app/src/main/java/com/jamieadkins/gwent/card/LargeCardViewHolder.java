package com.jamieadkins.gwent.card;

import android.view.View;

import com.jamieadkins.gwent.data.CardDetails;

/**
 * Holds much more detail about a card.
 */

public class LargeCardViewHolder extends BaseCardViewHolder {
    private LargeCardView mLargeCardView;

    public LargeCardViewHolder(View view) {
        super(view);
        mLargeCardView = (LargeCardView) view;
    }

    @Override
    public void bindItem(CardDetails card) {
        super.bindItem(card);

        mLargeCardView.setCardDetails(card);
    }
}
