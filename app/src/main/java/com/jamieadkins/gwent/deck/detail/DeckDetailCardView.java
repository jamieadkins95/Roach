package com.jamieadkins.gwent.deck.detail;

import android.content.Context;
import android.util.AttributeSet;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.LargeCardView;

/**
 * Card view to be used in the collection view.
 */

public class DeckDetailCardView extends LargeCardView {
    public DeckDetailCardView(Context context) {
        super(context);
    }

    public DeckDetailCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeckDetailCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void inflateView() {
        inflate(getContext(), R.layout.item_card_deck_detail, this);
    }
}
