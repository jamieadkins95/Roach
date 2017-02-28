package com.jamieadkins.gwent.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.deck.list.DeckViewHolder;

/**
 * Is able to contain different Gwent elements in a list (cards, decks, etc.).
 */

public class GwentRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    public static final int TYPE_CARD = 2000;
    public static final int TYPE_CARD_LEADER = 2001;
    public static final int TYPE_DECK = 1000;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CARD:
            case TYPE_CARD_LEADER:
                return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_detail_layout, parent, false));
            case TYPE_DECK:
                return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_deck, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
