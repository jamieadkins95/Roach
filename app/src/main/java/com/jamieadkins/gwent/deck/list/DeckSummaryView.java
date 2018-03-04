package com.jamieadkins.gwent.deck.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.deck.Deck;
import com.jamieadkins.gwent.model.GwentDeckCardCounts;

/**
 * Wrapper for our card detail view.
 */

public class DeckSummaryView extends LinearLayout {

    private TextView mDeckTotalCards;
    private TextView mDeckSilver;
    private TextView mDeckGold;

    public DeckSummaryView(Context context) {
        super(context);
        initialiseView();
    }

    public DeckSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialiseView();
    }

    public DeckSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialiseView();
    }

    protected void initialiseView() {
        inflateView();
        mDeckTotalCards = (TextView) findViewById(R.id.deck_total_cards);
        mDeckSilver = (TextView) findViewById(R.id.deck_silver);
        mDeckGold = (TextView) findViewById(R.id.deck_gold);
    }

    protected void inflateView() {
        inflate(getContext(), R.layout.item_deck_core, this);
    }

    public void setDeck(GwentDeckCardCounts deck) {
        final int invalidColour = ContextCompat.getColor(getContext(), R.color.monsters);
        final int defaultColour = ContextCompat.getColor(getContext(), R.color.textSecondary);

        int totalCardCount = deck.getTotalCardCount();
        mDeckTotalCards.setText(String.format(
                mDeckTotalCards.getContext().getString(R.string.deck_total_cards_value),
                totalCardCount, Deck.MAX_CARD_COUNT));
        mDeckTotalCards.setTextColor(
                totalCardCount < Deck.MIN_CARD_COUNT || totalCardCount > Deck.MAX_CARD_COUNT
                        ? invalidColour : defaultColour);

        int silverCardCount = deck.getSilverCardCount();
        mDeckSilver.setText(String.format(
                getContext().getString(R.string.deck_total_cards_value),
                silverCardCount, Deck.MAX_SILVER_COUNT));
        mDeckSilver.setTextColor(silverCardCount > Deck.MAX_SILVER_COUNT ? invalidColour : defaultColour);

        int goldCardCount = deck.getGoldCardCount();
        mDeckGold.setText(String.format(
                getContext().getString(R.string.deck_total_cards_value),
                goldCardCount, Deck.MAX_GOLD_COUNT));
        mDeckGold.setTextColor(goldCardCount > Deck.MAX_GOLD_COUNT ? invalidColour : defaultColour);
    }
}
