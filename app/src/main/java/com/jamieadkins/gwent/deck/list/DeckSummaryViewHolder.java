package com.jamieadkins.gwent.deck.list;

import android.view.View;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;

/**
 * Holds much more detail about a card.
 */

public class DeckSummaryViewHolder extends BaseViewHolder {
    private Deck mDeck;
    private final DeckSummaryView mDeckSummary;

    public DeckSummaryViewHolder(View view) {
        super(view);
        mDeckSummary = (DeckSummaryView) view.findViewById(R.id.deck_summary);
    }

    @Override
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mDeck = (Deck) item;
        mDeckSummary.setDeck(mDeck);
    }
}
