package com.jamieadkins.gwent.deck.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.data.Deck;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Wrapper for our card detail view.
 */

public class DeckBriefSummaryView extends LinearLayout {
    private TextView mDeckTotalCards;

    private TextView mDeckSilver;
    private TextView mDeckGold;

    public DeckBriefSummaryView(Context context) {
        super(context);
        initialiseView();
    }

    public DeckBriefSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialiseView();
    }

    public DeckBriefSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflate(getContext(), R.layout.item_deck_brief, this);
    }

    public void setDeck(Deck deck) {
        final int invalidColour = ContextCompat.getColor(getContext(), R.color.monsters);
        final int defaultColour = ContextCompat.getColor(getContext(), R.color.text_secondary_dark);

        int totalCardCount = deck.getTotalCardCount();
        mDeckTotalCards.setText(String.format(
                mDeckTotalCards.getContext().getString(R.string.deck_total_cards_value),
                totalCardCount, Deck.MAX_CARD_COUNT));
        mDeckTotalCards.setTextColor(
                totalCardCount < Deck.MIN_CARD_COUNT || totalCardCount > Deck.MAX_CARD_COUNT
                        ? invalidColour : defaultColour);

        deck.getSilverCardCount()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer silverCardCount) {
                        mDeckSilver.setText(String.format(
                                getContext().getString(R.string.deck_total_cards_value),
                                silverCardCount, Deck.MAX_SILVER_COUNT));
                        mDeckSilver.setTextColor(silverCardCount > Deck.MAX_SILVER_COUNT ? invalidColour : defaultColour);
                    }
                });

        deck.getGoldCardCount()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer goldCardCount) {
                        mDeckGold.setText(String.format(
                                getContext().getString(R.string.deck_total_cards_value),
                                goldCardCount, Deck.MAX_GOLD_COUNT));
                        mDeckGold.setTextColor(goldCardCount > Deck.MAX_GOLD_COUNT ? invalidColour : defaultColour);
                    }
                });
    }
}
