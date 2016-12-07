package com.jamieadkins.gwent.deck.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.cardui.BaseCardViewHolder;
import com.jamieadkins.gwent.model.Deck;
import com.jamieadkins.jgaw.card.CardDetails;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final TextView mDeckName;
    private final TextView mDeckMelee;
    private final TextView mDeckRanged;
    private final TextView mDeckSiege;
    private final TextView mDeckTotalCards;
    private final TextView mDeckTotalAttack;

    private Deck mBoundDeck;

    public DeckViewHolder(View view) {
        super(view);
        mView = view;
        mDeckName = (TextView) view.findViewById(R.id.deck_name);
        mDeckMelee = (TextView) view.findViewById(R.id.deck_melee);
        mDeckRanged = (TextView) view.findViewById(R.id.deck_range);
        mDeckSiege = (TextView) view.findViewById(R.id.deck_siege);
        mDeckTotalCards = (TextView) view.findViewById(R.id.deck_total_cards);
        mDeckTotalAttack = (TextView) view.findViewById(R.id.deck_total_attack);
    }

    public void bindDeck(Deck deck) {
        mBoundDeck = deck;

        mDeckName.setText(mBoundDeck.getName());
        mDeckMelee.setText("40");
        mDeckRanged.setText("24");
        mDeckSiege.setText("23");
        mDeckTotalCards.setText("Total Cards: 27");
        mDeckTotalAttack.setText("Total Attack: 80");
    }
}
