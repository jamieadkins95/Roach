package com.jamieadkins.gwent.deck.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.model.Deck;

import java.util.List;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class DeckRecyclerViewAdapter extends RecyclerView.Adapter<DeckViewHolder> {
    private List<Deck> mDecks;

    public Deck getValueAt(int position) {
        return mDecks.get(position);
    }

    public DeckRecyclerViewAdapter(List<Deck> decks) {
        mDecks = decks;
    }

    @Override
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false));
    }

    @Override
    public void onBindViewHolder(final DeckViewHolder holder, int position) {
        holder.bindDeck(mDecks.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDecks == null) {
            return 0;
        } else {
            return mDecks.size();
        }
    }
}
