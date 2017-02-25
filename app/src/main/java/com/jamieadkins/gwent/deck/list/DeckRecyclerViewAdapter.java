package com.jamieadkins.gwent.deck.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class DeckRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    public static final int TYPE_DECK = 1000;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DECK:
                return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_deck, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
