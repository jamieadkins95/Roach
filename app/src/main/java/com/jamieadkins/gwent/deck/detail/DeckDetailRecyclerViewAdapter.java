package com.jamieadkins.gwent.deck.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.collection.CollectionCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;

import java.util.Map;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class DeckDetailRecyclerViewAdapter extends CardRecyclerViewAdapter {
    private DeckDetailCardViewHolder.DeckDetailButtonListener mDeckDetailButtonListener;
    private Map<String, Integer> mCardCounts;

    public DeckDetailRecyclerViewAdapter(DeckDetailCardViewHolder.DeckDetailButtonListener
                                                 deckDetailButtonListener) {
        super();
        mDeckDetailButtonListener = deckDetailButtonListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CardRecyclerViewAdapter.TYPE_CARD:
                return new DeckDetailCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_deck_detail_layout, parent, false),
                        mDeckDetailButtonListener);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof DeckDetailCardViewHolder) {
            DeckDetailCardViewHolder deckDetailCardViewHolder = (DeckDetailCardViewHolder) holder;

            CardDetails cardDetails = (CardDetails) getItemAt(position);
            if (mCardCounts != null &&
                    mCardCounts.containsKey(cardDetails.getIngameId())) {
                deckDetailCardViewHolder.setItemCount(mCardCounts.get(cardDetails.getIngameId()));
            } else {
                deckDetailCardViewHolder.setItemCount(0);
            }
        }
    }

    public void setCardCounts(Map<String, Integer> cardCounts) {
        if (mCardCounts == null || !mCardCounts.equals(cardCounts)) {
            mCardCounts = cardCounts;
            notifyDataSetChanged();
        }
    }
}
