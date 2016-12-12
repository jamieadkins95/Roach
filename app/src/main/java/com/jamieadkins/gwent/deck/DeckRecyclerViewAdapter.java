package com.jamieadkins.gwent.deck;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class DeckRecyclerViewAdapter extends BaseRecyclerViewAdapter<Deck> {

    @Override
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false));
    }

    public void removeDeck(String removedDeckId) {
        Deck deckToRemove = null;
        for (Deck deck : getItems()) {
            if (deck.getId().equals(removedDeckId)) {
                deckToRemove = deck;
            }
        }

        getItems().remove(deckToRemove);
        notifyDataSetChanged();
    }
}
